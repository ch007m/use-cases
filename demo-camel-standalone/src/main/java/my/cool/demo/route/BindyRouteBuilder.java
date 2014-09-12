package my.cool.demo.route;

import my.cool.demo.model.MyType;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;
import org.apache.camel.model.dataformat.BindyDataFormat;
import org.apache.camel.model.dataformat.BindyType;
import org.apache.camel.model.dataformat.CsvDataFormat;

public class BindyRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        BindyDataFormat bindy = new BindyDataFormat();
        bindy.setType(BindyType.Csv);
        bindy.setClassType(MyType.class);

        from("timer://bindy")
           .setBody().constant("'10-Nov-2013 1 PM','Arthur'")
           .unmarshal(bindy)
           .log("Result : ${body}");

    }
}