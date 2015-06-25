package com.advicer.monitor;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * @author Iulian Balan
 *
 */
public class CliOptions {

private static final String COMMAND_LINE_NOT_PARSED = "Command line not yet parsed";
	//	Constants for 'path' cli argument
	private static final String PATH_DESC = "Give the absolute path to the directory to monitor";
	private static final String PATH_ARG_SHORT = "p";
	private static final String PATH_ARG_LONG = "path";
	
//	Constants for 'path' exclusion flags arguments
	private static final String EXCLUDE_CREATION_ARG_SHORT = "C";
	private static final String EXCLUDE_MODIFICATION_ARG_SHORT = "M";
	private static final String EXCLUDE_DELETION_ARG_SHORT = "D";
	private static final String EXCLUDE_CREATION_DESC = "Select the flag of the monitoring if you want to exclude file CREATION";
	private static final String EXCLUDE_MODIFICATION_DESC = "Select the flag of the monitoring if you want to exclude file MODIFICATION";
	private static final String EXCLUDE_DELETION_DESC = "Select the flag of the monitoring if you want to exclude file DELETION";
	
	public enum DirectoryObserver {
		PATH, CREATION, DELETION, MODIFICATION
	}
	
//	private fields
	private String[] args;
	private Options options;
	private CommandLine line;


	@SuppressWarnings("unused")
	private CliOptions(){}

	public CliOptions(String[]args){
		this.args = args;
		this.options = new Options();
	}
	
	protected Options getOptions() {
		return this.options;
	}

	/**
	 * Use directory monitor options that include:
	 * required full path and optional flags C D and M
	 * 
	 * @return this instance
	 */
	public CliOptions useDirectoryMonitorOptions() {
		
		OptionGroup groupReq = new OptionGroup();
		
		groupReq.addOption(Option.builder(PATH_ARG_SHORT)
				.longOpt(PATH_ARG_LONG)
				.hasArg()
				.desc(PATH_DESC)
				.build());
		groupReq.setRequired(true);
		
		
		OptionGroup groupNotReq = new OptionGroup();
		
		groupNotReq.addOption(Option.builder(EXCLUDE_CREATION_ARG_SHORT)
//				.longOpt("ignore-file-creation")
				.desc(EXCLUDE_CREATION_DESC)
				.build());
		
		groupNotReq.addOption(Option.builder(EXCLUDE_DELETION_ARG_SHORT)
//				.longOpt("ignore-file-deletion")
				.desc(EXCLUDE_DELETION_DESC)
				.build());
		
		groupNotReq.addOption(Option.builder(EXCLUDE_MODIFICATION_ARG_SHORT)
//				.longOpt("ignore-file-modification")
				.desc(EXCLUDE_MODIFICATION_DESC)
				.build());
		groupNotReq.setRequired(false);
		
		this.options
			.addOptionGroup(groupReq)
			.addOptionGroup(groupNotReq);
		
//		return this to chain multiple calls
		return this;
	}

	/**
	 * Parse function 
	 * 
	 * @return parsed CommandLine
	 * @throws ParseException
	 */
	public void parse() throws ParseException {
		if (line == null) {
			// 	create the command line parser
			CommandLineParser parser = new DefaultParser();
			this.line = parser.parse(this.options, this.args);
		}
	}
	
	public boolean hasOption(DirectoryObserver arg) throws ParseException{
		if (line == null) {
			throw new ParseException(COMMAND_LINE_NOT_PARSED);
		}
		return this.line.hasOption(getArgMap(arg));

	}
	
	public String getOptionValue(DirectoryObserver arg) throws ParseException {
		if (line == null) {
			throw new ParseException(COMMAND_LINE_NOT_PARSED);
		}
		return this.line.getOptionValue(getArgMap(arg));
	}
	
	private String getArgMap(DirectoryObserver arg) throws ParseException {
		switch (arg) {
		case PATH: return PATH_ARG_SHORT;
		case CREATION: return EXCLUDE_CREATION_ARG_SHORT;
		case DELETION: return EXCLUDE_DELETION_ARG_SHORT;
		case MODIFICATION: return EXCLUDE_MODIFICATION_ARG_SHORT;
		default:
			//This should never happen since the input is limited by the definition
			//of the enum
			throw new ParseException(COMMAND_LINE_NOT_PARSED);
		}
	}
	
	public void  printUsage(String cmdLineSyntax) {
		HelpFormatter form = new HelpFormatter();
		form.printHelp(cmdLineSyntax, this.options);
	}
	

}
