package com.advicer.monitor.queue;

import com.advicer.monitor.dto.Message;
import lombok.extern.slf4j.Slf4j;

import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.BlockingQueue;

/**
 * Class implementing the Observer interface intended
 * to listen to some monitoring tool and put a message
 * into a BlockingQueue
 *
 * @author Iulian Balan
 */
@Slf4j
public class QueueHandler implements Observer {

    private static final String MESSAGE_POSTED = "Message posted in the queue";

    private BlockingQueue<Message> testQueue;

    /**
     * Main constructor
     *
     * @param queue BlockingQueue already initialized
     */
    public QueueHandler(BlockingQueue<Message> queue) {
        this.testQueue = queue;
    }

    /* (non-Javadoc)
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    public void update(Observable o, Object arg) {
        if (arg instanceof Message) {
            Message msg = (Message) arg;
            try {
                testQueue.put(msg);
                log.info(MESSAGE_POSTED);
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
            }
        }
    }
}
