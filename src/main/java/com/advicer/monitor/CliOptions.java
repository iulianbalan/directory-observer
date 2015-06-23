package com.advicer.monitor;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class CliOptions {

	private String[] args;
	private Options options;
	private CommandLine line;


	@SuppressWarnings("unused")
	private CliOptions(){}

	protected CliOptions(String[]args){
		this.args = args;
		this.options = new Options();
		addOptions();
	}
	
	protected Options getOptions() {
		return this.options;
	}

	private void addOptions() {
		
		OptionGroup groupReq = new OptionGroup();
		
		groupReq.addOption(Option.builder("p")
				.longOpt("path")
				.hasArg()
				.desc("Give the absolute path to the directory to monitor")
				.build());
		groupReq.setRequired(true);
		
		
		OptionGroup groupNotReq = new OptionGroup();
		
		groupNotReq.addOption(Option.builder("C")
//				.longOpt("ignore-file-creation")
				.desc("Select the flag of the monitoring if you want to exclude file creation")
				.build());
		
		groupNotReq.addOption(Option.builder("D")
//				.longOpt("ignore-file-deletion")
				.desc("Select the flag of the monitoring if you want to exclude file deletion")
				.build());
		
		groupNotReq.addOption(Option.builder("M")
//				.longOpt("ignore-file-modification")
				.desc("Select the flag of the monitoring if you want to exclude file modification")
				.build());
		groupNotReq.setRequired(false);
		
		this.options
			.addOptionGroup(groupReq)
			.addOptionGroup(groupNotReq);

	}

	protected CommandLine getCommandLine() throws ParseException {
		if (line == null) {
			// 	create the command line parser
			CommandLineParser parser = new DefaultParser();
			this.line = parser.parse(this.options, this.args);
		}
		return this.line;
	}

}
