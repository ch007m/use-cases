package my.cool;

import javax.enterprise.context.RequestScoped;

@RequestScoped
public class Foo {

    public String ping() {
        return "foo";
    }

}
