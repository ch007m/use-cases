package org.acme.test;

import org.acme.model.Person;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.concurrent.ThreadLocalRandom;

public class DroolsCamelTest extends CamelSpringTestSupport {

    private static final Logger logger = LoggerFactory.getLogger(DroolsCamelTest.class);
    String[] users = {"Charles", "Samuel", "Claus", "Jeff", "James", "Chad", "Rachel", "Eric", "Nandan", "David", "Bernard", "Satya" , "Mario", "Chris"};

    @Override
    protected AbstractApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("/spring/camel-context.xml");
    }

    @Test
    public void testDroolsCamel() throws InterruptedException {
        for(int i = 0; i < 20; i++){
            template.sendBody("direct:camel_rule", users[ThreadLocalRandom.current().nextInt(users.length)]);
        }
    }
}
