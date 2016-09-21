package rocks.athrow.android_service_tickets.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

import org.json.JSONArray;

import rocks.athrow.android_service_tickets.data.JSONParser;
import rocks.athrow.android_service_tickets.data.UpdateDatabase;

/**
 * Created by joselopez on 9/21/16.
 */
public class UpdateDBService extends IntentService{
    public UpdateDBService(){
        super("UpdateServiceTicketsBroadcast");
    }

    @Override
    protected void onHandleIntent(Intent workIntent){
        Bundle arguments = workIntent.getExtras();
        String JSON = arguments.getString("serviceTicketsJSON");
        JSONArray jsonArray = JSONParser.parseServiceTickets(JSON);
        UpdateDatabase update = new UpdateDatabase(getApplicationContext());
        update.updateServiceTickets(jsonArray);
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent("UpdateServiceTicketsBroadcast"));

    }
}
