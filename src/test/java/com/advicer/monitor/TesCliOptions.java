package com.advicer.monitor;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.advicer.monitor.CliOptions.DirectoryObserver;

public class TesCliOptions {
	private static ExpectedException e;
	
	@BeforeClass
	public static void init() {
		e = ExpectedException.none();
	}

	@Test(expected = ParseException.class)
	public void tesHasOptionBeforeParsing() throws ParseException {
		CliOptions cli = new CliOptions(new String[0]);
		e.expect(ParseException.class);
		cli.hasOption(DirectoryObserver.PATH);
	}
	
	@Test(expected = ParseException.class)
	public void tesGetOptionBeforeParsing() throws ParseException {
		CliOptions cli = new CliOptions(new String[0]);
		e.expect(ParseException.class);
		cli.getOptionValue(DirectoryObserver.PATH);
	}
	
	@Test
	public void tesUseDirectoryMonitorOptionsWithPath() throws ParseException {
		CliOptions cli = new CliOptions(new String[0]);//{ "--path=C:\\Users\\balans\\Desktop\\lol"});
		cli.useDirectoryMonitorOptions();
		OptionGroup optG = cli.getOptions().getOptionGroup(Option.builder("p").build());
		Assert.assertTrue(optG.isRequired());
		Assert.assertEquals(1, optG.getOptions().size());
	}
	
	@Test
	public void tesUseDirectoryMonitorOptionsWithFlags() throws ParseException {
		CliOptions cli = new CliOptions(new String[0]);//{ "--path=C:\\Users\\balans\\Desktop\\lol -C -D -M"});
		cli.useDirectoryMonitorOptions();
		OptionGroup optG = cli.getOptions().getOptionGroup(Option.builder("C").build());
		Assert.assertFalse(optG.isRequired());
		Assert.assertEquals(3, optG.getOptions().size());
	}
	
}
