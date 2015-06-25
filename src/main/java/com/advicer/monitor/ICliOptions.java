package com.advicer.monitor;

import org.apache.commons.cli.ParseException;

import com.advicer.monitor.CliOptions.DirectoryObserver;

/**
 * Interface used to interact with CliOptions
 * 
 * @author Iulian Balan
 *
 */
public interface ICliOptions {

	/**
	 * Use directory monitor options that include:
	 * required full path and optional flags C D and M
	 * 
	 * @return this instance
	 */
	public ICliOptions useDirectoryMonitorOptions();

	/**
	 * Parse function 
	 * 
	 * @return parsed CommandLine
	 * @throws ParseException
	 */
	public void parse() throws ParseException;

	/**
	 * Controls if there is an option in the command
	 * line provided
	 * 
	 * @param arg DirectoryObserver enum
	 * @return true if the option was found
	 * @throws ParseException if the command line was
	 * not previously parsed
	 */
	public boolean hasOption(DirectoryObserver arg) throws ParseException;

	/**
	 * Get the value of an argument provided at command line
	 * 
	 * @param arg DirectoryObserver enum
	 * @return the value of the arguments selected
	 * @throws ParseException if the command line was
	 * not previously parsed
	 */
	public String getOptionValue(DirectoryObserver arg) throws ParseException;

	/**
	 * Prints usage of the execution
	 * 
	 * @param cmdLineSyntax
	 */
	public void printUsage(String cmdLineSyntax);

}