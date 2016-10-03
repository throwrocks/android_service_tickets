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

import rocks.athrow.android_service_tickets.data.API;
import rocks.athrow.android_service_tickets.data.APIResponse;
import rocks.athrow.android_service_tickets.data.JSONParser;
import rocks.athrow.android_service_tickets.data.ServiceTicket;
import rocks.athrow.android_service_tickets.util.Utilities;

import static org.junit.Assert.*;

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
            id = ticket.getString(ServiceTicket.ID);
        }catch (JSONException e){
            System.out.println(e);
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
        APIResponse apiResponse = API.createNote(id, 79842, "A unit test note from Android!");
        int responseCode = apiResponse.getResponseCode();
        assertTrue(responseCode == 200);
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