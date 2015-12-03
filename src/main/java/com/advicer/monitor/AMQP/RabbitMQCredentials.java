package com.advicer.monitor.AMQP;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * Simple class used to store credentials and connection
 * parameters for a RabbitMQ connections instance
 *
 * @author Iulian Balan
 */
@Component
public class RabbitMQCredentials {

    private static final String HOSTNAME_KEY = "host";
    private static final String PORT_KEY = "port";
    private static final String USERNAME_KEY = "username";
    private static final String PASSWORD_KEY = "password";

    @Getter
    private String hostname;
    @Getter
    private int port;
    @Getter
    private String username;
    @Getter
    private String password;

    public RabbitMQCredentials(String hostname, int port, String username, String password) {
        this.hostname = hostname;
        this.port = port;
        this.username = username;
        this.password = password;
    }

    /**
     * Constructor that gets the fields from a Properties object
     *
     * @param prop
     */
    public RabbitMQCredentials(Properties prop) {
        this(prop.getProperty(HOSTNAME_KEY),
                Integer.parseInt(prop.getProperty(PORT_KEY)),
                prop.getProperty(USERNAME_KEY),
                prop.getProperty(PASSWORD_KEY));
    }
}
