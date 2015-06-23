package com.advicer.monitor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;


public class Utils {

	
	protected static boolean checkDirectory(String path) {
		
		File f = Paths.get(path).toFile();

		if (!f.exists() || !f.isDirectory() || !f.canRead()) {
			return false;
		}
		return true;
		
	}
	
	protected static String messagePojoToString(MessagePojo msg) throws JsonGenerationException, JsonMappingException, IOException {
		//1. Convert Java object to JSON format
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		return ow.writeValueAsString(msg);
	}
}
