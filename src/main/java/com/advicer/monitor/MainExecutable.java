package com.advicer.monitor;

import java.io.IOException;
import java.net.ConnectException;
import java.nio.file.StandardWatchEventKinds;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeoutException;

import com.advicer.monitor.AMQP.RabbitMQCredentials;
import com.advicer.monitor.cli.CliOptions;
import com.advicer.monitor.cli.CliOptionsFactory;
import com.advicer.monitor.dto.Message;
import com.advicer.monitor.exceptions.DirectoryAccessException;
import com.advicer.monitor.queue.QueueHandler;
import com.advicer.monitor.queue.QueueProcessor;
import com.advicer.monitor.util.DirectoryObserverEventsConstants;
import com.advicer.monitor.util.Scanner;
import com.advicer.monitor.util.Utils;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main class containing main function
 * 
 * @author Iulian Balan
 *
 */
public class MainExecutable {
	

	private static final String CONNECTION_ERROR_RABBIT = "There was an error connecting to RabbitMQ Server. Please check your credentials";
	private static final String CLI_USAGE = "java -jar <jar-file> -p=path [options]\n\n";
	private static final int NUMBER_ARRAY_MESSAGGES = 10000;
	private static final String ERROR_DIRECTORY = "Make sure the path specified exists and and have read access to it";
	private static final String PROPERTY_FILE_RABBIT = "rabbitmq.properties";

	private final static Logger log = LoggerFactory.getLogger(MainExecutable.class);

	public static void main(String[] args) throws TimeoutException {
		
		CliOptions cliopts = CliOptionsFactory.getCliOptions(args);

		try {

			// parse the command line arguments
			cliopts.useDirectoryObserverOptions().parse();

			// validate that path has been set
			String path = cliopts.getOptionValue(DirectoryObserverEventsConstants.PATH);
			if (!Utils.checkDirectory(path)) {
				throw new DirectoryAccessException(ERROR_DIRECTORY);
			}

			BlockingQueue<Message> msgQueue = new ArrayBlockingQueue<>(
					NUMBER_ARRAY_MESSAGGES);
			QueueHandler qh = new QueueHandler(msgQueue);
			QueueProcessor qp = new QueueProcessor(msgQueue);
			
			qp.connectToRabbitMq(new RabbitMQCredentials(Utils
					.readPropertyFile(PROPERTY_FILE_RABBIT)));

			Scanner scanner = new Scanner(path);

			scanner.excludeEventFromListening(StandardWatchEventKinds.ENTRY_CREATE,
						cliopts.hasOption(DirectoryObserverEventsConstants.CREATION))
					.excludeEventFromListening(StandardWatchEventKinds.ENTRY_DELETE,
							cliopts.hasOption(DirectoryObserverEventsConstants.DELETION))
					.excludeEventFromListening(StandardWatchEventKinds.ENTRY_MODIFY,
							cliopts.hasOption(DirectoryObserverEventsConstants.MODIFICATION))
					.addObserver(qh);

			// Creating the threads based on Runnable Interfaces implementations
			Thread processor = new Thread(qp);

			// Starting threads
			processor.start();
			scanner.startMonitoring();

		} catch (ParseException exp) {
			cliopts.printUsage(CLI_USAGE);
		}	catch (ConnectException e){ //catching exception above IOException to be reachable
			log.error(CONNECTION_ERROR_RABBIT, e);
		} catch (DirectoryAccessException | IOException exp) {
			log.error(exp.getMessage(), exp);
		} 
	}
}
