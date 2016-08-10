package com.advicer.monitor.queue;

import com.advicer.monitor.amqp.RabbitMqService;
import com.advicer.monitor.dto.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.BlockingQueue;

/**
 * @author Iulian Balan
 */
@Slf4j
public class QueueProcessor implements Runnable {

    private static final String WAITING_FILES_PROCESSOR = "Waiting for new test messages to be processed!";

    @Autowired
    private RabbitMqService rabbitMqService;

    private BlockingQueue<Message> testQueue;

    /**
     * Main constructor
     *
     * @param queue provide a BlockingQueue implementation
     */
    public QueueProcessor(BlockingQueue<Message> queue) {
        this.testQueue = queue;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Runnable#run()
     */
    public void run() {

        rabbitMqService.connect();

        while (true) {
            Message msg;
            try {
                if (testQueue.isEmpty()) {
                    log.info(WAITING_FILES_PROCESSOR);
                }
                msg = testQueue.take();
                process(msg);
            } catch (InterruptedException e) {
                rabbitMqService.closeConnection();
            }
        }
    }

    private void process(Message message) {
        rabbitMqService.publishMessage(message);
    }
}
