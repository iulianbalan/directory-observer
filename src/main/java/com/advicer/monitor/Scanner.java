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

public class Scanner extends Observable {

	private static final Logger log = LoggerFactory.getLogger(Scanner.class);

	private WatchService watcher;
	private Path dir;

	private boolean create;
	private boolean delete;
	private boolean edit;

	public Scanner() throws IOException {

		this.watcher = FileSystems.getDefault().newWatchService();

	}

	public void excludeCreate(boolean hasOption) {
		this.create = hasOption;
	}

	public void excludeDelete(boolean hasOption) {
		this.delete = hasOption;
	}

	public void excludeEdit(boolean hasOption) {
		this.edit = hasOption;
	}

	public void monitor(String path) {

		this.dir = Paths.get(path);
		try {
			List<WatchEvent.Kind<?>> kindEvents = new ArrayList<WatchEvent.Kind<?>>();
			if (create)
				kindEvents.add(StandardWatchEventKinds.ENTRY_CREATE);
			if (delete)
				kindEvents.add(StandardWatchEventKinds.ENTRY_DELETE);
			if (edit)
				kindEvents.add(StandardWatchEventKinds.ENTRY_MODIFY);
			
			WatchEvent.Kind<?>[] array = kindEvents.toArray(new WatchEvent.Kind<?>[kindEvents.size()]);
			dir.register(watcher,(Kind[]) array);
		} catch (IOException e) {

			log.debug(e.getMessage());
			e.printStackTrace();
		}
	}

	public void run() {

		while (true) {
			// wait for key to be signaled
			WatchKey key;
			try {

				key = this.watcher.take();
			} catch (InterruptedException e) {
				log.error(e.getMessage(), e);
				return;
			}

			for (WatchEvent<?> event : key.pollEvents()) {
				WatchEvent.Kind<?> kind = event.kind();
				
				MessagePojo msg = new MessagePojo();
				msg.setFullPath(dir.toAbsolutePath().toString());

				if (create && kind == StandardWatchEventKinds.ENTRY_CREATE) {
//					log.info("Creating a new file");
					msg.setAction("created");
				} else if (delete && kind == StandardWatchEventKinds.ENTRY_DELETE) {
//					log.info("Deleting a file");
					msg.setAction("deleted");
				} else if (edit && kind == StandardWatchEventKinds.ENTRY_MODIFY) {
//					log.info("Modifying a file");
					msg.setAction("modified");
				} else if (kind == StandardWatchEventKinds.OVERFLOW) {
//					log.info("Overflow occured");
					continue;
				} else {
//					nothing to notify
					continue;
				}

				// The filename is the context of the event.
				@SuppressWarnings("unchecked")
				WatchEvent<Path> ev = (WatchEvent<Path>) event;
				Path filename = ev.context();

				
				msg.setFile(filename.toString());
				setChanged();
				notifyObservers(msg);
			}

			if (!key.reset()) {
				break;
			}
		}
	}

}
