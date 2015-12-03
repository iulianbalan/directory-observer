package com.advicer.monitor;

import com.advicer.monitor.cli.CliOptions;
import com.advicer.monitor.cli.CliOptionsFactory;
import com.advicer.monitor.exceptions.DirectoryAccessException;
import com.advicer.monitor.queue.QueueHandler;
import com.advicer.monitor.queue.QueueProcessor;
import com.advicer.monitor.util.DirectoryObserverEventsConstants;
import com.advicer.monitor.util.Scanner;
import com.advicer.monitor.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.ParseException;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.net.ConnectException;
import java.nio.file.StandardWatchEventKinds;

/**
 * @author Iulian Balan
 */
@Slf4j
public class IntegrationManager {

    private static final String CONNECTION_ERROR_RABBIT = "There was an error connecting to RabbitMQ Server. Please check your credentials";
    private static final String CLI_USAGE = "java -jar <jar-file> -p=path [options]\n\n";
    private static final String ERROR_DIRECTORY = "Make sure the path '%s' exists and and have read access to it";

    @Autowired
    private QueueHandler queueHandler;

    @Autowired
    private QueueProcessor queueProcessor;

    public IntegrationManager() {
    }

    public void operate(String[] args) {

        CliOptions cliopts = CliOptionsFactory.getCliOptions(args);

        try {

            // parse the command line arguments
            cliopts.useDirectoryObserverOptions().parse();

            // validate that path has been set
            String path = cliopts.getOptionValue(DirectoryObserverEventsConstants.PATH);
            if (!Utils.checkDirectory(path)) {
                throw new DirectoryAccessException(String.format(ERROR_DIRECTORY, path));
            }

            Scanner scanner = new Scanner(path);

            scanner.excludeEventFromListening(StandardWatchEventKinds.ENTRY_CREATE,
                    cliopts.hasOption(DirectoryObserverEventsConstants.CREATION))
                    .excludeEventFromListening(StandardWatchEventKinds.ENTRY_DELETE,
                            cliopts.hasOption(DirectoryObserverEventsConstants.DELETION))
                    .excludeEventFromListening(StandardWatchEventKinds.ENTRY_MODIFY,
                            cliopts.hasOption(DirectoryObserverEventsConstants.MODIFICATION))
                    .addObserver(queueHandler);

            // Creating the threads based on Runnable Interfaces implementations
            Thread processor = new Thread(queueProcessor);

            // Starting threads
            processor.start();
            scanner.startMonitoring();

        } catch (ParseException exp) {
            cliopts.printUsage(CLI_USAGE);
        } catch (ConnectException e) { //catching exception above IOException to be reachable
            log.error(CONNECTION_ERROR_RABBIT, e);
        } catch (DirectoryAccessException | IOException exp) {
            log.error(exp.getMessage(), exp);
        }
    }
}
