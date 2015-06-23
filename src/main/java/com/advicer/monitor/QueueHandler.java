package com.advicer.monitor;

import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QueueHandler implements Observer{
	
	private static final Logger log = LoggerFactory.getLogger(QueueHandler.class);
	
	private BlockingQueue<MessagePojo> testQueue;
	
	public QueueHandler(BlockingQueue<MessagePojo> queue) {
		this.testQueue = queue;
	}

	public void update(Observable o, Object arg) {
		if (arg instanceof MessagePojo) {
			MessagePojo msg = (MessagePojo)arg;
//			log.debug(String.format("The queue handler was informed that the file '%s' was %s", msg.getFile(), msg.getAction()));
			try {
				testQueue.put(msg);
				log.info("Message posted in the queue");
			} catch (InterruptedException e) {
				log.error(e.getMessage(), e);
			}
		}
	}
}
