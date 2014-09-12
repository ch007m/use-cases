package my.cool.demo;

import io.hawtjms.jms.JmsConnectionFactory;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import java.net.URI;

public class ConnectionFactoryUtility {

    static String host;
    static int port;
    static boolean failover;

    public static Connection createAmqpConnection(String username, String password, boolean failover) throws Exception {
        if (failover) {
            return createAmqpConnection(getFailoverAmqpConnectionURI(), username, password);
        } else {
            return createAmqpConnection(getBrokerAmqpConnectionURI(), username, password);
        }
    }

    public static Connection createAmqpConnection(boolean failover) throws Exception {
        if (failover) {
            return createAmqpConnection(getFailoverAmqpConnectionURI());
        } else {
            return createAmqpConnection(getBrokerAmqpConnectionURI());
        }
    }

    public static Connection createAmqpConnection(URI brokerURI) throws Exception {
        return createAmqpConnection(brokerURI, null, null);
    }

    public static Connection createAmqpConnection(URI brokerURI, String username, String password) throws Exception {
        ConnectionFactory factory = createAmqpConnectionFactory(brokerURI, username, password);
        return factory.createConnection();
    }

    public static URI getBrokerAmqpConnectionURI() {
        try {
            String uri = "amqp://" + host + ":" + port;
            return new URI(uri);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public static URI getFailoverAmqpConnectionURI() {
        try {
            //String uri = "failover://(amqp://localhost:5672,amqp://localhost:5673)";
            String uri = "failover://(amqp://localhost:61616)";
            return new URI(uri);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public static ConnectionFactory createAmqpConnectionFactory(URI brokerURI, String username, String password) throws Exception {
        JmsConnectionFactory factory = new JmsConnectionFactory(brokerURI);
        factory.setForceAsyncSend(isForceAsyncSends());
        factory.setAlwaysSyncSend(isAlwaysSyncSend());
        factory.setMessagePrioritySupported(isMessagePrioritySupported());
        factory.setSendAcksAsync(isSendAcksAsync());
        if (username != null) {
            factory.setUsername(username);
        }
        if (password != null) {
            factory.setPassword(password);
        }
        return factory;
    }

    protected static boolean isForceAsyncSends() {
        return false;
    }

    protected static boolean isAlwaysSyncSend() {
        return false;
    }

    protected static boolean isMessagePrioritySupported() {
        return true;
    }

    protected static boolean isSendAcksAsync() {
        return false;
    }

}
