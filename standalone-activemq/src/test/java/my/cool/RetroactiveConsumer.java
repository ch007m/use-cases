package my.cool;

import junit.framework.TestCase;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.broker.region.policy.FixedCountSubscriptionRecoveryPolicy;
import org.apache.activemq.broker.region.policy.PolicyEntry;
import org.apache.activemq.broker.region.policy.PolicyMap;
import org.apache.activemq.broker.region.policy.SimpleDispatchPolicy;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.store.PersistenceAdapter;
import org.apache.activemq.store.kahadb.KahaDBPersistenceAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

public class RetroactiveConsumer extends TestCase {

    private static final Logger log = LoggerFactory.getLogger(RetroactiveConsumer.class);

    private BrokerService broker;
    private String ACTIVEMQ_BROKER_URI;

    private final String ACTIVEMQ_BROKER_BIND = "tcp://localhost:0";

    @Override
    protected void setUp() throws Exception {
        createBroker();
    }

    @Override
    protected void tearDown() throws Exception {
        broker.stop();
    }

    protected void restartBroker() throws Exception {
        broker.stop();
        broker.start();
    }

    private void createBroker() throws Exception {

        PolicyEntry policy = new PolicyEntry();
        policy.setDispatchPolicy(new SimpleDispatchPolicy());
        policy.setSubscriptionRecoveryPolicy(new FixedCountSubscriptionRecoveryPolicy());
        PolicyMap pMap = new PolicyMap();
        pMap.setDefaultEntry(policy);
        
        broker = new BrokerService();
        broker.setBrokerName("durable-broker");
        broker.setDeleteAllMessagesOnStartup(true);
        broker.setPersistenceAdapter(createPersistenceAdapter());
        broker.setPersistent(true);
        broker.addConnector(ACTIVEMQ_BROKER_BIND);
        broker.start();
        broker.waitUntilStarted();
    }

    protected PersistenceAdapter createPersistenceAdapter() throws Exception {
        KahaDBPersistenceAdapter adapter = new KahaDBPersistenceAdapter();
        adapter.setConcurrentStoreAndDispatchQueues(false);
        adapter.setConcurrentStoreAndDispatchTopics(false);
        adapter.deleteAllMessages();
        return adapter;
    }

    public void testFixedRecoveryPolicy() throws Exception {

        ACTIVEMQ_BROKER_URI = broker.getTransportConnectors().get(0).getPublishableConnectString();
        Connection connection = new ActiveMQConnectionFactory(ACTIVEMQ_BROKER_URI).createConnection();
        connection.setClientID("cliId1");
        connection.start();

        // Create the durable sub.
        Session session = connection.createSession(false, javax.jms.Session.AUTO_ACKNOWLEDGE);

        // Ensure that consumer will receive messages sent before it was created
        Topic topic = session.createTopic("TestTopic?consumer.retroactive=true");
        TopicSubscriber consumer = session.createDurableSubscriber(topic, "sub1");

        // Produce a message
        MessageProducer producer = session.createProducer(topic);
        producer.setDeliveryMode(DeliveryMode.PERSISTENT);

        // Make sure it works when the durable sub is active.
        producer.send(session.createTextMessage("Msg:1"));

        // Activate the sub.
        consumer = session.createDurableSubscriber(topic, "sub1");
        consumer.close();

        // Send a new message.
        producer.send(session.createTextMessage("Msg:2"));

        // Restart the broker.
        restartBroker();
        
        // Reconnect the consumers
        consumer = session.createDurableSubscriber(topic, "sub1");

        // Try to get the message.
        assertTextMessageEquals("Msg:1", consumer.receive(5000));
        assertTextMessageEquals("Msg:2", consumer.receive(5000));
    }

    private void assertTextMessageEquals(String string, Message message) throws JMSException {
        assertNotNull("Message was null", message);
        assertTrue("Message is not a TextMessage", message instanceof TextMessage);
        assertEquals(string, ((TextMessage) message).getText());
    }
}
