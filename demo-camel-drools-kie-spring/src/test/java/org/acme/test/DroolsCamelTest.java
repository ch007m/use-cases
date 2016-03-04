package org.acme.test;

import org.acme.model.Person;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DroolsCamelTest extends CamelSpringTestSupport {

    private static final Logger logger = LoggerFactory.getLogger(DroolsCamelTest.class);

    @Override
    protected AbstractApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("/spring/camel-context.xml");
    }

    @Test
    public void testDroolsCamel() throws InterruptedException {
        Person p = new Person();
        p.setName("charles");
        template.sendBody("direct:simple_rule1",p);
    }
}
