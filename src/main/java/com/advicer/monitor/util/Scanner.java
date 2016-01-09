package com.advicer.monitor.util;

import com.advicer.monitor.dto.Message;
import com.advicer.monitor.exceptions.ApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.HashSet;
import java.util.Observable;
import java.util.Set;

/**
 * Class used to monitor a directory on the file system
 *
 * @author Iulian Balan
 */
public class Scanner extends Observable {

    private static final Logger log = LoggerFactory.getLogger(Scanner.class);

    private WatchService watcher;
    private Path dir;
    private Set<WatchEvent.Kind<Path>> events;

    /**
     * Main constructor
     *
     * @param path String that contains the full path of the directory
     * @throws IOException If an I/O error occurs
     */
    public Scanner(String path) throws IOException {
        this.dir = Paths.get(path);
        this.watcher = FileSystems.getDefault().newWatchService();
        events = new HashSet<>();

        //Add all the events and exclude them on demand
        events.add(StandardWatchEventKinds.ENTRY_CREATE);
        events.add(StandardWatchEventKinds.ENTRY_DELETE);
        events.add(StandardWatchEventKinds.ENTRY_MODIFY);

    }

    /**
     * Function used to exclude some events from being monitored
     *
     * @param event   java.nio.file.StandardWatchEventKinds intended to exclude
     * @param exclude true if the event should be excluded
     * @return the instance of this object that permits chaining
     */
    public Scanner excludeEventFromListening(WatchEvent.Kind<Path> event, boolean exclude) {
        if (!exclude)
            return this;
        events.remove(event);
        return this;
    }

    private void monitor() {
        WatchEvent.Kind<?>[] array = events.toArray(new WatchEvent.Kind<?>[events.size()]);
        try {
            this.dir.register(watcher, (Kind[]) array);
        } catch (IOException e) {
            throw new ApplicationException("Error starting the monitor on the directory", e);
        }
    }


    /**
     * Procedure that start the actual monitoring
     */
    public void startMonitoring() {

        //register directory for monitoring
        monitor();

        while (true) {
            // wait for key to be signaled
            WatchKey key;
            try {
                key = this.watcher.take();
            } catch (InterruptedException e) {
                //Don't know what to do with this exception
                log.error(e.getMessage(), e);
                return;
            }
            key.pollEvents().forEach(this::handleSingleEvent);
            if (!key.reset()) {
                break;
            }
        }
    }

    private void handleSingleEvent(WatchEvent<?> event) {
        WatchEvent.Kind<?> kind = event.kind();
        Message msg = createMessage(kind, (Path) event.context());
        if (msg == null) {
            return;
        }
        setChanged();
        notifyObservers(msg);
    }

    private Message createMessage(WatchEvent.Kind<?> kind, Path newFile) {
        Message msg = new Message();

        if (kind == StandardWatchEventKinds.OVERFLOW)
            return null;
        log.info(kind.toString());
        msg.setAction(kind.toString());
        msg.setFullPath(dir.toAbsolutePath().toString());

        msg.setFile(newFile.toString());
        return msg;
    }
}
