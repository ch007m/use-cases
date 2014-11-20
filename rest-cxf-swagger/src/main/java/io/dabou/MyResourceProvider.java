package io.dabou;

import org.apache.cxf.jaxrs.lifecycle.PerRequestResourceProvider;
import org.apache.cxf.jaxrs.lifecycle.ResourceProvider;
import org.apache.cxf.jaxrs.utils.ResourceUtils;
import org.apache.cxf.message.Message;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class MyResourceProvider implements ResourceProvider {

    // Using the dynamical proxy to provide the instance of client to invoke
    private Class<?> clazz;
    private ResourceProvider provider;

    public MyResourceProvider(Class<?> clazz) {
        this.clazz = clazz;
        if (!clazz.isInterface()) {
            provider = new PerRequestResourceProvider(clazz);
        }
    }

    @Override
    public Object getInstance(Message m) {
        Object result = null;
        if (provider != null) {
            result = provider.getInstance(m);
        } else {
            // create the instance with the invoker
            result = Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz},
                    new SubResourceClassInvocationHandler());
        }
        return result;
    }

    @Override
    public void releaseInstance(Message m, Object o) {
        if (provider != null) {
            provider.releaseInstance(m, o);
        }
    }

    @Override
    public Class<?> getResourceClass() {
        if (provider != null) {
            return provider.getResourceClass();
        } else {
            return clazz;
        }
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

    public class SubResourceClassInvocationHandler implements InvocationHandler {

        @Override
        public Object invoke(Object proxy, Method method, Object[] parameters) throws Throwable {
            Object result = null;
            Class<?> returnType = method.getReturnType();
            if (!returnType.isAssignableFrom(Void.class)) {
                // create a instance to return
                if (returnType.isInterface()) {
                    // create a new proxy for it
                    result = Proxy.newProxyInstance(returnType.getClassLoader(), new Class[] {returnType},
                            new SubResourceClassInvocationHandler());
                } else {
                    // get the constructor and create a new instance
                    Constructor<?> c = ResourceUtils.findResourceConstructor(returnType, true);
                    result = c.newInstance(new Object[] {});
                }
            }
            return result;
        }

    }


}
