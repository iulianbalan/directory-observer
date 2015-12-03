package com.advicer.monitor.cli;

/**
 * Factory class
 * 
 * @author Iulian Balan
 *
 */
public class CliOptionsFactory {
	
	/** Gets an implementation of this interface 
	 * 
	 * @param args array of arguments
	 * @return an instance of CliOptions class
	 */
	public static CliOptions getCliOptions(String[] args) {
		return new CliOptionsImpl(args);
	}

}
