package org.acme.test;

import org.acme.model.Person;
import org.drools.core.impl.StatelessKnowledgeSessionImpl;
import org.junit.BeforeClass;
import org.junit.Test;
import org.kie.api.runtime.KieSession;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class KieGlobalsTest {

    static ApplicationContext context = null;

    @BeforeClass
    public static void setup() {
        context = new ClassPathXmlApplicationContext("/spring/kie.xml");
    }

    @Test
    public void testKieSession() throws Exception {
        StatelessKnowledgeSessionImpl ksession = (StatelessKnowledgeSessionImpl) context.getBean("ksession1");
        assertNotNull(ksession);

        Collection col = ksession.getGlobals().getGlobalKeys();
        assertEquals(1, col.size());
/*
        Person[] persons = (Person[]) col.toArray(new Person[col.size()]);
        Person p = (Person) persons[0];
        assertNotNull(p);*/
    }

}
