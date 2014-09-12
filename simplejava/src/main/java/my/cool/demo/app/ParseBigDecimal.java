package my.cool.demo.app;

import my.cool.demo.MathParser;

import java.util.Calendar;
import java.util.Locale;

public class ParseBigDecimal {

    public static void main(String[] args) {

        Locale locale;
        String pattern;
        char groupingSeparator;
        char decimalSeparator;
        MathParser math;

        try {

            // Belgium
            System.out.println("French Belgian Locale");
            math = new MathParser();
            math.setPrecision(2);

            locale = new Locale("fr","BE");
            pattern = "###,###.###"; // PATTERN DEFINED AS en_US LOCALE
            groupingSeparator = ',';
            decimalSeparator = '.';
            math.parsePattern("123,000.452",locale,pattern,groupingSeparator,decimalSeparator);
            math.parsePattern("-123,000.45",locale,pattern,groupingSeparator,decimalSeparator);
            math.parsePattern("123000.45",locale,pattern,groupingSeparator,decimalSeparator);
            math.parsePattern("-1234",locale,pattern,groupingSeparator,decimalSeparator);
            math.parsePattern("1,234",locale,pattern,groupingSeparator,decimalSeparator);
            System.out.println("########################");

            // UK
            System.out.println("UK Locale");
            math = new MathParser();
            math.setPrecision(2);

            locale = new Locale("en");
            pattern = "###,###.###"; // PATTERN DEFINED AS en_US LOCALE
            //pattern = "###.###,###"; // DOES NOT WORK
            groupingSeparator = '.';
            decimalSeparator = ',';
            math.parsePattern("123.000,452",locale,pattern,groupingSeparator,decimalSeparator);
            math.parsePattern("-123.000,45",locale,pattern,groupingSeparator,decimalSeparator);
            math.parsePattern("123000,45",locale,pattern,groupingSeparator,decimalSeparator);
            math.parsePattern("-1234",locale,pattern,groupingSeparator,decimalSeparator);
            math.parsePattern("1.234",locale,pattern,groupingSeparator,decimalSeparator);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
