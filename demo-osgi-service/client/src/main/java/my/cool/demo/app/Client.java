package my.cool.demo.app;

import my.cool.demo.Calling;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

@Component
public class Client implements BundleActivator {

    @Reference(referenceInterface = Calling.class)
    public Calling calling;

    @Override
    public void start(BundleContext bundleContext) throws Exception {
        calling.sayHello("Morning");
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {
        calling.sayHello("Evening");
    }
}
