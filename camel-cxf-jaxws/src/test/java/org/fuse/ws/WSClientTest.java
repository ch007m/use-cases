package org.fuse.ws;

import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.bus.spring.SpringBusFactory;
import org.fuse.ws.model.InputReportIncident;
import org.fuse.ws.model.OutputReportIncident;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.URL;

public class WSClientTest extends CamelSpringTestSupport {

    @Override protected AbstractApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("/META-INF/spring/context.xml");
    }

    @Test public void ReportIncidentWS() throws Exception {
        SpringBusFactory bf = new SpringBusFactory();
        URL busFile = WSClientTest.class.getResource("/config/client.xml");
        Bus bus = bf.createBus(busFile.toString());
        BusFactory.setDefaultBus(bus);
        BusFactory.setThreadDefaultBus(bus);

        InputReportIncident incident = new InputReportIncident();
        incident.setFamilyName("Moulliard");
        incident.setGivenName("Charles");
        incident.setEmail("cm@redhat.com");
        incident.setIncidentId("123");

        URL wsdlURL = new URL("http://localhost:9999/ws/report?wsdl");
        /*
         * QName = targetNamespace="http://service.ws.fuse.org/", xmlns:tns="http://ws.fuse.org/" of the WSDL file
         * SERVICE_NAME = name="incidentService" of the wsdl file
         */
        QName SERVICE_NAME = new QName("http://service.ws.fuse.org/", "incidentService");
        Service service = Service.create(wsdlURL, SERVICE_NAME);
        IncidentService client = service.getPort(IncidentService.class);
        OutputReportIncident response = client.reportIncident(incident);

        assertEquals("Check Response", "OK;123", response.getCode());
    }
}

