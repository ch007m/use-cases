package my.cool.demo;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Simple jetty launcher, which launches the webapplication from the local
 * resources and reuses the projects classpath.
 *
 * @author jos
 */
public class LauncherBundle implements BundleActivator {

    /**
     * run under root context
     */
    private static String contextPath = "/";

    /**
     * port to listen on
     */
    private static int httpPort = 8081;

    private static String[] _configurationClasses = { "org.eclipse.jetty.webapp.WebInfConfiguration",
            "org.eclipse.jetty.webapp.WebXmlConfiguration", "org.eclipse.jetty.webapp.MetaInfConfiguration",
            "org.eclipse.jetty.webapp.FragmentConfiguration",
            "org.eclipse.jetty.plus.webapp.EnvConfiguration",
            "org.eclipse.jetty.webapp.JettyWebXmlConfiguration" };

    private static String[] _ServerClasses = { "org.eclipse.jetty.servlet.ServletContextHandler.Decorator",
            "org.eclipse.jetty.server.handler.ContextHandler", "org.eclipse.jetty.servlet.FilterHolder",
            "org.eclipse.jetty.servlet.ServletContextHandler", "org.eclipse.jetty.servlet.ServletHolder" };

    private Server server;

    @Override public void start(BundleContext context) throws Exception {

/*        System.setProperty("java.naming.factory.url", "org.eclipse.jetty.jndi");
        System.setProperty("java.naming.factory.initial", "org.eclipse.jetty.jndi.InitialContextFactory");

        ClassLoader thatLoader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(getClass().getClassLoader());

        try {
            InitialContext ctx = new InitialContext();
        } finally {
            Thread.currentThread().setContextClassLoader(thatLoader);
        }*/

        server = new Server(httpPort);
        WebAppContext webapp = new WebAppContext();

        webapp.setContextPath(contextPath);
        webapp.setResourceBase("/");

        webapp.setClassLoader(getClass().getClassLoader());

        webapp.addServlet(HelloWorldServlet.class, "/*");

        webapp.setClassLoader(getClass().getClassLoader());

        server.setHandler(webapp);
        server.start();
        server.join();
    }

    @Override public void stop(BundleContext bundleContext) throws Exception {
        server.stop();
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