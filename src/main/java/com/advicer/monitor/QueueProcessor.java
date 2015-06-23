package com.advicer.monitor;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @author Iulian Balan
 *
 */
public class QueueProcessor implements Runnable {

	private static final Logger log = LoggerFactory
			.getLogger(QueueProcessor.class);

	private static final String WAITING_FILES_PROCESSOR = "Waiting for new test messages to be processed!";
	
	private static final String EXCHANGE_NAME = "amq.topic";
	private static final String ROUTING_KEY = "monitoring";
	

	private BlockingQueue<MessagePojo> testQueue;

	public QueueProcessor(BlockingQueue<MessagePojo> queue) {
		this.testQueue = queue;

	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {

		while (true) {
			MessagePojo msg = null;
			try {
				if (testQueue.isEmpty()) {
					log.info(WAITING_FILES_PROCESSOR);
				}
				msg = testQueue.take();
			} catch (InterruptedException e) {
				log.error("Error!", e);
			}
			try {
				process(msg);
			} catch (IOException e) {
				//dont need to break flow, just log the error and move on
				log.warn("Unexpected error: ", e);
			} catch (TimeoutException e) {
				//dont need to break flow, just log the error and move on
				log.warn("Unexpected error: ", e);
			}
		}
	}

	private void process(MessagePojo msg) throws TimeoutException, IOException  {
		

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
		
		Channel channel = connection.createChannel();
		channel.exchangeDeclare(EXCHANGE_NAME, "topic", true);
		
		String message = Utils.messagePojoToString(msg);
		channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, null, message.getBytes("UTF-8"));

		channel.close();
		connection.close();
	}
}
