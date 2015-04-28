package my.cool;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import javax.jms.*;

import junit.framework.TestCase;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQSession;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.broker.region.policy.PolicyEntry;
import org.apache.activemq.broker.region.policy.PolicyMap;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.store.kahadb.KahaDBStore;
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

    private final AtomicLong totalConsumed = new AtomicLong();

    @Override
    protected void setUp() throws Exception {
        // Start an embedded broker up.
        brokerService = new BrokerService();
        brokerService.setPersistent(false);
        brokerService.setUseJmx(false);
        brokerService.setDeleteAllMessagesOnStartup(true);

        // A small max page size makes this issue occur faster.
        PolicyMap policyMap = new PolicyMap();
        PolicyEntry pe = new PolicyEntry();
        pe.setQueuePrefetch(1000);
        policyMap.put(new ActiveMQQueue(">"), pe);
        brokerService.setDestinationPolicy(policyMap);

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
/*        ACTIVEMQ_BROKER_URI_CONSUMER_1 = ACTIVEMQ_BROKER_URI + "?consumer.priority=1";
        ACTIVEMQ_BROKER_URI_CONSUMER_2 = ACTIVEMQ_BROKER_URI + "?consumer.priority=2";*/
        doTestConsumers();
    }

    public void doTestConsumers() throws Exception {

        connection = connectionFactory.createConnection();
        connection.start();
        session = connection.createSession(false, ActiveMQSession.INDIVIDUAL_ACKNOWLEDGE);

        final int NUM_MESSAGES = 50;

        log.info("Consumer queue 1 :" + getName() + "?consumer.priority=1");
        log.info("Consumer queue 2 :" + getName() + "?consumer.priority=2");

        Queue queue1 = new ActiveMQQueue(getName() + "?consumer.priority=1");
        Queue queue2 = new ActiveMQQueue(getName() + "?consumer.priority=2");
        Queue queue = new ActiveMQQueue(getName());

        MessageConsumer consumer1 = session.createConsumer(queue1);
        MessageConsumer consumer2 = session.createConsumer(queue2);
        
        final MessageProducer producer = session.createProducer(queue);

        ConsumerThread c1 = new ConsumerThread(consumer1, NUM_MESSAGES);
        c1.start();

        ConsumerThread c2 = new ConsumerThread(consumer2, NUM_MESSAGES);
        c2.start();

        /* DON't WORK as counter of c1, c2 = 0
           ProducerThread p1 = new ProducerThread(producer, NUM_MESSAGES);
           p1.start(); 
         */
        
        Thread producerThread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    for (int i = 0; i < NUM_MESSAGES; ++i) {
                        producer.send(session.createTextMessage("TEST"));
                        TimeUnit.MILLISECONDS.sleep(pause.nextInt(10));
                    }
                } catch (Exception e) {
                    log.error("Caught an unexpected error: ", e);
                }
            }
        });
        producerThread.start();
        producerThread.join();
        
        long resultc1 = c1.getCounter().addAndGet(0);
        long resultc2 = c2.getCounter().addAndGet(0);

        assertEquals(0, resultc1);
        assertEquals(50, resultc2);
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
                while (totalConsumed.get() < NUM_MESSAGES) {
                    Message message = consumer.receive();
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
