package com.advicer.monitor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Properties;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;


/**
 * Utility class
 * 
 * @author Iulian Balan
 *
 */
public class Utils {

	
	/**
	 * Utility function meant to control all system's properties 
	 * necessary to perform a monitoring
	 * 
	 * @param path full path to the monitored folder
	 * @return true if everything is all right
	 */
	public static boolean checkDirectory(String path) {
		
		File f = Paths.get(path).toFile();
		return f.exists() && f.isDirectory() && f.canRead();		
	}
	
	/**
	 * Serializing object to JSON
	 * 
	 * @param o any object
	 * @return a String serialized in JSON
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static String ObjectToJson(Object o) throws JsonGenerationException,
			JsonMappingException, IOException {
		// 1. Convert Java object to JSON format
		return new ObjectMapper()
				.writer()
				.withDefaultPrettyPrinter()
		 		.writeValueAsString(o);
	}
	
	/**
	 * Reading property file from resources
	 * 
	 * @param file String specifying the file name
	 * @return Property object
	 * @throws IOException
	 */
	public static Properties readPropertyFile(String file) throws IOException {
		InputStream in = Utils.class.getClassLoader().getResourceAsStream(file);
		if (in == null){
			return null;
		}
		Properties prop = new Properties();
		prop.load(in);
		return prop;
	}
}
