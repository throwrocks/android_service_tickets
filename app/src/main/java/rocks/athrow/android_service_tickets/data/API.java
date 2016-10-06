package rocks.athrow.android_service_tickets.data;

import android.net.Uri;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import rocks.athrow.android_service_tickets.BuildConfig;

/**
 * API
 * Created by joselopez on 9/21/16.
 */
public final class API {
    private static final String API_HOST = BuildConfig.API_HOST;
    private static final String API_KEY = BuildConfig.API_KEY;
    private static final String API_SERVICE_TICKETS =  API_HOST + "/layout/service_tickets.json?RFMkey=" + API_KEY;
    private static final String API_SERVICE_NOTES_BY_TICKET = API_HOST + "/layout/service_ticket_notes.json?RFMkey=" + API_KEY;
    private static final String API_CREATE_NOTE = API_HOST + "/script/api_createNote/service_ticket_notes.json?RFMkey=" + API_KEY;
    private static final String API_CLOSE_TICKET = API_HOST + "/script/api_closeTicket/service_tickets.json?RFMkey=" + API_KEY;
    private static final String API_VALIDATE_KEY = API_HOST + "/script/api_validateKey/api_keys.json?RFMkey=" + API_KEY;

    private API() {
        throw new AssertionError("No API instances for you!");
    }

// --Commented out by Inspection START (10/3/2016 8:49 PM):
//    public static APIResponse getAllServiceTickets(){
//        return httpConnect(API_SERVICE_TICKETS);
//    }
// --Commented out by Inspection STOP (10/3/2016 8:49 PM)

    public static APIResponse validateKey(String key){
        APIResponse apiResponse = new APIResponse();
        try {
            String url = API_VALIDATE_KEY +  "&RFMscriptParam=" +  URLEncoder.encode(key, "UTF-8");
            apiResponse = httpConnect(url);
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
            apiResponse.setResponseCode(1270);
        }
        return  apiResponse;

    }

    public static APIResponse getOpenServiceTickets(){
        String url = API_SERVICE_TICKETS + "&RFMsF1=" + ServiceTicket.STATUS + "&RFMsV1=open";
        return httpConnect(url);
    }

    public static APIResponse getNotesByTicket(String id){
        String url = API_SERVICE_NOTES_BY_TICKET + "&RFMsF1=" + ServiceTicketNote.SERVICE_TICKET_ID + "&RFMsV1=" + id;
        return httpConnect(url);
    }

    /**
     * createNote
     * @param ticketId the service ticket id
     * @param employeeNumber the employee number
     * @param note the note
     * @return an APIResponse object
     */
    public static APIResponse createNote(String ticketId, int employeeNumber, String employeeName, String note){
        APIResponse apiResponse = new APIResponse();
        String params = ticketId + "|" + employeeNumber + "|" + employeeName + "|" + note;
        try {
            String url = API_CREATE_NOTE + "&RFMscriptParam=" +  URLEncoder.encode(params, "UTF-8");
            apiResponse = httpConnect(url);
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
            apiResponse.setResponseCode(1270);
        }
        return apiResponse;
    }


    public static APIResponse closeTicket(String id){
        String url = API_CLOSE_TICKET + "&RFMscriptParam=" + id;
        return httpConnect(url);
    }

    private static APIResponse httpConnect(String queryURL){
        APIResponse apiResponse = new APIResponse();
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        try {
            // Build the URL
            Uri builtUri = Uri.parse(queryURL).buildUpon().build();
            URL url = new URL(builtUri.toString());
            // Establish the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
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
