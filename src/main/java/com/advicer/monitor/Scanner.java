package com.advicer.monitor;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class used to monitor a directory on the file system
 * 
 * @author Iulian Balan
 *
 */
public class Scanner extends Observable {

	private static final Logger log = LoggerFactory.getLogger(Scanner.class);

	private WatchService watcher;
	private Path dir;

	
	//Events enabled by default
	private boolean create = true;
	private boolean delete = true;
	private boolean edit = true;
	
	public enum Events {
		CREATION, DELETION, MODIFICATION
	}

	/** Main constructor
	 * @param path String that contains the full path of the directory
	 * @throws IOException If an I/O error occurs
	 */
	public Scanner(String path) throws IOException {
		this.dir = Paths.get(path);
		this.watcher = FileSystems.getDefault().newWatchService();
	}
	
	private Kind<Path> getEventMapped (Events event) {
		switch (event) {
		//no need for break since I'm returning
		case CREATION: return StandardWatchEventKinds.ENTRY_CREATE;
		case DELETION: return StandardWatchEventKinds.ENTRY_DELETE;
		case MODIFICATION: return StandardWatchEventKinds.ENTRY_MODIFY;
		//Default case should NEVER happen!
		default: return null;
		}
	}

	/**
	 * Function used to exclude some events from being monitored
	 * 
	 * @param event intended to exclude
	 * @param exclude true if the event should be excluded
	 * @return the instance of this object that permits chaining
	 */
	public Scanner excludeEventFromListening(Events event, boolean exclude) {
		if (!exclude)
			return this;
		
		switch (event) {
		case CREATION: 
			this.create = false;
			break; //avoid falling on next case
		case DELETION: 
			this.delete = false;
			break; //avoid falling on next case
		case MODIFICATION: 
			this.edit = false;
		}
		return this;
	}

	private void monitor() throws IOException {

		List<WatchEvent.Kind<?>> kindEvents = new ArrayList<WatchEvent.Kind<?>>();
		addEventListener(kindEvents, Events.CREATION, this.create);
		addEventListener(kindEvents, Events.DELETION, this.delete);
		addEventListener(kindEvents, Events.MODIFICATION, this.edit);

		WatchEvent.Kind<?>[] array = kindEvents.toArray(new WatchEvent.Kind<?>[kindEvents.size()]);
		this.dir.register(watcher,(Kind[]) array);

	}
	
	private void addEventListener(List<WatchEvent.Kind<?>> kindEvents, Events event, boolean listen){
		if (listen)
			kindEvents.add(getEventMapped(event));
	}

	/**
	 * Procedure that start the actual monitoring 
	 * 
	 * @throws IOException If an I/O error occurs
	 */
	public void startMonitoring() throws IOException {
		
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

			for (WatchEvent<?> event : key.pollEvents()) {
				
				WatchEvent.Kind<?> kind = event.kind();
				MessagePojo msg = createMessage(kind, (Path) event.context());
				
				if (msg == null) continue;
				
				setChanged();
				notifyObservers(msg);
			}
			if (!key.reset()) {
				break;
			}
		}
	}
	
	private MessagePojo createMessage(WatchEvent.Kind<?> kind, Path newFile) {
		MessagePojo msg = new MessagePojo();
		
		if (kind == StandardWatchEventKinds.OVERFLOW) 
			return null;
		log.info(kind.toString());
		msg.setAction(kind.toString());				
		msg.setFullPath(dir.toAbsolutePath().toString());
		
		msg.setFile(newFile.toString());
		return msg;
	}
}
