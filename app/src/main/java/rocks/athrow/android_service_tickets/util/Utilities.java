package rocks.athrow.android_service_tickets.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import rocks.athrow.android_service_tickets.R;
/**
 * Utilities
 * Created by joselopez on 9/21/16.
 */
public final class Utilities {
    public static final String EMPLOYEE_KEY = "key";
    public static final String EMPLOYEE_ID = "employee_number";
    public static final String EMPLOYEE_NAME = "name";
    public static final String NULL = "null";
    public static final String FMDataFormat =  "MM/dd/yyyy hh:mm:ss a";

    private Utilities() {
        throw new AssertionError("No Utilities instances for you!");
    } // suppress constructor


    /**
     * getStringAsDate
     *
     * @param dateString a string in date format
     * @param format     the resulting date format
     * @return a new date in the specified format
     */
    @SuppressWarnings("SameParameterValue")
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

    /**
     * getDateAsString
     * Convert a date into a string
     *
     * @param date   the date
     * @param format the format in which to return the string
     * @return the new formatted date string
     */
    @SuppressWarnings("SameParameterValue")
    public static String getDateAsString(Date date, String format, String timezone) {
        DateFormat formatter = new SimpleDateFormat(format, Locale.getDefault());
        if (timezone == null) {
            formatter.setTimeZone(TimeZone.getDefault());
        } else {
            formatter.setTimeZone(TimeZone.getTimeZone(timezone));
        }
        return formatter.format(date);
    }


    /**
     * getBulletedList
     *
     * @param string    pass a value delimited list string
     * @param separator the separator / delimiter
     * @return a bulleted list
     */
    @SuppressWarnings("SameParameterValue")
    public static String getBulletedList(String string, String separator, int type) {
        String newSeparator;
        if (type == 1) {
            newSeparator = "\n∙ ";
        } else {
            newSeparator = " ∙ ";
        }
        String[] x = string.split(separator);
        String result = null;
        int count = x.length;
        int i = 0;
        while (i < count) {
            if (i == 0) {
                result = "∙ " + x[0];
            } else {
                result = result + newSeparator + x[i];
            }
            i++;
        }
        return result;
    }

    /**
     * isConnected
     * This method is used to check for network connectivity before attempting a network call
     *
     * @param context the activity from where the method is called
     * @return true for is connected and false for is not connected
     */
    public static boolean isConnected(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    /**
     * formatStatusView
     *
     * @param view the ticket status view
     * @param status           the ticket's status (open or closed)
     * @param context          a context object
     */

    public static void formatStatusView(TextView view, String status, Context context) {
        switch (status) {
            case "Open":
                view.setBackground(ContextCompat.getDrawable(context, R.drawable.badge_status_open));
                break;
            case "Closed":
                view.setBackground(ContextCompat.getDrawable(context, R.drawable.badge_status_closed));
                break;
        }

    }

    /**
     * formatPriorityView
     *
     * @param view the ticket's priority view
     * @param priority           the ticket's priority (low, medium, high)
     * @param context            a context object
     */
    public static void formatPriorityView(TextView view, String priority, Context context) {

        switch (priority) {
            case "High":
                view.setText("H");
                view.setTextColor(ContextCompat.getColor(context, R.color.white));
                view.
                        setBackground(ContextCompat.getDrawable(context, R.drawable.badge_high));
                break;
            case "Medium":
                view.setText("M");
                view.setTextColor(ContextCompat.getColor(context, R.color.white));
                view.setBackground(ContextCompat.getDrawable(context, R.drawable.badge_medium));
                break;
            case "Low":
                view.setText("L");
                view.setTextColor(ContextCompat.getColor(context, R.color.textPrimary));
                view.setBackground(ContextCompat.getDrawable(context, R.drawable.badge_low));
        }

    }

    public static void formatInProgress(TextView view,Context context){
        view.setText("In Progress");
        view.setBackground(ContextCompat.getDrawable(context, R.drawable.badge_in_progress));
    }

    /**
     * padLeft
     *
     * @param s        the string to pad
     * @param fillChar the character the pad the string with
     * @param length   the length of the resulting string
     * @return the padded results
     */
    @SuppressWarnings("SameParameterValue")
    public static String padLeft(String s, String fillChar, int length) {
        if (s.length() == length) {
            return s;
        }
        String result = s;
        int i = s.length();
        while (i < length) {
            result = fillChar + result;
            i++;
        }
        return result;
    }

    @SuppressWarnings("SameParameterValue")
    public static void showToast(Context context, String message, int duration) {
        Toast toast = Toast.makeText(context, message, duration);
        toast.show();
    }

}
