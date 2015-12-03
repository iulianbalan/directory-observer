package com.advicer.monitor.AMQP;

import java.util.Properties;

/**
 * Simple class used to store credentials and connection
 * parameters for a RabbitMQ connections instance
 * 
 * @author Iulian Balan
 *
 */
public class RabbitMQCredentials {
	
	private static final String HOSTNAME_KEY = "host";
	private static final String PORT_KEY = "port";
	private static final String USERNAME_KEY = "username";
	private static final String PASSWORD_KEY = "password";
	private String hostname;
	private int port;
	private String username;
	private String password;
	
	public RabbitMQCredentials(String hostname, int port, String username) {
		this.hostname = hostname;
		this.port = port;
		this.username = username;
	}
	
	/**
	 * Constructor that gets the fields from a Properties object
	 * @param prop
	 */
	public RabbitMQCredentials(Properties prop) {
		this.hostname = prop.getProperty(HOSTNAME_KEY);
		this.port = Integer.parseInt(prop.getProperty(PORT_KEY));
		this.username = prop.getProperty(USERNAME_KEY);
		this.password = prop.getProperty(PASSWORD_KEY);
	}


	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
