package com.advicer.monitor;

import com.advicer.monitor.dto.Message;
import com.advicer.monitor.util.Utils;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestUtils {

    @Test
    public void testCheckDirectory() {

        File f = mock(File.class);

        when(f.exists()).thenReturn(true);
        when(f.isDirectory()).thenReturn(true);
        when(f.canRead()).thenReturn(true);

        assertTrue(Utils.checkDirectory(f));
    }

    @Test
    public void shouldSerielizeObject() {

        final String action = "Fake action";
        final String fullPath = "Fake full path";
        final String file = "Fake file";

        Message message = new Message();
        message.setAction(action);
        message.setFile(file);

        message.setFullPath(fullPath);

        String responseJson = Utils.ObjectToJson(message);
        String expectedJson = String.format("{\"action\":\"%s\",\"file\":\"%s\",\"fullPath\":\"%s\"}",
                action, file, fullPath);

        assertEquals("Json strings should match", expectedJson, responseJson);


    }

}
