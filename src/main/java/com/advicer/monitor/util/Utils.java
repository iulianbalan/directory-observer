package com.advicer.monitor.util;

import com.advicer.monitor.exceptions.ApplicationException;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * Utility class
 *
 * @author Iulian Balan
 */
public class Utils {

    private static ObjectMapper objectMapper;

    /**
     * Utility function meant to control all system's properties necessary to
     * perform a monitoring
     *
     * @param path full path to the monitored folder
     * @return true if everything is all right
     */
    public static boolean checkDirectory(String path) {

        File file = Paths.get(path).toFile();
        return checkDirectory(file);
    }

    /**
     * Utility function meant to control all system's properties necessary to
     * perform a monitoring
     *
     * @param file File representing the monitored folder
     * @return true if everything is all right
     */
    public static boolean checkDirectory(File file) {

        return file.exists() && file.isDirectory() && file.canRead();
    }

    /**
     * Serializing object to JSON
     *
     * @param object any object
     * @return a String serialized in JSON
     * @throws ApplicationException in case of serializing problems
     */
    public static String ObjectToJson(Object object) {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }
        try {
            return objectMapper.writer().writeValueAsString(object);
        } catch (IOException e) {
            throw new ApplicationException("Error serializing the object", e);
        }
    }
}
