package com.advicer.monitor;

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
	public static ICliOptions getCliOptions(String[] args) {
		return new CliOptions(args);
	}

}
