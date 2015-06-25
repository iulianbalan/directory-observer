package com.advicer.monitor;

import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class implementing the Observer interface intended
 * to listen to some monitoring tool and put a message
 * into a BlockingQueue
 * 
 * @author Iulian Balan
 *
 */
public class QueueHandler implements Observer{
	
	private static final String MESSAGE_POSTED = "Message posted in the queue";

	private static final Logger log = LoggerFactory.getLogger(QueueHandler.class);
	
	private BlockingQueue<MessagePojo> testQueue;
	
	/**
	 * Main constructor
	 * @param queue BlockingQueue already initialized
	 */
	public QueueHandler(BlockingQueue<MessagePojo> queue) {
		this.testQueue = queue;
	}

	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	public void update(Observable o, Object arg) {
		if (arg instanceof MessagePojo) {
			MessagePojo msg = (MessagePojo)arg;
			try {
				testQueue.put(msg);
				log.info(MESSAGE_POSTED);
			} catch (InterruptedException e) {
				log.error(e.getMessage(), e);
			}
		}
	}
}
