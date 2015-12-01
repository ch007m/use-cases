package my.cool.demo;

import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class AppListener implements ServletContextListener {

    private final static String BEAN_MANAGER_ATTRIBUTE = "org.jboss.reasteasy.cdi";
    @Inject BeanManager beanManager;

    public void contextInitialized(ServletContextEvent servletContextEvent) {
        System.out.println(">> Context Initialized of the AppListener");
        ServletContext ctx = servletContextEvent.getServletContext();
        ctx.setAttribute(BEAN_MANAGER_ATTRIBUTE, beanManager);
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        System.out.println(">> Context destroyed of the AppListener");
    }
}
