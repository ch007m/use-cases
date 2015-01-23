package my.cool;

public class Service {

    protected Foo foo;

    public Foo getFoo() {
        return new Foo();
    }

    public class Foo {
        public String ping() {
            return "foo";
        }
    }

}
