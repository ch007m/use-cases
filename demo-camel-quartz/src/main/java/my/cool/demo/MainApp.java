package my.cool.demo;

import my.cool.demo.route.SimpleQuartzBuilder;
import org.apache.camel.main.Main;

/**
 * A Camel Application
 */
public class MainApp {

    /**
     * A main() so we can easily run these routing rules in our IDE
     */
    public static void main(String... args) throws Exception {
        Main main = new Main();
        main.enableHangupSupport();
        main.addRouteBuilder(new SimpleQuartzBuilder());
        main.enableTrace();
        main.run(args);
    }

}

