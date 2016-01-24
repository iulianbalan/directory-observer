package com.advicer.monitor.AMQP;

import com.advicer.monitor.dto.Message;
import com.advicer.monitor.exceptions.ApplicationException;
import com.advicer.monitor.util.Utils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @author Iulian Balan
 */
@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(SpringJUnit4ClassRunner.class)
@PrepareForTest(Utils.class)
@ContextConfiguration("/spring-context.xml")
public class RabbitMqServiceTest {

    @Value(value = "${rabbitmq.exchangeType}")
    private String exchangeType;
    @Value(value = "${rabbitmq.exchangeName}")
    private String exchangeName;
    @Value(value = "${rabbitmq.routingKey}")
    private String routingKey;

    @Resource
    private RabbitMqService rabbitMqService;

    @Mock
    private ConnectionFactory connectionFactory;

    @Mock
    private Connection connection;

    @Mock
    private Channel channel;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(Utils.class);
        rabbitMqService.setConnectionFactory(connectionFactory);
    }

    @Test(expected = ApplicationException.class)
    public void shouldThrowApplicationExceptionWhenTimeOut() throws Exception {
        doThrow(TimeoutException.class).when(connectionFactory).newConnection();
        rabbitMqService.connect();
    }

    @Test(expected = ApplicationException.class)
    public void shouldThrowApplicationExceptionWhenIOException() throws Exception {
        doThrow(IOException.class).when(connectionFactory).newConnection();
        rabbitMqService.connect();
    }

    @Test()
    public void shouldCreateChannelOnConnecting() throws Exception {
        //given
        mockChannelCreation();

        //when
        rabbitMqService.connect();

        //then
        verify(connection, times(1)).createChannel();
    }

    @Test()
    public void shouldDeclareExchangeOnConnecting() throws Exception {
        //given
        mockChannelCreation();

        //when
        rabbitMqService.connect();

        //then
        verify(channel, times(1)).exchangeDeclare(exchangeName, exchangeType, true);
    }

    @Test()
    public void shouldPublishJson() throws Exception {
        //given
        String jsonMessage = "lol json fake";
        mockConnection();
        PowerMockito.when(Utils.ObjectToJson(anyObject())).thenReturn(jsonMessage);


        //when
        rabbitMqService.publishMessage(new Message());

        //then
        verify(channel, times(1)).basicPublish(exchangeName, routingKey, null, jsonMessage.getBytes(StandardCharsets.UTF_8));
    }

    @Test(expected = ApplicationException.class)
    public void shouldThrowApplicationExceptionWhenIOExceptionInPublish() throws Exception {
        mockConnection();
        doThrow(IOException.class).when(channel).basicPublish(anyString(), anyString(), any(AMQP.BasicProperties.class), any(byte[].class));
        PowerMockito.when(Utils.ObjectToJson(anyObject())).thenReturn("fake");

        rabbitMqService.publishMessage(new Message());
    }

    @Test(expected = ApplicationException.class)
    public void shouldThrowApplicationExceptionWhenIOExceptionInClose() throws Exception {
        //given
        mockConnection();
        doThrow(IOException.class).when(channel).close();

        rabbitMqService.closeConnection();
    }

    @Test(expected = ApplicationException.class)
    public void shouldThrowApplicationExceptionWhenTimeoutExceptionInClose() throws Exception {
        //given
        mockConnection();
        doThrow(TimeoutException.class).when(channel).close();

        rabbitMqService.closeConnection();
    }

    private void mockConnection() throws IOException, TimeoutException {
        mockChannelCreation();
        rabbitMqService.connect();
    }

    private void mockChannelCreation() throws IOException, TimeoutException {
        doReturn(connection).when(connectionFactory).newConnection();
        doReturn(channel).when(connection).createChannel();
    }
}