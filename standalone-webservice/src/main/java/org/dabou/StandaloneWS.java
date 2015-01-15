package org.dabou;

import org.apache.cxf.Bus;
import org.apache.cxf.BusException;
import org.apache.cxf.BusFactory;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;
import org.apache.cxf.transport.DestinationFactory;
import org.apache.cxf.transport.DestinationFactoryManager;
import org.apache.cxf.transport.http.HTTPTransportFactory;
import org.apache.cxf.transport.local.LocalTransportFactory;
import org.apache.cxf.transport.servlet.CXFNonSpringServlet;
import org.dabou.service.HelloWorld;
import org.dabou.service.HelloWorldImpl;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;


import javax.naming.Context;

public class StandaloneWS {

    public static void main(String[] args) throws Exception {

        Bus bus = BusFactory.getDefaultBus();
        DestinationFactoryManager dfm = bus.getExtension(DestinationFactoryManager.class);

        /*      USE LOCAL Transport
        LocalTransportFactory localTransport = new LocalTransportFactory();
        dfm.registerDestinationFactory("http://schemas.xmlsoap.org/soap/http", localTransport);
        dfm.registerDestinationFactory("http://schemas.xmlsoap.org/wsdl/soap/http", localTransport);
        dfm.registerDestinationFactory("http://cxf.apache.org/bindings/xformat", localTransport);
        dfm.registerDestinationFactory("http://cxf.apache.org/transports/local", localTransport);
        */

        /* HTTP Transport
        HTTPTransportFactory httpTransport = new HTTPTransportFactory();
        dfm.registerDestinationFactory("http://cxf.apache.org/transports/http/configuration",httpTransport);
        */

        Server httpServer = new Server(9000);
        ContextHandlerCollection contexts = new ContextHandlerCollection();
        httpServer.setHandler(contexts);

        ServletContextHandler root = new ServletContextHandler(contexts, "/",
                ServletContextHandler.SESSIONS);

        CXFNonSpringServlet cxf = new CXFNonSpringServlet();
        cxf.setBus(bus);

        ServletHolder servlet = new ServletHolder(cxf);
        servlet.setName("soap");
        servlet.setForcedPath("soap");
        root.addServlet(servlet, "/soap/*");

        httpServer.start();

        HelloWorldImpl implementor = new HelloWorldImpl();
        JaxWsServerFactoryBean svrFactory = new JaxWsServerFactoryBean();
        svrFactory.setServiceClass(HelloWorld.class);
        //svrFactory.setAddress("local://helloWorld");
        svrFactory.setAddress("/helloWorld");
        svrFactory.setServiceBean(implementor);
        svrFactory.getInInterceptors().add(new LoggingInInterceptor());
        svrFactory.getOutInterceptors().add(new LoggingOutInterceptor());
        svrFactory.create();
    }
}
