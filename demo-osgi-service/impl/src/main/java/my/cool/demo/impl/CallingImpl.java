package my.cool.demo.impl;

import my.cool.demo.Calling;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Service;

@Component(immediate = true)
@Service(value=Calling.class)
public class CallingImpl implements Calling {

    @Activate
    void start(){
        System.out.println("Started CallingImpl.");
    }

    @Deactivate
    void stop(){
        System.out.println("Stopped CallingImpl.");
    }

    @Override
    public void sayHello(String message) {
        System.out.println("Hello : " + message + " !");
    }
}
