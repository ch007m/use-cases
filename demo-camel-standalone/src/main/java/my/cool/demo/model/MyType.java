package my.cool.demo.model;

import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

import java.util.Date;

@CsvRecord(separator = ",", quote = "'")
public class MyType {

    @DataField(pos = 1, required = true, pattern = "dd-MMM-yyyy hh aa")
    Date creationDate;

    @DataField(pos = 2, required = true)
    String user;

    public String toString() {
        return ">> Date : " + creationDate.toString() + "," +
               "User : " + user;
    }

}
