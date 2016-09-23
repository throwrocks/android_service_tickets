package rocks.athrow.android_service_tickets;


import android.content.Context;

import org.json.JSONArray;
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

import static org.junit.Assert.*;

@Config(manifest = Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class UnitTest extends Robolectric {
    @Mock
    Context mContext;

    APIResponse mAPIResponse = null;

    private void getServiceTickets(){
        mAPIResponse = API.getAllServiceTickets();
    }

    @Before
    public void setUp() throws Exception {
        if ( mContext == null ){
            mContext = RuntimeEnvironment.application.getApplicationContext();
        }
        if ( mAPIResponse == null ){
            getServiceTickets();
        }
    }


    @Test
    public void getServiceTicketsFromAPI() throws Exception {
        int responseCode = mAPIResponse.getResponseCode();
        assertTrue(responseCode == 200);
    }

    @Test
    public void getServiceTicketJSON() throws Exception {
        String responseText = mAPIResponse.getResponseText();
        JSONArray jsonArray = JSONParser.parseServiceTickets(responseText);
        assertTrue(jsonArray != null);
    }


}