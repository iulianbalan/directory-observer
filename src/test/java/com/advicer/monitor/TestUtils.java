package com.advicer.monitor;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import static org.mockito.Mockito.*;

public class TestUtils {

	@Test
	public void testCheckDirectory() {
		
		File f = mock(File.class);
		
		when(f.exists()).thenReturn(true);
		when(f.isDirectory()).thenReturn(true);
		when(f.canRead()).thenReturn(true);
		assertTrue(Utils.checkDirectory(f));
	}

}
