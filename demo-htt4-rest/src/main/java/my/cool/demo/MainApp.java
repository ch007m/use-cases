package my.cool.demo;

import my.cool.demo.model.TankLevelReport;
import my.cool.demo.route.RESTFullClient;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.main.Main;

import java.util.HashMap;
import java.util.Map;

/**
 * A Camel Application
 */
public class MainApp {

    /**
     * A main() so we can easily run these routing rules in our IDE
     */
    public static void main(String... args) throws Exception {

        String USER = "Accenture";
        String PASS = "6bznqSKX";

        Main main = new Main();
        main.enableHangupSupport();
        main.addRouteBuilder(new RESTFullClient());
        main.enableTrace();
        main.start();

        ProducerTemplate t = main.getCamelTemplate();
        Map<String,Object> parameters = new HashMap<String,Object>();

        /*
         * Get a Token from wayne service
         */
        parameters.put(Exchange.HTTP_URI,"https://enterprise2.wayne.com/FuelReportingService/Service.svc/rest/GetSession");
        parameters.put(Exchange.HTTP_QUERY,"User=" + USER + "&" + "Pass=" + PASS);
        parameters.put(Exchange.HTTP_METHOD, "GET");
        parameters.put("service","GetSession");
        String token = (String)t.requestBodyAndHeaders("direct:wayne-get-token", "", parameters);
        token = token.replaceAll("\"","");
        System.out.println(">> Token : " + token);

        /*
         * Get TankLevelsReport
         */
        parameters.put(Exchange.HTTP_URI, "https://enterprise2.wayne.com/FuelReportingService/Service.svc/rest/TankLevelsReport");
        parameters.put(Exchange.HTTP_QUERY,"SessionId=" + token);
        parameters.put(Exchange.HTTP_METHOD,"GET");
        parameters.put("service","TankLevelsReport");
        TankLevelReport tankLevelReport = (TankLevelReport)t.requestBodyAndHeaders("direct:wayne-report", "", parameters);
        System.out.println(">> TankLevelReport Return Message: " + tankLevelReport.getReturnMessage());
        System.out.println(">> TankLevelReport Return Code: " + tankLevelReport.getReturnCode());
        System.out.println(">> TankLevels Size : " + tankLevelReport.getTankLevels().size());

        main.run();
    }

}

