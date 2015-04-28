package my.cool;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import javax.jms.*;

import junit.framework.TestCase;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQSession;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.broker.region.policy.PolicyEntry;
import org.apache.activemq.broker.region.policy.PolicyMap;
import org.apache.activemq.command.ActiveMQQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PriorityAndPrefetch extends TestCase {

    private static final Logger log = LoggerFactory.getLogger(ConsumerThread.class);

    private BrokerService brokerService;

    private final String ACTIVEMQ_BROKER_BIND = "tcp://localhost:0";
    private String ACTIVEMQ_BROKER_URI;

    private ActiveMQConnectionFactory connectionFactory;
    private ActiveMQQueue destination;
    private Connection connection;
    private Session session;
    private final Random pause = new Random();

    @Override
    protected void setUp() throws Exception {
        // Start an embedded broker up.
        brokerService = new BrokerService();
        brokerService.setPersistent(false);
        brokerService.setUseJmx(false);
        brokerService.setDeleteAllMessagesOnStartup(true);

/*        PolicyMap policyMap = new PolicyMap();
        PolicyEntry pe = new PolicyEntry();
        pe.setQueuePrefetch(5);
        policyMap.put(new ActiveMQQueue(">"), pe);
        brokerService.setDestinationPolicy(policyMap);*/

        brokerService.addConnector(ACTIVEMQ_BROKER_BIND);
        brokerService.start();
        brokerService.waitUntilStarted();

        ACTIVEMQ_BROKER_URI = brokerService.getTransportConnectors().get(0).getPublishableConnectString();
        connectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_BROKER_URI);
    }

    @Override
    protected void tearDown() throws Exception {
        connection.close();

        brokerService.stop();
        brokerService.waitUntilStarted();
    }

    public void testTwoConsumersWithPriority1and2() throws Exception {

        int NUM_MESSAGES = 10;

        connection = connectionFactory.createConnection();
        connection.start();
        session = connection.createSession(false, ActiveMQSession.INDIVIDUAL_ACKNOWLEDGE);

        Queue queue1 = new ActiveMQQueue(getName() + "?consumer.priority=1");
        Queue queue2 = new ActiveMQQueue(getName() + "?consumer.priority=2");
        Queue queue = new ActiveMQQueue(getName());

        MessageConsumer lowPriority = session.createConsumer(queue1);
        MessageConsumer highPriority = session.createConsumer(queue2);

        final MessageProducer producer = session.createProducer(queue);

        ConsumerThread low = new ConsumerThread(lowPriority, 0);
        low.start();

        ConsumerThread high = new ConsumerThread(highPriority, 10);
        high.start();

        ProducerThread p1 = new ProducerThread(producer, NUM_MESSAGES);
        p1.start();
        p1.join();
        
        long resultLow = low.getCounter().addAndGet(0);
        long resultHigh = high.getCounter().addAndGet(0);

        assertEquals(0, resultLow);
        assertEquals(10, resultHigh);
    }

    public void testTwoConsumersWithPriority1and2AndPrefetchSize5() throws Exception {

        int NUM_MESSAGES = 20;

        connection = connectionFactory.createConnection();
        connection.start();
        session = connection.createSession(false, ActiveMQSession.INDIVIDUAL_ACKNOWLEDGE);

        Queue queue1 = new ActiveMQQueue(getName() + "?consumer.priority=1&consumer.prefetchSize=100");
        Queue queue2 = new ActiveMQQueue(getName() + "?consumer.priority=2&consumer.prefetchSize=5");
        Queue queue = new ActiveMQQueue(getName());

        MessageConsumer lowPriority = session.createConsumer(queue1);
        MessageConsumer highPriority = session.createConsumer(queue2);

        final MessageProducer producer = session.createProducer(queue);

        ConsumerThread low = new ConsumerThread(lowPriority, 15);
        low.start();

        ConsumerThread high = new ConsumerThread(highPriority, 5);
        high.start();

        ProducerThread p1 = new ProducerThread(producer, NUM_MESSAGES);
        p1.start();
        p1.join();
        
        low.join();
        high.join();

        long resultLow = low.getCounter().addAndGet(0);
        long resultHigh = high.getCounter().addAndGet(0);

        assertEquals(15, resultLow);
        assertEquals(5, resultHigh);
    }

    public class ProducerThread extends Thread {

        int NUM_MESSAGES;
        MessageProducer producer;

        public ProducerThread(MessageProducer producer, int num_messages) {
            this.NUM_MESSAGES = num_messages;
            this.producer = producer;
        }

        public void run() {
            try {
                for (int i = 0; i < this.NUM_MESSAGES; ++i) {
                    this.producer.send(session.createTextMessage("TEST"));
                    TimeUnit.MILLISECONDS.sleep(pause.nextInt(10));
                }
            } catch (Exception e) {
                log.error("Caught an unexpected error: ", e);
            }
        }
    }

    public class ConsumerThread extends Thread {

        AtomicLong counter = new AtomicLong();
        MessageConsumer consumer;
        int NUM_MESSAGES;

        public ConsumerThread(MessageConsumer consumer, int num_messages) {
            this.consumer = consumer;
            this.NUM_MESSAGES = num_messages;
        }

        @Override
        public void run() {
            try {
                while (counter.get() < NUM_MESSAGES) {
                    Message message = consumer.receive(100);
                    counter.incrementAndGet();
                }
            } catch (Exception e) {
                log.error("Caught an unexpected error: ", e);
            }
        }

        public AtomicLong getCounter() {
            return counter;
        }
    }

}
