package my.cool.demo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class MathParser {

    private int precision = 2;
    private DecimalFormatSymbols symbols;
    private char groupSeparatorChar;
    private String groupSeparator;
    private String fixedString;

    public void parsePattern(String formattedString,
                             Locale locale,
                             String my_pattern,
                             char groupingSeparator,
                             char decimalSeparator) throws ParseException {

        Locale.setDefault(locale);
        DecimalFormat df;

        // Define separator and grouping
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(locale);
        symbols.setDecimalSeparator(decimalSeparator);
        symbols.setGroupingSeparator(groupingSeparator);

        // Create a NumberFormat
        df = (DecimalFormat) NumberFormat.getInstance(locale);
        df.setParseBigDecimal(true);
        df.setDecimalFormatSymbols(symbols);
        df.applyPattern(my_pattern);
        //df.applyLocalizedPattern(pattern);

        // Parse the String to BigDecimal
        BigDecimal bigDecimal = (BigDecimal) df.parse(formattedString);
        bigDecimal = bigDecimal.setScale(2,BigDecimal.ROUND_CEILING);

        // Format it according to the Locale
        //System.out.println(df.format(bigDecimal));

        // BigDecimal result (not formatted)
        System.out.println("Result : " + bigDecimal);
        Locale.getDefault();
    }

    public void parseGroupingDecimal(String formattedString,
                                     Locale locale,
                                     String pattern,
                                     char groupingSeparator,
                                     char decimalSeparator) throws ParseException {
        symbols = new DecimalFormatSymbols(locale);
        groupSeparatorChar = symbols.getGroupingSeparator();

        if (groupSeparatorChar == ',') {
            groupSeparator = "\\" + groupSeparatorChar;
        } else {
            groupSeparator = Character.toString(groupSeparatorChar);
        }

        fixedString = formattedString.replaceAll(groupSeparator, "");
        BigDecimal bd = new BigDecimal(fixedString);
        bd.setScale(precision);
        System.out.println(bd.toString());
    }

    public void setPrecision(int precision) {
        this.precision = precision;
    }

    protected int getPrecision() {
        return precision;
    }

}
