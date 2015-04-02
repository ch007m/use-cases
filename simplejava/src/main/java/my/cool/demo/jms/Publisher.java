/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package my.cool.demo.jms;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;

class Publisher {
    
    private static final Logger logger = LoggerFactory.getLogger(Publisher.class);

    public static void main(String []args) throws JMSException {

        String user = env("ACTIVEMQ_USER", "");
        String password = env("ACTIVEMQ_PASSWORD", "");
        String host = env("ACTIVEMQ_HOST", "localhost");
        int port = Integer.parseInt(env("ACTIVEMQ_PORT", "61616"));
        String destination = arg(args, 0, "foo.bar");

        int messages = 10;
        int size = 256;

        String DATA = "abcdefghijklmnopqrstuvwxyz";
        String body = "";
        for( int i=0; i < size; i ++) {
            body += DATA.charAt(i%DATA.length());
        }

        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://" + host + ":" + port + "?jms.useAsyncSend=false");

        Connection connection = factory.createConnection(user, password);
        logger.info("Connection created");
        connection.start();
        logger.info("Connection started");
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination dest = new ActiveMQQueue(destination);
        MessageProducer producer = session.createProducer(dest);
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

        for( int i=1; i <= messages; i ++) {
            TextMessage msg = session.createTextMessage(body);
            msg.setIntProperty("id", i);
            producer.send(msg);
            logger.info("Message send" + i );
        }

        producer.send(session.createTextMessage("THE.END"));
        connection.close();
        logger.info("Connection closed");

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