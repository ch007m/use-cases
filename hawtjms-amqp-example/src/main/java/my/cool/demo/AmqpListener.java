package my.cool.demo;

import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;

import javax.jms.*;

public class AmqpListener extends ConnectionFactoryUtility {

    static String user;
    static String password;

    public static void main(String []args) throws Exception {

/*      user = env("ACTIVEMQ_USER", "admin");
        password = env("ACTIVEMQ_PASSWORD", "password");
        host = env("ACTIVEMQ_HOST", "localhost");
        port = Integer.parseInt(env("ACTIVEMQ_PORT", "5672"));
        String destination = arg(args, 0, "topic://event");*/

        user = "admin";
        password = "admin";
        host = "localhost";
        // port = 5672;
        // port = 61616;
        port = 52191;
        failover = false;
        String destination = "DEMO";

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

        Connection connection = createAmqpConnection(user, password, failover);
        connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageConsumer consumer = session.createConsumer(dest);
            long start = System.currentTimeMillis();
            long count = 1;
            System.out.println("Waiting for messages...");
            while(true) {
                Message msg = consumer.receive();
                if( msg instanceof TextMessage ) {
                    body = ((TextMessage) msg).getText();
                    if( "SHUTDOWN".equals(body)) {
                        long diff = System.currentTimeMillis() - start;
                        System.out.println(String.format("Received %d in %.2f seconds", count, (1.0*diff/1000.0)));
                        connection.close();
                        System.exit(1);
                    } else {
                        try {
                            if( count != msg.getIntProperty("id") ) {
                                System.out.println("mismatch: "+count+"!="+msg.getIntProperty("id"));
                            }
                        } catch (NumberFormatException ignore) {
                        }
                        if( count == 1 ) {
                            start = System.currentTimeMillis();
                        } else if( count % 1000 == 0 ) {
                            System.out.println(String.format("Received %d messages.", count));
                        }
                        count ++;
                    }

                } else {
                    System.out.println("Unexpected message type: "+msg.getClass());
                }
            }
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