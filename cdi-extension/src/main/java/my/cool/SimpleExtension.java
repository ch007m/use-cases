package my.cool;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.*;

public class SimpleExtension implements Extension {

    public void registerBeans(@Observes AfterBeanDiscovery abd, BeanManager manager) {
        if (manager.getBeans(Service.class, AnyLiteral.INSTANCE).isEmpty())
            abd.addBean(new ServiceBean(manager));
    }

}