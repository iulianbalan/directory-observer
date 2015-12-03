package com.advicer.monitor.AMQP;

import com.advicer.monitor.dto.Message;
import com.advicer.monitor.exceptions.ApplicationException;
import com.advicer.monitor.util.Utils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * Created by Iulian Balan on 28.12.2015.
 */
@Component
@Slf4j
public class RabbitMqService {

    private static final String CONNECTED = "Connected to RabbitMQ server at {}:{}";

    @Value(value = "${rabbitmq.exchangeType}")
    private String exchangeType;
    @Value(value = "${rabbitmq.exchangeName}")
    private String exchangeName;
    @Value(value = "${rabbitmq.routingKey}")
    private String routingKey;

    private Connection connection;
    private Channel channel;
    private ConnectionFactory factory;

    @Autowired
    private RabbitMQCredentials rabbitMQCredentials;

    public RabbitMqService() {
    }

    private void setUpParameters() {
        this.factory = new ConnectionFactory();
        if (rabbitMQCredentials.getHostname() != null) {
            factory.setHost(rabbitMQCredentials.getHostname());
        }
        if (rabbitMQCredentials.getPort() > 0) {
            factory.setPort(rabbitMQCredentials.getPort());
        }
        if (rabbitMQCredentials.getUsername() != null) {
            factory.setUsername(rabbitMQCredentials.getUsername());
        }
        if (rabbitMQCredentials.getPassword() != null) {
            factory.setPassword(rabbitMQCredentials.getPassword());
        }
    }

    /**
     * Create a RabbitMQ connection using custom rabbitMQCredentials
     *
     * @throws IOException      when there's trouble establishing a new connection
     * @throws TimeoutException when there's trouble establishing a new connection
     */
    public void connect() {
        this.setUpParameters();
        try {
            this.connection = factory.newConnection();
            log.info(CONNECTED, factory.getHost(), factory.getPort());

            this.channel = this.connection.createChannel();
            channel.exchangeDeclare(exchangeName, exchangeType, true);
        } catch (IOException | TimeoutException e) {
            throw new ApplicationException("Error connecting to RabbitMQ", e);
        }
    }

    public void publishMessage(Message message) {
        try {
            String messageJson = Utils.ObjectToJson(message);
            channel.basicPublish(exchangeName, routingKey, null, messageJson.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new ApplicationException("Error publishing new message", e);
        }
    }

    public void closeConnection() {
        try {
            channel.close();
            connection.close();
        } catch (IOException | TimeoutException e) {
            throw new ApplicationException("Error closing the connection", e);
        }
    }
}
