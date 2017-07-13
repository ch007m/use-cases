package my.cool;

import org.apache.commons.text.StrMatcher;
import org.apache.commons.text.StrSubstitutor;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by dabou on 13/07/2017.
 */
public class SubStituteKey {

    private static final StrMatcher DEFAULT_PREFIX = StrMatcher.stringMatcher("${");
    private static final StrMatcher DEFAULT_SUFFIX = StrMatcher.stringMatcher("}");
    private static final StrMatcher DEFAULT_VALUE_DELIMITER = StrMatcher.stringMatcher(":");

/*    public static void main(String[] args) {
        SubStituteKey sk = new SubStituteKey();
        sk.convert();
    }*/

    public void convert() {

        System.setProperty("GIFT","girl");

        Map valuesMap = new HashMap();
        valuesMap.put("animal", "quick brown fox");
        valuesMap.put("target", "lazy dog");
        valuesMap.put("gift", System.getProperty("GIFT"));

        String templateString = "The ${animal} jumps over the ${target} to find a ${gift:baby}.";
        StrSubstitutor sub = new StrSubstitutor(valuesMap);
        sub.setValueDelimiterMatcher(StrMatcher.stringMatcher(":"));
        String resolvedString = sub.replace(templateString);
        System.out.println("Result : " + resolvedString);


        // Grab all System properties
        // System.setProperty("SERVER_PORT","9090");
        HashMap values = new HashMap();
        Properties systemProperties = System.getProperties();
        for(Map.Entry<Object, Object> x : systemProperties.entrySet()) {
            values.put(x.getKey(),x.getValue());
        }
        sub = new StrSubstitutor(values);
        sub.setValueDelimiterMatcher(StrMatcher.stringMatcher(":"));
        System.out.println("Result : " + sub.replace("This is the application.yaml key -> server.port: ${SERVER_PORT:8080}"));
    }

}
