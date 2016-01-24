package com.advicer.monitor.AMQP;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Properties;

import static com.advicer.monitor.util.DirectoryObserverConstants.HOSTNAME_KEY;
import static com.advicer.monitor.util.DirectoryObserverConstants.PASSWORD_KEY;
import static com.advicer.monitor.util.DirectoryObserverConstants.PORT_KEY;
import static com.advicer.monitor.util.DirectoryObserverConstants.USERNAME_KEY;

/**
 * @author Iulian Balan
 */
public class RabbitMQCredentialsTest {

    private String username;
    private String password;
    private String host;
    private int port;

    private Properties properties;


    @Before
    public void setUp() throws Exception {
        username = "fake username";
        password = "fake password";
        host = "fake google.ro";
        port = 8888888;

        properties = new Properties();
        properties.setProperty(HOSTNAME_KEY, host);
        properties.setProperty(PORT_KEY, Integer.toString(port));
        properties.setProperty(USERNAME_KEY, username);
        properties.setProperty(PASSWORD_KEY, password);

    }

    @Test
    public void shouldInstantiateUsingSingleFields() {
        RabbitMQCredentials rabbitMQCredentials =
                new RabbitMQCredentials(host, port, username, password);
        assertCredentials(rabbitMQCredentials);
    }

    @Test
    public void shouldInstantiateUsingProperties() {
        RabbitMQCredentials rabbitMQCredentials = new RabbitMQCredentials(properties);
        assertCredentials(rabbitMQCredentials);
    }

    private void assertCredentials(RabbitMQCredentials rabbitMQCredentials) {
        Assert.assertEquals("Hostname should match", host, rabbitMQCredentials.getHostname());
        Assert.assertEquals("Port should match", port, rabbitMQCredentials.getPort());
        Assert.assertEquals("Username should match", username, rabbitMQCredentials.getUsername());
        Assert.assertEquals("Password should match", password, rabbitMQCredentials.getPassword());
    }


}