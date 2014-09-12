package my.cool.demo.app;

import my.cool.demo.CalendarUtil;
import my.cool.demo.MathParser;

import java.util.Calendar;
import java.util.Locale;

public class ConvertTimeCalendar {

    private static final String TIME_ZONE = "IST";
    public static void main(String[] args) {

        Calendar result = CalendarUtil.convertToGmt(Calendar.getInstance(),TIME_ZONE);
        System.out.println("Date : " + result.getTime());
        System.out.println("########################");

    }

}
