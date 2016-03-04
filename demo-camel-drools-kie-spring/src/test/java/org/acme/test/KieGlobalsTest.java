package org.acme.test;

import org.acme.model.Person;
import org.junit.BeforeClass;
import org.junit.Test;
import org.kie.api.runtime.KieSession;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class KieGlobalsTest {

    static ApplicationContext context = null;

    @BeforeClass
    public static void setup() {
        context = new ClassPathXmlApplicationContext("/spring/kie.xml");
    }

    @Test
    public void testKieSession() throws Exception {
        KieSession ksession = (KieSession) context.getBean("ksession1");
        assertNotNull(ksession);

        // Key = persons
        Collection col = ksession.getGlobals().getGlobalKeys();
        assertEquals(1, col.size());

        // List contains 2 records
        List<Person> persons = (List<Person>) ksession.getGlobals().get("persons");
        assertNotNull(persons);
        assertEquals("charles",persons.get(0).getName());
        assertEquals("samuel",persons.get(1).getName());


    }

}
