package org.fuse.usecase;

import org.apache.camel.*;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.SearchResult;
import java.util.*;

public class LDAPCamelTest extends CamelSpringTestSupport {

    private static final String USER_JDOE = "[uid=jdoe,ou=User,ou=ActiveMQ: null:null:{uid=uid: jdoe, cn=cn: Jane Doe}]";

    @Test
    public void testGetUserFromLdap() throws Exception {

        CamelContext ctx = super.context();
        ctx.addRoutes(createRouteBuilder("ldap:ldapserver?base=ou=system&returnedAttributes=uid,cn"));
        ProducerTemplate template = ctx.createProducerTemplate();

        MockEndpoint resultEndpoint = (MockEndpoint) ctx.getEndpoint("mock:result");

        resultEndpoint.expectedBodiesReceived(USER_JDOE);

        template.sendBody("direct:start","(&(objectClass=*)(uid=jdoe))");

        List<Exchange> exchanges = resultEndpoint.getExchanges();
        for (Exchange msg : exchanges) {
            ArrayList results = (ArrayList) msg.getIn().getBody();
            SearchResult result = (SearchResult)results.get(0);
            String name = result.getName();
            BasicAttributes attrs = (BasicAttributes) result.getAttributes();
            try {
                for (NamingEnumeration ae = attrs.getAll(); ae.hasMore();) {
                    Attribute attr = (Attribute) ae.next();
                    System.out.println("attribute: " + attr.getID());

                    /* print each value */
                    for (NamingEnumeration e = attr.getAll(); e.hasMore();) {
                        if (attr.getID().equals("userpassword")) {
                            byte[] b = (byte[]) e.next();
                            System.out.println("value: " + new String(b));
                        } else {
                            System.out.println("value: " + e.next().toString());;
                        }
                    }
                }
            } catch (NamingException e) {
                e.printStackTrace();
            }
        }

        resultEndpoint.assertIsSatisfied();
    }

    @Override protected AbstractApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("org/fuse/usecase/camel-context.xml");
    }

    protected RouteBuilder createRouteBuilder(final String ldapEndpointUrl) throws Exception {
        return new RouteBuilder() {
            public void configure() throws Exception {
                from("direct:start").to(ldapEndpointUrl).to("mock:result");
            }
        };
    }
}
