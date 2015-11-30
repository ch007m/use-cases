package my.cool.demo;

import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.naming.InitialContext;
import javax.naming.Reference;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.deltaspike.cdise.api.CdiContainer;
import org.apache.deltaspike.cdise.api.CdiContainerLoader;
import org.eclipse.jetty.plus.jndi.Resource;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.jboss.weld.environment.servlet.Listener;

import java.io.IOException;

/**
 * Simple jetty launcher, which launches the webapplication from the local
 * resources and reuses the projects classpath.
 *
 * @author jos
 */
public class Launcher {

    /**
     * run under root context
     */
    private static String contextPath = "/";
    /**
     * location where resources should be provided from for VAADIN resources
     */
    private static String resourceBase = "src/main/resources";
    /**
     * port to listen on
     */
    private static int httpPort = 8081;

    private static String[] _configurationClasses = {
            "org.eclipse.jetty.webapp.WebInfConfiguration",
            "org.eclipse.jetty.webapp.WebXmlConfiguration",
            "org.eclipse.jetty.webapp.MetaInfConfiguration",
            "org.eclipse.jetty.webapp.FragmentConfiguration",
            "org.eclipse.jetty.plus.webapp.EnvConfiguration",
            "org.eclipse.jetty.webapp.JettyWebXmlConfiguration" };

    private static String[] _ServerClasses = {
            "org.eclipse.jetty.servlet.ServletContextHandler.Decorator",
            "org.eclipse.jetty.server.handler.ContextHandler",
            "org.eclipse.jetty.servlet.FilterHolder",
            "org.eclipse.jetty.servlet.ServletContextHandler",
            "org.eclipse.jetty.servlet.ServletHolder"
    };

    /**
     * Start the server, and keep waiting.
     */
    public static void main(String[] args) throws Exception {

        CdiContainer cdiContainer = CdiContainerLoader.getCdiContainer();
        cdiContainer.boot();
        cdiContainer.getContextControl().startContexts();

        System.setProperty("java.naming.factory.url", "org.eclipse.jetty.jndi");
        System.setProperty("java.naming.factory.initial", "org.eclipse.jetty.jndi.InitialContextFactory");

        InitialContext ctx = new InitialContext();
        //ctx.createSubcontext("java:comp"); NOT REQUIRED

        Server server = new Server(httpPort);
        WebAppContext webapp = new WebAppContext();

        //webapp.setConfigurationClasses(_configurationClasses);
        //webapp.setServerClasses(_ServerClasses);
        //webapp.addEventListener(new Listener());

        webapp.setContextPath(contextPath);
        webapp.setResourceBase(resourceBase);

        webapp.setClassLoader(Thread.currentThread().getContextClassLoader());

        webapp.addServlet(HelloWorldServlet.class, "/*");

        server.setHandler(webapp);

        server.start();
        server.join();

        cdiContainer.shutdown();
    }

    public static class HelloWorldServlet extends HttpServlet {

        @Inject BeanManager manager;

        protected void doGet(HttpServletRequest req, HttpServletResponse resp)
                throws ServletException, IOException {

            resp.setContentType("text/plain");
            resp.getWriter().append("Hello from " + manager);
        }
    }
}