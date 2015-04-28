package my.cool;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import javax.jms.Connection;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import junit.framework.TestCase;

import org.apache.activemq.ActiveMQConnectionFactory;
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
    private ArrayList<Thread> threads = new ArrayList<Thread>();

    private final String ACTIVEMQ_BROKER_BIND = "tcp://localhost:0";
    private String ACTIVEMQ_BROKER_URI;
    private String ACTIVEMQ_BROKER_URI_CONSUMER_1;
    private String ACTIVEMQ_BROKER_URI_CONSUMER_2;

    AtomicBoolean shutdown = new AtomicBoolean();
    private ActiveMQQueue destination;

    @Override
    protected void setUp() throws Exception {
        // Start an embedded broker up.
        brokerService = new BrokerService();
        KahaDBStore adaptor = new KahaDBStore();
        brokerService.setPersistenceAdapter(adaptor);
        brokerService.deleteAllMessages();

        // A small max page size makes this issue occur faster.
        PolicyMap policyMap = new PolicyMap();
        PolicyEntry pe = new PolicyEntry();
        pe.setQueuePrefetch(1000);
        policyMap.put(new ActiveMQQueue(">"), pe);
        brokerService.setDestinationPolicy(policyMap);

        brokerService.addConnector(ACTIVEMQ_BROKER_BIND);
        brokerService.start();

        ACTIVEMQ_BROKER_URI = brokerService.getTransportConnectors().get(0).getPublishableConnectString();
        destination = new ActiveMQQueue(getName());
    }

    @Override
    protected void tearDown() throws Exception {
        // Stop any running threads.
        shutdown.set(true);
        for (Thread t : threads) {
            t.interrupt();
            t.join();
        }
        brokerService.stop();
    }

    public void testTwoConsumersWithPriority1and2() throws Exception {
        ACTIVEMQ_BROKER_URI_CONSUMER_1 = ACTIVEMQ_BROKER_URI + "?consumer.priority=1";
        ACTIVEMQ_BROKER_URI_CONSUMER_2 = ACTIVEMQ_BROKER_URI + "?consumer.priority=2";
        doTestConsumers();
    }

/*        public void testTwoConsumersWithPriority1and2AndPrefetch() throws Exception {
            doTestConsumers();
        }*/

    public void doTestConsumers() throws Exception {

        ConsumerThread c1 = new ConsumerThread(ACTIVEMQ_BROKER_URI_CONSUMER_1, "Consumer-1");
        threads.add(c1);
        c1.start();

        ConsumerThread c2 = new ConsumerThread(ACTIVEMQ_BROKER_URI_CONSUMER_2, "Consumer-2");
        threads.add(c2);
        c2.start();

        ProducerThread p = new ProducerThread(50);
        threads.add(p);
        p.start();

        long c1Counter = c1.counter.getAndSet(0);
        long c2Counter = c2.counter.getAndSet(0);
        log.debug("c1: " + c1Counter + ", c2: " + c2Counter);

        //assertTrue("Total received=" + totalReceived + ", Consumer 2 should be receiving new messages every second.", c2Counter > 0);

    }

    public class ProducerThread extends Thread {
        
        int NUM_MESSAGES;
        
        public ProducerThread(int num_messages) {
            this.NUM_MESSAGES = num_messages;
        }

        public void run() {
            try {
                produce(this.NUM_MESSAGES);
            } catch(Exception e) {
                log.error("Caught an unexpected error: ", e);
            }
        }
    }

    public void produce(int count) throws Exception {
        Connection connection = null;
        try {
            ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(ACTIVEMQ_BROKER_URI);
            factory.setDispatchAsync(true);

            connection = factory.createConnection();

            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageProducer producer = session.createProducer(destination);
            connection.start();

            for (int i = 0; i < count; i++) {
                producer.send(session.createTextMessage(getName() + " Message " + (++i)));
            }

        } finally {
            try {
                connection.close();
            } catch (Throwable e) {
            }
        }
    }

    public class ConsumerThread extends Thread {
        final AtomicLong counter = new AtomicLong();
        String URI;

        public ConsumerThread(String URI, String threadId) {
            super(threadId);
            this.URI = URI;
        }

        public void run() {
            Connection connection = null;
            try {
                log.debug(getName() + ": is running");

                ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(this.URI);
                factory.setDispatchAsync(true);

                connection = factory.createConnection();

                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
                MessageConsumer consumer = session.createConsumer(destination);
                connection.start();

                while (!shutdown.get()) {
                    TextMessage msg = (TextMessage) consumer.receive(1000);
                        if ( msg!=null ) {
                            counter.incrementAndGet();
                        }
                }

            } catch (Exception e) {
            } finally {
                log.debug(getName() + ": is stopping");
                try {
                    connection.close();
                } catch (Throwable e) {
                }
            }
        }

    }

}
