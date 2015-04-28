package my.cool.demo.jms.cursor;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.broker.region.policy.FilePendingQueueMessageStoragePolicy;
import org.apache.activemq.broker.region.policy.PolicyEntry;
import org.apache.activemq.broker.region.policy.PolicyMap;
import org.apache.activemq.broker.region.policy.StorePendingQueueMessageStoragePolicy;
import org.apache.activemq.usage.SystemUsage;

import javax.jms.*;
import java.util.Date;
import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

public class StoreCursor {
    
    protected String bindAddress = "tcp://localhost:60706";
    BrokerService broker;
    ActiveMQConnectionFactory factory;
    Connection connection;
    Session session;
    Queue queue;
    int messageSize = 1024;
    int memoryLimit = 10 * messageSize;

    public static void main(String[] args) throws Exception {
        StoreCursor store = new StoreCursor();
        store.setUp();
        store.start();
        
        /* Persistent Messages - QueueStorePrefetch 
        store.configureBroker(store.memoryLimit, store.memoryLimit);
        store.sendMessages(DeliveryMode.PERSISTENT);
        store.tearDown();
        */


        int limit = store.memoryLimit / 2;
        store.configureBroker(store.memoryLimit, store.memoryLimit);
        store.sendMessages(DeliveryMode.NON_PERSISTENT);
        
        store.tearDown();
    }

    protected void setUp() throws Exception {
        if (broker == null) {
            broker = new BrokerService();
            broker.setPersistent(false);
            broker.setAdvisorySupport(false);
        }
    }

    protected void tearDown() throws Exception {
        if (broker != null) {
            broker.stop();
            broker = null;
        }
    }

    protected void start() throws Exception {
        broker.start();
        factory = new ActiveMQConnectionFactory("vm://localhost?jms.alwaysSyncSend=true");
        factory.setWatchTopicAdvisories(false);
        connection = factory.createConnection();
        connection.start();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        queue = session.createQueue("QUEUE." + this.getClass().getName());
    }

    protected void stop() throws Exception {
        session.close();
        connection.close();
        broker.stop();
        broker = null;
    }

    protected void configureBroker(long memoryLimit, long systemLimit) throws Exception {
        broker.setDeleteAllMessagesOnStartup(true);
        broker.addConnector(bindAddress);
        broker.setPersistent(true);

        SystemUsage systemUsage = broker.getSystemUsage();
        systemUsage.setSendFailIfNoSpace(true);
        systemUsage.getMemoryUsage().setLimit(systemLimit);

        PolicyEntry policy = new PolicyEntry();
        policy.setProducerFlowControl(false);
        policy.setPendingQueuePolicy(new StorePendingQueueMessageStoragePolicy());
        /*policy.setPendingQueuePolicy(new FilePendingQueueMessageStoragePolicy());*/
        /*policy.setUseCache(true);*/
        PolicyMap pMap = new PolicyMap();
        pMap.setDefaultEntry(policy);
        broker.setDestinationPolicy(pMap);
    }

    protected String createMessageText(int index) {
        StringBuffer buffer = new StringBuffer(messageSize);
        buffer.append("Message: " + index + " sent at: " + new Date());
        if (buffer.length() > messageSize) {
            return buffer.substring(0, messageSize);
        }
        for (int i = buffer.length(); i < messageSize; i++) {
            buffer.append(' ');
        }
        return buffer.toString();
    }

    protected void sendMessages(int deliveryMode) throws Exception {
        start();
        MessageProducer producer = session.createProducer(queue);
        producer.setDeliveryMode(deliveryMode);
        int i =0;
        try {
            for (i = 0; i < 5; i++) {
                TextMessage message = session.createTextMessage(createMessageText(i));
                producer.send(message);
            }
        } catch (javax.jms.ResourceAllocationException e) {
            e.printStackTrace();
        }
    }
}