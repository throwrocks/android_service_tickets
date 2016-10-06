package rocks.athrow.android_service_tickets.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * PreferencesHelper
 */


public class PreferencesHelper {

    private SharedPreferences prefs;

    private static final String FILE_NAME = "rocks.throw.service_tickets.preferences";


    public PreferencesHelper(Context context) {
        prefs = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
    }


    /**
     * Save the specified value to the shared preferences
     *
     * @param key   The key of the value you wish to load
     * @param value The value to store
     */
    public void save(String key, String value) {
        prefs.edit().putString(key, value).apply();
    }


    /**
     * Load the specified value from the shared preferences
     *
     * @param key      The key of the value you wish to load
     * @param defValue The default value to be returned if no value is found
     */
    public String loadString(String key, String defValue) {
        return prefs.getString(key, defValue);
    }
}
