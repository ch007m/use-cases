package org.dabou;

import org.dabou.bean.User;
import org.dabou.bean.UserImpl;
import org.dabou.service.HelloWorld;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import java.util.Map;
import java.util.Set;

public class ClientWS {

    private static final String PORT = "9000";
    private static final QName serviceName = new QName("http://apache.org/hello_world_soap_http",
            "SOAPService");

    public static void main(String[] args) {

        Service service = Service.create(serviceName);
        HelloWorld hw = service.getPort(HelloWorld.class);

        User user = new UserImpl("Student2");

        BindingProvider bp = (BindingProvider)hw;
        bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
                "http://localhost:" + PORT + "/soap/helloWorld");
        bp.getRequestContext().put(BindingProvider.SESSION_MAINTAIN_PROPERTY, true);

        //
        System.out.println("Response from sayHi : " + hw.sayHi("Student1"));
        System.out.println("Response from sayHiToUser : " + hw.sayHiToUser(user));

        Map<Integer, User> users = hw.getUsers();
        Set<Integer> keys = users.keySet();

        for(Integer key : keys) {
            System.out.println(key + " User : " + users.get(key).getName());
        }
    }
}
