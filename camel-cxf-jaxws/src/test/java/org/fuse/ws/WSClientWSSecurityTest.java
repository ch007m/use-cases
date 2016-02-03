package org.fuse.ws;

import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.bus.spring.SpringBusFactory;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.phase.PhaseInterceptor;
import org.apache.cxf.ws.security.wss4j.DefaultCryptoCoverageChecker;
import org.apache.cxf.ws.security.wss4j.WSS4JInInterceptor;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;
import org.fuse.ws.model.InputReportIncident;
import org.fuse.ws.model.OutputReportIncident;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WSClientWSSecurityTest extends CamelSpringTestSupport {

    private static final String WSSE_NS
            = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd";
    private static final String WSU_NS
            = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd";

    @Override protected AbstractApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("/META-INF/spring/context.xml");
    }

    @Test public void ReportIncidentWS() throws Exception {

        // URL busFile = WSClientWSSecurityTest.class.getResource("/config/client.xml");
        // Bus bus = bf.createBus(busFile.toString());

        SpringBusFactory bf = new SpringBusFactory();
        Bus bus = bf.createBus();
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

        // Check to make sure that the SOAP Body and Timestamp were signed,
        // and that the SOAP Body was encrypted
        DefaultCryptoCoverageChecker coverageChecker = new DefaultCryptoCoverageChecker();
        coverageChecker.setSignBody(true);
        coverageChecker.setSignTimestamp(true);
        coverageChecker.setEncryptBody(true);

        WSS4JInInterceptor wss4JInInterceptor = new WSS4JInInterceptor(ConfigureInSecurity());
        List in = new ArrayList<PhaseInterceptor>();
        in.add(new LoggingInInterceptor());
        in.add(wss4JInInterceptor);
        in.add(coverageChecker);

        WSS4JOutInterceptor wss4JOutInterceptor = new WSS4JOutInterceptor(ConfigureOutSecurity());
        List out = new ArrayList<PhaseInterceptor>();
        out.add(new LoggingOutInterceptor());
        out.add(wss4JOutInterceptor);

        ClientProxy.getClient(client).getInInterceptors().addAll(in);
        ClientProxy.getClient(client).getOutInterceptors().addAll(out);

        OutputReportIncident response = client.reportIncident(incident);

        assertEquals("Check Response", "OK;123", response.getCode());
    }

    public static Map<String, Object> ConfigureInSecurity() {
        Map<String, Object> inProps = new HashMap<String, Object>();

        inProps.put("action", "UsernameToken Timestamp Signature Encrypt");
        inProps.put("passwordType", "PasswordText");
        inProps.put("passwordCallbackClass", "org.fuse.ws.UTPasswordCallback");

        inProps.put("decryptionPropFile", "config/Client_Sign.properties");
        inProps.put("encryptionKeyIdentifier", "IssuerSerial");

        inProps.put("signaturePropFile", "config/Client_Encrypt.properties");
        inProps.put("signatureKeyIdentifier", "DirectReference");

        inProps.put("signatureAlgorithm", "http://www.w3.org/2000/09/xmldsig#rsa-sha1");

        return inProps;
    }

    public static Map<String, Object> ConfigureOutSecurity() {
        Map<String, Object> outProps = new HashMap<String, Object>();
        outProps.put("action", "UsernameToken Timestamp Signature Encrypt");

        outProps.put("passwordType", "PasswordDigest");

        outProps.put("user", "abcd");
        outProps.put("signatureUser", "clientx509v1");

        outProps.put("passwordCallbackClass", "org.fuse.ws.UTPasswordCallback");

/*      outProps.put("encryptionUser", "serverx509v1");
        outProps.put("encryptionPropFile", "config/Client_Encrypt.properties");
        outProps.put("encryptionKeyIdentifier", "IssuerSerial");
        outProps.put("encryptionParts",
                "{Element}{" + WSSE_NS + "}UsernameToken;"
                        + "{Content}{http://schemas.xmlsoap.org/soap/envelope/}Body");*/

        outProps.put("signaturePropFile", "config/Client_Sign.properties");
        outProps.put("signatureKeyIdentifier", "DirectReference");
        outProps.put("signatureParts",
                "{Element}{" + WSU_NS + "}Timestamp;"
                        + "{Element}{http://schemas.xmlsoap.org/soap/envelope/}Body;");

        outProps.put("signatureAlgorithm", "http://www.w3.org/2000/09/xmldsig#rsa-sha1");
        return outProps;
    }
}

