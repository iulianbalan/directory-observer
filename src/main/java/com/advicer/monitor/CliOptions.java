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
 * Class used for the command line options and arguments
 * This class is an implementation of ICliOptions
 * 
 * @author Iulian Balan
 *
 */
/**
 * @author Ilinca
 *
 */
public class CliOptions implements ICliOptions {

	private static final String COMMAND_LINE_NOT_PARSED = "Command line not yet parsed";
	
	//	Constants for cli argument descriptions
	private static final String PATH_DESC = "Give the absolute path to the directory to monitor";
	private static final String EXCLUDE_CREATION_DESC = "Select the flag of the monitoring if you want to exclude file CREATION";
	private static final String EXCLUDE_MODIFICATION_DESC = "Select the flag of the monitoring if you want to exclude file MODIFICATION";
	private static final String EXCLUDE_DELETION_DESC = "Select the flag of the monitoring if you want to exclude file DELETION";
	
	public enum DirectoryObserver {
		PATH("p", "path"), CREATION("C"), DELETION("D"), MODIFICATION("M");
		
		//Making private fields final to avoid evil code
		private final String flag;
		private final String arg;
		
		//Every constructor has to initialize all final fields not yet initialized
		DirectoryObserver(String flag){ this.flag = flag; this.arg = null; } 
		DirectoryObserver(String flag, String arg){ this.flag = flag; this.arg = arg; }
		
		public String getFlag() { return this.flag; }
		public String getArg() { return this.arg; }
		
	}
	
//	private fields
	private String[] args;
	private Options options;
	private CommandLine line;



	/**
	 * Main constructor that takes Main Class' arguments
	 * 
	 * @param args
	 */
	public CliOptions(String[]args){
		this.args = args;
		this.options = new Options();
	}
	
	protected Options getOptions() {
		return this.options;
	}

	/* (non-Javadoc)
	 * @see com.advicer.monitor.ICliOptions#useDirectoryMonitorOptions()
	 */
	@Override
	public ICliOptions useDirectoryObserverOptions() {
		
		OptionGroup groupReq = new OptionGroup();
		
		groupReq.addOption(Option.builder(DirectoryObserver.PATH.getFlag())
				.longOpt(DirectoryObserver.PATH.getArg())
				.hasArg()
				.desc(PATH_DESC)
				.build());
		groupReq.setRequired(true);
		
		
		OptionGroup groupNotReq = new OptionGroup();
		
		groupNotReq.addOption(Option.builder(DirectoryObserver.CREATION.getFlag())
//				.longOpt("ignore-file-creation")
				.desc(EXCLUDE_CREATION_DESC)
				.build());
		
		groupNotReq.addOption(Option.builder(DirectoryObserver.DELETION.getFlag())
//				.longOpt("ignore-file-deletion")
				.desc(EXCLUDE_DELETION_DESC)
				.build());
		
		groupNotReq.addOption(Option.builder(DirectoryObserver.MODIFICATION.getFlag())
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

	/* (non-Javadoc)
	 * @see com.advicer.monitor.ICliOptions#parse()
	 */
	@Override
	public void parse() throws ParseException {
		if (line == null) {
			// 	create the command line parser
			CommandLineParser parser = new DefaultParser();
			this.line = parser.parse(this.options, this.args);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.advicer.monitor.ICliOptions#hasOption(com.advicer.monitor.CliOptions.DirectoryObserver)
	 */
	@Override
	public boolean hasOption(DirectoryObserver arg) throws ParseException{
		if (line == null) {
			throw new ParseException(COMMAND_LINE_NOT_PARSED);
		}
		return this.line.hasOption(arg.getFlag());

	}
	
	/* (non-Javadoc)
	 * @see com.advicer.monitor.ICliOptions#getOptionValue(com.advicer.monitor.CliOptions.DirectoryObserver)
	 */
	@Override
	public String getOptionValue(DirectoryObserver arg) throws ParseException {
		if (line == null) {
			throw new ParseException(COMMAND_LINE_NOT_PARSED);
		}
		return this.line.getOptionValue(arg.getFlag());
	}
	
	/* (non-Javadoc)
	 * @see com.advicer.monitor.ICliOptions#printUsage(java.lang.String)
	 */
	@Override
	public void  printUsage(String cmdLineSyntax) {
		HelpFormatter form = new HelpFormatter();
		form.printHelp(cmdLineSyntax, this.options);
	}
}
