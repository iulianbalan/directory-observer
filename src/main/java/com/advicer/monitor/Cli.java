package com.advicer.monitor;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Cli {

	private final static Logger log = LoggerFactory.getLogger(Cli.class);
	private static String path;


	public static void main(String[] args) throws IOException {

		CliOptions cliopts = new CliOptions(args);

		try {
			// parse the command line arguments
			CommandLine cli = cliopts.getCommandLine();


			// validate that block-size has been set
			path = cli.getOptionValue("path");
			if (!Utils.checkDirectory(path)) {
				throw new DirectoryAccessException("Make sure the path specified exists and and have read access to it");
			}
			
			
			BlockingQueue<MessagePojo> msgQueue = new ArrayBlockingQueue<>(10000);
			QueueHandler qh = new QueueHandler(msgQueue);
			QueueProcessor qp = new QueueProcessor(msgQueue);
			
			Scanner scanner = new Scanner();
			
			scanner.excludeCreate(!cli.hasOption("C"));
			scanner.excludeDelete(!cli.hasOption("D"));
			scanner.excludeEdit(!cli.hasOption("M"));

			scanner.monitor(path);
						
			scanner.addObserver(qh);
			
			
			//Creating the threads based on Runnable Interfaces implementations
			Thread processor = new Thread(qp);
			
			//Starting threads
			processor.start();
			scanner.run();
			
		}
		catch( ParseException exp ) {
			HelpFormatter form = new HelpFormatter();
			form.printHelp("java -jar <jar-file> -p=path [options]\n\n", cliopts.getOptions());
		}
		catch (DirectoryAccessException exp) {
			log.error(exp.getMessage());
		}
	}
}
