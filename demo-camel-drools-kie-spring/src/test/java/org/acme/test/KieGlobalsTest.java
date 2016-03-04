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
        // GET A CLASS CAST
        KieSession ksession = (KieSession) context.getBean("ksession1");
        //StatelessKnowledgeSessionImpl ksession = (StatelessKnowledgeSessionImpl) context.getBean("ksession1");
        assertNotNull(ksession);

        Collection col = ksession.getGlobals().getGlobalKeys();
        assertEquals(1, col.size());
    }

}
