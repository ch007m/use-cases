package my.cool.demo;

import io.hawtjms.jms.JmsConnection;
import io.hawtjms.jms.JmsConnectionListener;
import io.hawtjms.jms.message.JmsInboundMessageDispatch;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;

import javax.jms.*;

public class AmqpProducer extends ConnectionFactoryUtility {

    static String user;
    static String password;

    public static void main(String []args) throws Exception {

        /*      user = env("ACTIVEMQ_USER", "admin");
        password = env("ACTIVEMQ_PASSWORD", "password");
        host = env("ACTIVEMQ_HOST", "localhost");
        port = Integer.parseInt(env("ACTIVEMQ_PORT", "5672"))
        String destination = arg(args, 0, "topic://event");
        long sleepTime = Integer.parseInt(arg(args, 1, "0"));
        int messages = Integer.parseInt(arg(args, 2, "10000"));
        */

        user = "admin";
        password = "admin";
        host = "localhost";
        //port = 5672;
        port = 52191;
        failover = true;
        String destination = "DEMO";
        long sleepTime = 0;
        int messages = 25000;
        int size = 256;

        String DATA = "abcdefghijklmnopqrstuvwxyz";
        String body = "";
        for (int i = 0; i < size; i++) {
            body += DATA.charAt(i % DATA.length());
        }


        Destination dest = null;
        if (destination.startsWith("topic://")) {
            System.out.print("Topic : " + destination);
            dest = new ActiveMQTopic(destination);
        } else {
            System.out.print("Queue : " + destination);
            dest = new ActiveMQQueue(destination);
        }

        final JmsConnection connection = (JmsConnection) createAmqpConnection(user, password, failover);
        //final JmsConnection connection = (JmsConnection) createAmqpConnection(failover);
        connection.addConnectionListener(new JmsConnectionListener() {

            @Override
            public void onMessage(JmsInboundMessageDispatch envelope) {
            }

            @Override
            public void onConnectionRestored() {
                System.out.println("Connection reports restored to: " + connection.getProvider().getRemoteURI());
            }

            @Override
            public void onConnectionInterrupted() {
                System.out.println("Connection reports interrupted.");
            }

            @Override
            public void onConnectionFailure(Throwable error) {
                System.out.println("Connection reported failure: {}" + error.getMessage());
            }
        });
        connection.start();
        System.out.println("Connected to : " + ((JmsConnection) connection).getProvider().getRemoteURI());
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        MessageProducer producer = session.createProducer(dest);
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

        for (int i = 1; i <= messages; i++) {
            TextMessage msg = session.createTextMessage("#:" + i);
            msg.setIntProperty("id", i);
            producer.send(msg);
            if ((i % 1000) == 0) {
                System.out.println(String.format("Sent %d messages", i));
            }
            Thread.sleep(sleepTime);
        }

        producer.send(session.createTextMessage("SHUTDOWN"));
        Thread.sleep(1000 * 3);
        connection.close();
        System.exit(0);
    }


    private static String env(String key, String defaultValue) {
        String rc = System.getenv(key);
        if( rc== null )
            return defaultValue;
        return rc;
    }

    private static String arg(String []args, int index, String defaultValue) {
        if( index < args.length )
            return args[index];
        else
            return defaultValue;
    }

}
