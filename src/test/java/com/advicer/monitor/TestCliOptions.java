package com.advicer.monitor;

import com.advicer.monitor.cli.CliOptionsImpl;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.advicer.monitor.util.DirectoryObserverEventsConstants;

public class TestCliOptions {
	private static ExpectedException e;
	
	@BeforeClass
	public static void init() {
		e = ExpectedException.none();
	}

	@Test(expected = ParseException.class)
	public void tesHasOptionBeforeParsing() throws ParseException {
		CliOptionsImpl cli = new CliOptionsImpl(new String[0]);
		e.expect(ParseException.class);
		cli.hasOption(DirectoryObserverEventsConstants.PATH);
	}
	
	@Test(expected = ParseException.class)
	public void tesGetOptionBeforeParsing() throws ParseException {
		CliOptionsImpl cli = new CliOptionsImpl(new String[0]);
		e.expect(ParseException.class);
		cli.getOptionValue(DirectoryObserverEventsConstants.PATH);
	}
	
	@Test
	public void tesUseDirectoryMonitorOptionsWithPath() throws ParseException {
		CliOptionsImpl cli = new CliOptionsImpl(new String[0]);//{ "--path=C:\\Users\\balans\\Desktop\\lol"});
		cli.useDirectoryObserverOptions();
		OptionGroup optG = cli.getOptions().getOptionGroup(Option.builder("p").build());
		Assert.assertTrue(optG.isRequired());
		Assert.assertEquals(1, optG.getOptions().size());
	}
	
	@Test
	public void tesUseDirectoryMonitorOptionsWithFlags() throws ParseException {
		CliOptionsImpl cli = new CliOptionsImpl(new String[0]);//{ "--path=C:\\Users\\balans\\Desktop\\lol -C -D -M"});
		cli.useDirectoryObserverOptions();
		OptionGroup optG = cli.getOptions().getOptionGroup(Option.builder("C").build());
		Assert.assertFalse(optG.isRequired());
		Assert.assertEquals(3, optG.getOptions().size());
	}
	
}
