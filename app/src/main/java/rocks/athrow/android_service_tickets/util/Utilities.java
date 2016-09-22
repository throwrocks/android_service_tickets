package rocks.athrow.android_service_tickets.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by joselopez on 9/21/16.
 */
public final class Utilities {

    private Utilities(){ throw new AssertionError("No Utilities instances for you!"); } // suppress constructor


    /**
     * getStringAsDate
     *
     * @param dateString a string in date format
     * @param format     the resulting date format
     * @return a new date in the specified format
     */
    public static Date getStringAsDate(String dateString, String format, String timezone) {
        SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.getDefault());
        if (timezone == null) {
            formatter.setTimeZone(TimeZone.getDefault());
        } else {
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        }
        Date date = new Date();
        try {
            date = formatter.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String getBulletedList(String string, String separator){
        String[] x = string.split(separator);
        String result = null;
        int count = x.length;
        int i = 0;
        while ( i < count ){
            if ( i == 0 ){
               result =  "∙ " + x[0];
            }else{
                result = result + "\n∙ " + x[i];
            }
            i ++;
        }
        return result;
    }

}
