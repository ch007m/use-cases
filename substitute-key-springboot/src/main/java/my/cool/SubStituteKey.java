package my.cool;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.text.StrSubstitutor;

/**
 * Created by dabou on 13/07/2017.
 */
public class SubStituteKey {

    public static void main(String[] args) {
        Map valuesMap = new HashMap();
        valuesMap.put("animal", "quick brown fox");
        valuesMap.put("target", "lazy dog");
        String templateString = "The ${animal} jumps over the ${target}.";
        StrSubstitutor sub = new StrSubstitutor(valuesMap);
        String resolvedString = sub.replace(templateString);

        System.out.println("Result : " + resolvedString);
    }

}
