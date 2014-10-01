package my.cool.demo.app;

import my.cool.demo.Calling;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Reference;

@Component
public class Client {

    @Reference(referenceInterface = Calling.class)
    public Calling calling;

    @Activate
    public void start() {
        calling.sayHello("Morning");
    }

    @Deactivate
    public void stop() {
        calling.sayHello("Evening");
    }
}
