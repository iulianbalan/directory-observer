package com.advicer.monitor;

import java.io.IOException;
import java.net.ConnectException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeoutException;

import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.advicer.monitor.CliOptions.DirectoryObserver;
import com.advicer.monitor.Scanner.Events;

/**
 * Main class containing main function
 * 
 * @author Iulian Balan
 *
 */
public class Cli {
	

	private static final String CONNECTION_ERROR_RABBIT = "There was an error connecting to RabbitMQ Server. Please check your credentials";
	private static final String CLI_USAGE = "java -jar <jar-file> -p=path [options]\n\n";
	private static final int NUMBER_ARRAY_MESSAGGES = 10000;
	private static final String ERROR_DIRECTORY = "Make sure the path specified exists and and have read access to it";
	private static final String PROPERTY_FILE_RABBIT = "rabbitmq.properties";

	private final static Logger log = LoggerFactory.getLogger(Cli.class);

	private static String path;

	public static void main(String[] args) throws TimeoutException {

		ICliOptions cliopts = CliOptionsFactory.getCliOptions(args);

		try {

			// parse the command line arguments
			cliopts.useDirectoryMonitorOptions().parse();

			// validate that path has been set
			path = cliopts.getOptionValue(DirectoryObserver.PATH);
			if (!Utils.checkDirectory(path)) {
				throw new DirectoryAccessException(ERROR_DIRECTORY);
			}

			BlockingQueue<MessagePojo> msgQueue = new ArrayBlockingQueue<>(
					NUMBER_ARRAY_MESSAGGES);
			QueueHandler qh = new QueueHandler(msgQueue);
			QueueProcessor qp = new QueueProcessor(msgQueue);
			
			qp.connectToRabbitMq(new RabbitMQCredentials(Utils
					.readPropertyFile(PROPERTY_FILE_RABBIT)));

			Scanner scanner = new Scanner(path);

			scanner.excludeEventFromListening(Events.CREATION,
						cliopts.hasOption(DirectoryObserver.CREATION))
					.excludeEventFromListening(Events.DELETION,
							cliopts.hasOption(DirectoryObserver.DELETION))
					.excludeEventFromListening(Events.MODIFICATION,
							cliopts.hasOption(DirectoryObserver.MODIFICATION))
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
