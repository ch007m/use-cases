package my.cool;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import javax.jms.*;

import junit.framework.TestCase;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQSession;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.command.ActiveMQQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PriorityAndPrefetch extends TestCase {

    private static final Logger log = LoggerFactory.getLogger(ConsumerThread.class);

    private BrokerService brokerService;

    private final String ACTIVEMQ_BROKER_BIND = "tcp://localhost:0";
    private String ACTIVEMQ_BROKER_URI;

    private ActiveMQConnectionFactory connectionFactory;

    @Override
    protected void setUp() throws Exception {
        // Start an embedded broker up.
        brokerService = new BrokerService();
        brokerService.setPersistent(false);
        brokerService.setUseJmx(false);
        brokerService.setDeleteAllMessagesOnStartup(true);

        brokerService.addConnector(ACTIVEMQ_BROKER_BIND);
        brokerService.start();
        brokerService.waitUntilStarted();

        ACTIVEMQ_BROKER_URI = brokerService.getTransportConnectors().get(0).getPublishableConnectString();
        connectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_BROKER_URI);
    }

    @Override
    protected void tearDown() throws Exception {
        brokerService.waitUntilStarted();
    }

    public void testTwoConsumersWithPriority1and2() throws Exception {

        int NUM_MESSAGES = 10;
        int EXPECTED_NUM_CONSUMER_HIGH= 10;

        Queue queueHigh = new ActiveMQQueue(getName() + "?consumer.priority=2");
        Queue queue = new ActiveMQQueue(getName());

        ConsumerThread high = new ConsumerThread(EXPECTED_NUM_CONSUMER_HIGH, queueHigh);
        high.start();

        ProducerThread p1 = new ProducerThread(NUM_MESSAGES, queue);
        p1.start();
        p1.join();

        Thread.sleep(500);

        long resultHigh = high.getCounter().addAndGet(0);

        assertEquals(EXPECTED_NUM_CONSUMER_HIGH, resultHigh);
    }

    public void testTwoConsumersWithPriority1and2AndPrefetchSize5() throws Exception {

        int NUM_MESSAGES = 20;
        int EXPECTED_NUM_CONSUMER_LOW = 15;
        int EXPECTED_NUM_CONSUMER_HIGH= 5;

        Queue queueLow  = new ActiveMQQueue(getName() + "?consumer.priority=1&consumer.prefetchSize=100");
        Queue queueHigh = new ActiveMQQueue(getName() + "?consumer.priority=2&consumer.prefetchSize=5");
        Queue queue = new ActiveMQQueue(getName());

        ConsumerThread low = new ConsumerThread(EXPECTED_NUM_CONSUMER_LOW, queueLow);
        low.start();

        ConsumerThread high = new ConsumerThread(EXPECTED_NUM_CONSUMER_HIGH, queueHigh);
        high.start();
        
        low.join();
        high.join();

        ProducerThread p1 = new ProducerThread(NUM_MESSAGES, queue);
        p1.start();
        p1.join();

        Thread.sleep(500);

        long resultLow = low.getCounter().addAndGet(0);
        long resultHigh = high.getCounter().addAndGet(0);

        assertEquals(EXPECTED_NUM_CONSUMER_LOW, resultLow);
        assertEquals(EXPECTED_NUM_CONSUMER_HIGH, resultHigh);
    }

    public class ProducerThread extends Thread {

        int NUM_MESSAGES;
        Destination destination;
        Connection connection;

        public ProducerThread(int num_messages, Destination destination) {
            this.NUM_MESSAGES = num_messages;
            this.destination = destination;
        }

        public void run() {
            try {
                connection = connectionFactory.createConnection();
                connection.start();
                Session session = connection.createSession(false, ActiveMQSession.INDIVIDUAL_ACKNOWLEDGE);
                MessageProducer producer = session.createProducer(destination);

                for (int i = 0; i < this.NUM_MESSAGES; ++i) {
                    producer.send(session.createTextMessage("TEST"));
                }

                connection.stop();
                
            } catch (Exception e) {
                log.error("Caught an unexpected error: ", e);
            } finally {
                try {
                    connection.close();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public class ConsumerThread extends Thread {

        AtomicLong counter = new AtomicLong();
        Destination destination;
        Connection connection;
        int NUM_MESSAGES;

        public ConsumerThread(int num_messages, Destination destination) {
            this.NUM_MESSAGES = num_messages;
            this.destination = destination;
        }

        @Override
        public void run() {
            try {
                connection = connectionFactory.createConnection();
                connection.start();
                Session session = connection.createSession(false, ActiveMQSession.INDIVIDUAL_ACKNOWLEDGE);
                MessageConsumer consumer = session.createConsumer(destination);
                
                while (counter.get() < NUM_MESSAGES) {
                    Message message = consumer.receive(100);
                    counter.incrementAndGet();
                }
                
                connection.stop();
            } catch (Exception e) {
                log.error("Caught an unexpected error: ", e);
            } finally {
                try {
                    connection.close();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        }

        public AtomicLong getCounter() {
            return counter;
        }
    }

}
