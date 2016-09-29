package rocks.athrow.android_service_tickets.data;

import android.net.Uri;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import rocks.athrow.android_service_tickets.BuildConfig;

/**
 * API
 * Created by joselopez on 9/21/16.
 */
public final class API {
    private static String API_HOST = BuildConfig.API_HOST;
    private static String API_KEY = BuildConfig.API_KEY;
    private static String API_SERVICE_TICKETS_ALL =  API_HOST + "/layout/service_tickets.json?RFMkey=" + API_KEY;
    private static String API_SERVICE_NOTES_BY_TICKET = API_HOST +
            "/script/api_getNotes/service_ticket_notes.json?RFMkey=" + API_KEY +
            "?RFMScriptParam=";

    private API() {
        throw new AssertionError("No API instances for you!");
    }

    public static APIResponse getAllServiceTickets(){
        return httpConnect(API_SERVICE_TICKETS_ALL, "GET");
    }

    public static APIResponse getNotesByTicket(String id){
        String url = API_SERVICE_NOTES_BY_TICKET + id;
        return httpConnect(url, "GET");
    }

    private static APIResponse httpConnect(String queryURL, String requestMethod){
        APIResponse apiResponse = new APIResponse();
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        try {
            // Build the URL
            Uri builtUri = Uri.parse(queryURL).buildUpon().build();
            URL url = new URL(builtUri.toString());
            // Establish the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(requestMethod);
            urlConnection.connect();
            apiResponse.setResponseCode(urlConnection.getResponseCode());
            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder buffer = new StringBuilder();
            if (inputStream == null) {
                return apiResponse;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            if (buffer.length() == 0) {
                return apiResponse;
            }
            apiResponse.setResponseText(buffer.toString());
        } catch (IOException v) {
            apiResponse.setResponseText(v.toString());
            return apiResponse;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return apiResponse;
    }

}
