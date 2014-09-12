package my.cool.demo;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class CalendarUtil {

    public static Calendar convertToGmt(Calendar cal, String timeZone) {

        Date date = cal.getTime();
        TimeZone tz = cal.getTimeZone();

        System.out.println("Input calendar has date [" + date + "] for Time Zone : '" + tz.getDisplayName() + "'");

        // Create a new calendar in GMT timezone, set to this date and add the offset
        Calendar cl = new java.util.GregorianCalendar(TimeZone.getTimeZone(timeZone));
        cl.setTime(date);

        System.out.println("Created '" + timeZone + "' cal with date [" + cl.getTime() + "]");

        return cal;
    }
}
