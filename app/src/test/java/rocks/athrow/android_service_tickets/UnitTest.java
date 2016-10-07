package rocks.athrow.android_service_tickets;


import android.content.Context;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.concurrent.TimeUnit;

import rocks.athrow.android_service_tickets.data.API;
import rocks.athrow.android_service_tickets.data.APIResponse;
import rocks.athrow.android_service_tickets.data.JSONParser;
import rocks.athrow.android_service_tickets.data.Ticket;
import rocks.athrow.android_service_tickets.util.Utilities;

import static junit.framework.Assert.assertTrue;

@Config(manifest = Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class UnitTest extends Robolectric {
    @Mock
    private Context mContext;
    private APIResponse mServiceTicketsAPIResponse = null;

    private APIResponse getOpenServiceTickets(){
        return API.getOpenServiceTickets();
    }

    private JSONArray parseServiceTickets(){
        String responseText = mServiceTicketsAPIResponse.getResponseText();
        return JSONParser.getJSONArray(responseText);
    }

    private String getFirstTicketID(){
        String id = null;
        JSONArray jsonArray = parseServiceTickets();
        try{
            JSONObject ticket = jsonArray.getJSONObject(0);
            id = ticket.getString(Ticket.ID);
        }catch (JSONException e){
            e.printStackTrace();
        }
        return id;
    }

    @Before
    public void setUp() throws Exception {
        if ( mContext == null ){
            mContext = RuntimeEnvironment.application.getApplicationContext();
        }
        if ( mServiceTicketsAPIResponse == null ){
            mServiceTicketsAPIResponse = getOpenServiceTickets();
        }
    }

    @Test
    public void validateKey(){
        APIResponse apiResponse = API.validateKey(BuildConfig.API_USER_KEY);
        assertTrue(apiResponse.getResponseCode() == 200);
    }


    @Test
    public void getOpenServiceTicketsFromAPI() throws Exception {
        int responseCode = mServiceTicketsAPIResponse.getResponseCode();
        assertTrue(responseCode == 200);
    }

    @Test
    public void getServiceTicketJSON() throws Exception {
        JSONArray jsonArray = parseServiceTickets();
        assertTrue(jsonArray != null);
    }

    @Test
    public void getServiceNote() throws Exception {
        String id = getFirstTicketID();
        APIResponse apiResponse = API.getNotesByTicket(id);
        int responseCode = apiResponse.getResponseCode();
        assertTrue(responseCode == 200 || responseCode == 500);
    }

    @Test
    public void createNote() throws Exception {
        String id = getFirstTicketID();
        APIResponse apiResponse = API.createNote(id, BuildConfig.EMPLOYEE_ID, BuildConfig.EMPLOYEE_NAME, "A unit test note from Android!");
        int responseCode = apiResponse.getResponseCode();
        assertTrue(responseCode == 200);
    }

    @Test
    public void closeTicket() throws Exception {
        String id = getFirstTicketID();
        APIResponse apiResponse = API.closeTicket(id, BuildConfig.EMPLOYEE_NAME, Utilities.getDateAsString(new java.util.Date(),"MM/dd/YYYY hh:mm:ss a",null));
        int responseCode = apiResponse.getResponseCode();
        assertTrue(responseCode == 200);
    }

    @Test
    public void trackTime() throws Exception {
        String id = getFirstTicketID();
        APIResponse apiResponse;
        apiResponse = API.trackTime(id, BuildConfig.EMPLOYEE_NAME, "Start Time", Utilities.getDateAsString(new java.util.Date(),"MM/dd/YYYY hh:mm:ss a",null));
        assertTrue(apiResponse.getResponseCode() == 200);
        // Wait a minute
        TimeUnit.SECONDS.sleep(10);
        apiResponse = API.trackTime(id, BuildConfig.EMPLOYEE_NAME, "Stop Time", Utilities.getDateAsString(new java.util.Date(),"MM/dd/YYYY hh:mm:ss a",null));
        assertTrue(apiResponse.getResponseCode() == 200);

    }

    @Test
    public void testPadLeft(){
        String result;
        result = Utilities.padLeft("1","0",3);
        assertTrue(result.length() == 3);
        result = Utilities.padLeft("12","0",3);
        assertTrue(result.length() == 3);
        result = Utilities.padLeft("123","0",3);
        assertTrue(result.length() == 3);
    }

}