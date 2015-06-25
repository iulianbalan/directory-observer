package com.advicer.monitor;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
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

	private static final String CONNECTED = "Connected to RabbitMQ server at {}:{}";

	private static final String EXCHANGE_TYPE = "topic";

	private static final Logger log = LoggerFactory.getLogger(QueueProcessor.class);

	private static final String WAITING_FILES_PROCESSOR = "Waiting for new test messages to be processed!";
	
	private static final String EXCHANGE_NAME = "amq.topic";
	private static final String ROUTING_KEY = "monitoring";
	

	private BlockingQueue<MessagePojo> testQueue;
	private Connection connection;
	private Channel channel;

	/**
	 * Main constructor
	 * 
	 * @param queue provide a BlockingQueue implementation
	 */
	public QueueProcessor(BlockingQueue<MessagePojo> queue) {
		this.testQueue = queue;
	}
	
	/**
	 * Create a RabbitMQ connection using default credentials
	 * 
	 * @param credentials
	 * @throws IOException when there's trouble establishing a new connection
	 * @throws TimeoutException when there's trouble establishing a new connection
	 */
	public void connectToRabbitMq() throws IOException, TimeoutException {
		ConnectionFactory factory = new ConnectionFactory();
		this.connection = factory.newConnection();
		log.info(CONNECTED, factory.getHost(), factory.getPort());
	}
	
	/**
	 * Create a RabbitMQ connection using custom credentials
	 * 
	 * @param credentials
	 * @throws IOException when there's trouble establishing a new connection
	 * @throws TimeoutException when there's trouble establishing a new connection
	 */
	public void connectToRabbitMq(RabbitMQCredentials credentials) throws IOException, TimeoutException {
		ConnectionFactory factory = new ConnectionFactory();
		if (credentials.getHostname() != null) {
			factory.setHost(credentials.getHostname());
		}
		if (credentials.getPort() > 0) {
			factory.setPort(credentials.getPort());
		}
		if (credentials.getUsername() != null) {
			factory.setUsername(credentials.getUsername());
		}
		if (credentials.getPassword() != null) {
			factory.setPassword(credentials.getPassword());
		}
		this.connection = factory.newConnection();
		log.info(CONNECTED, factory.getHost(), factory.getPort());
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

				try {
					channel.close();
					connection.close();
				} catch (IOException | TimeoutException e1) {
					log.error(e1.getMessage(), e1);
					Thread.currentThread().interrupt();
				}finally {
					log.error(e.getMessage(), e);
				}
			}
			try {
				process(msg);
			} catch (IOException e) {
				//dont need to break flow, just log the error and move on
				log.warn(e.getMessage(), e);
			} 
		}
	}

	private void process(MessagePojo msg) throws IOException  {

		Channel channel = this.connection.createChannel();
		channel.exchangeDeclare(EXCHANGE_NAME, EXCHANGE_TYPE, true);
		
		String message = Utils.ObjectToJson(msg);
		channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, null, message.getBytes(StandardCharsets.UTF_8));

	}
}
