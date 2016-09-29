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

import static org.bouncycastle.asn1.x509.X509ObjectIdentifiers.id;
import static org.junit.Assert.*;
import static rocks.athrow.android_service_tickets.data.API.getNotesByTicket;

@Config(manifest = Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class UnitTest extends Robolectric {
    @Mock
    private Context mContext;
    private APIResponse mServiceTicketsAPIResponse = null;
    private String mTicketId = null;


    private APIResponse getServiceTickets(){
        return API.getAllServiceTickets();
    }

    private JSONArray parseServiceTickets(){
        String responseText = mServiceTicketsAPIResponse.getResponseText();
        return JSONParser.parseServiceTickets(responseText);
    }

    private String getFirstTicketID(){
        String id = null;
        JSONArray jsonArray = parseServiceTickets();
        try{
            JSONObject ticket = jsonArray.getJSONObject(0);
            id = ticket.getString("id");
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
            mServiceTicketsAPIResponse = getServiceTickets();
        }
    }


    @Test
    public void getServiceTicketsFromAPI() throws Exception {
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
        assertTrue(apiResponse.getResponseCode() == 200);
    }

}