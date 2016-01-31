package com.advicer.monitor.util;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.WatchService;

/**
 * @author Iulian Balan
 */
@PrepareForTest(FileSystems.class)
public class ScannerTest {

    @Before
    public void setup() throws IOException {

        FileSystem fsMock = Mockito.mock(FileSystem.class);
        WatchService watchServiceMock = Mockito.mock(WatchService.class);

        PowerMockito.mockStatic(FileSystems.class);
        PowerMockito.doReturn(fsMock).when(FileSystems.getDefault());
        Mockito.doReturn(watchServiceMock).when(fsMock).newWatchService();
    }

    @Test
    public void shouldCreateNewInstanceWithFakePath() throws IOException {
        Scanner scanner = new Scanner("Fake path");
    }

}