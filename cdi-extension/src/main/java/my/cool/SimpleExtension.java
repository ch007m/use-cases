package my.cool;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.*;

public class SimpleExtension implements Extension {

    public void registerBeans(@Observes BeforeBeanDiscovery event, BeanManager manager) {
        event.addAnnotatedType(manager.createAnnotatedType(Foo.class));
    }

}