package my.cool.demo;

import org.apache.camel.builder.ExpressionBuilder;
import org.apache.camel.builder.PredicateBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.jsonpath.JsonPath;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

public class JSonPathCamelTest extends CamelTestSupport {

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:start")
                        .choice()
                        .when().jsonpath("$.store.book[?(@.price < 10)]")
                        .to("mock:cheap")
                        .when().jsonpath("$.store.book[?(@.price < 30)]")
                        .to("mock:average")
                        .otherwise()
                        .to("mock:expensive");

                from("direct:bicycle")
                        .choice()
                        .when().method(new BeanPredicate())
                        .to("mock:cheap")
                        .otherwise()
                        .to("mock:expensive");

                from("direct:bicycle2")
                        .choice()
                        .when(PredicateBuilder.isLessThan(ExpressionBuilder.languageExpression("jsonpath", "$.store.bicycle.price"), ExpressionBuilder.constantExpression(20)))
                        .to("mock:cheap")
                        .otherwise()
                        .to("mock:expensive");

                from("direct:type")
                      .choice()
                      .when(PredicateBuilder.isEqualTo(ExpressionBuilder.languageExpression("jsonpath", "$.kind"), ExpressionBuilder.constantExpression("full")))
                        .to("mock:full")
                        .otherwise()
                        .to("mock:empty");
            }
        };
    }

    public static class BeanPredicate {
        public boolean checkPrice(@JsonPath("$.store.bicycle.price") double price) {
            return price < 20;
        }
    }

    @Test
    public void testCheapBicycle() throws Exception {
        sendMessageToBicycleRoute("direct:bicycle");
        resetMocks();
        sendMessageToBicycleRoute("direct:bicycle2");

    }

    private void sendMessageToBicycleRoute(String startPoint) throws Exception {
        getMockEndpoint("mock:cheap").expectedMessageCount(1);
        getMockEndpoint("mock:average").expectedMessageCount(0);
        getMockEndpoint("mock:expensive").expectedMessageCount(0);

        template.sendBody(startPoint, readFileContent("/cheap.json"));

        assertMockEndpointsSatisfied();
    }

    @Test
    public void testCheap() throws Exception {
        getMockEndpoint("mock:cheap").expectedMessageCount(1);
        getMockEndpoint("mock:average").expectedMessageCount(0);
        getMockEndpoint("mock:expensive").expectedMessageCount(0);

        template.sendBody("direct:start", readFileContent("/cheap.json"));

        assertMockEndpointsSatisfied();
    }

    @Test
    public void testAverage() throws Exception {
        getMockEndpoint("mock:cheap").expectedMessageCount(0);
        getMockEndpoint("mock:average").expectedMessageCount(1);
        getMockEndpoint("mock:expensive").expectedMessageCount(0);

        template.sendBody("direct:start", readFileContent("/average.json"));

        assertMockEndpointsSatisfied();
    }

    @Test
    public void testExpensive() throws Exception {
        getMockEndpoint("mock:cheap").expectedMessageCount(0);
        getMockEndpoint("mock:average").expectedMessageCount(0);
        getMockEndpoint("mock:expensive").expectedMessageCount(1);

        template.sendBody("direct:start", readFileContent("/expensive.json"));

        assertMockEndpointsSatisfied();
    }

    @Test
    public void testKindFull() throws Exception {
        getMockEndpoint("mock:full").expectedMessageCount(1);
        getMockEndpoint("mock:empty").expectedMessageCount(0);
        template.sendBody("direct:type", readFileContent("/type1.json"));
        assertMockEndpointsSatisfied();
    }

    @Test
    public void testKindEmpty() throws Exception {
        getMockEndpoint("mock:full").expectedMessageCount(0);
        getMockEndpoint("mock:empty").expectedMessageCount(1);
        template.sendBody("direct:type", readFileContent("/type2.json"));
        assertMockEndpointsSatisfied();
    }

    public static String readFileContent(String location) throws IOException {
        URL path = JSonPathCamelTest.class.getResource(location);
        InputStream input = new URL(path.toString()).openStream();
        String content = new Scanner(input, "UTF-8").useDelimiter("\\A").next();
        return content;
    }

}
