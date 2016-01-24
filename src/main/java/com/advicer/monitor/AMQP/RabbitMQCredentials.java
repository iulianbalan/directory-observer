package com.advicer.monitor.AMQP;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.Properties;

import static com.advicer.monitor.util.DirectoryObserverConstants.HOSTNAME_KEY;
import static com.advicer.monitor.util.DirectoryObserverConstants.PASSWORD_KEY;
import static com.advicer.monitor.util.DirectoryObserverConstants.PORT_KEY;
import static com.advicer.monitor.util.DirectoryObserverConstants.USERNAME_KEY;

/**
 * Simple class used to store credentials and connection
 * parameters for a RabbitMQ connections instance
 *
 * @author Iulian Balan
 */
@Component
public class RabbitMQCredentials {

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
     * @param prop properties containing
     *             {@link com.advicer.monitor.util.DirectoryObserverConstants#HOSTNAME_KEY}
     *             {@link com.advicer.monitor.util.DirectoryObserverConstants#PORT_KEY}
     *             {@link com.advicer.monitor.util.DirectoryObserverConstants#USERNAME_KEY}
     *             {@link com.advicer.monitor.util.DirectoryObserverConstants#PASSWORD_KEY}
     */
    public RabbitMQCredentials(Properties prop) {
        this(prop.getProperty(HOSTNAME_KEY),
                Integer.parseInt(prop.getProperty(PORT_KEY)),
                prop.getProperty(USERNAME_KEY),
                prop.getProperty(PASSWORD_KEY));
    }
}
