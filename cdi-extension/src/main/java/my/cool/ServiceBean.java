package my.cool;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class ServiceBean implements Bean<Service>, PassivationCapable {

    private final InjectionTarget<Service> target;
    private final Set<Annotation> qualifiers;
    private final Set<Type> types;

    public ServiceBean(BeanManager manager) {
        this.qualifiers = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(AnyLiteral.INSTANCE, DefaultLiteral.INSTANCE)));
        AnnotatedType<Service> annotatedType = manager.createAnnotatedType(Service.class);
        // Calculate the types of the Bean (Object, Interface, Bean, ...)
        this.types = Collections.unmodifiableSet(annotatedType.getTypeClosure());
        this.target = manager.createInjectionTarget(annotatedType);
    }

    @Override
    public Class<? extends Annotation> getScope() {
        return RequestScoped.class;
    }

    @Override
    public Class<Service> getBeanClass() {
        return Service.class;
    }

    @Override
    public Set<InjectionPoint> getInjectionPoints() {
        return Collections.emptySet();
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean isNullable() {
        return false;
    }

    @Override
    public boolean isAlternative() {
        return false;
    }

    @Override
    public Set<Type> getTypes() {
        return types;
    }

    @Override
    public Set<Annotation> getQualifiers() {
        return qualifiers;
    }

    @Override
    public Set<Class<? extends Annotation>> getStereotypes() {
        return Collections.emptySet();
    }

    @Override
    public Service create(CreationalContext<Service> creational) {
        Service svc = target.produce(creational);
        target.inject(svc, creational);
        target.postConstruct(svc);
        creational.push(svc);
        return svc;
    }

    @Override
    public void destroy(Service instance, CreationalContext<Service> creational) {
        target.preDestroy(instance);
        target.dispose(instance);
        creational.release();
    }


    @Override
    public String getId() {
        return getClass().getName();
    }
}
