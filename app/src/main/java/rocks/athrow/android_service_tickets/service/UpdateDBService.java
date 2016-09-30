package rocks.athrow.android_service_tickets.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

import org.json.JSONArray;

import rocks.athrow.android_service_tickets.data.JSONParser;
import rocks.athrow.android_service_tickets.data.UpdateDatabase;

/**
 * UpdateDBService
 * Created by joselopez on 9/21/16.
 */
public class UpdateDBService extends IntentService{
    public static final String SERVICE_NAME = "UpdateDBService";
    public static final String UPDATE_TICKETS_DB_SERVICE_BROADCAST = "UpdateServiceTicketsBroadcast";
    public static final String UPDATE_NOTES_DB_SERVICE_BROADCAST = "UpdateNotesBroadcast";
    public static final String TYPE_TICKETS = "tickets";
    public static final String TYPE_NOTES = "notes";
    public static final String DATA = "JSON";
    public static final String TYPE = "type";

    public UpdateDBService(){
        super(SERVICE_NAME);
    }

    @Override
    protected void onHandleIntent(Intent workIntent){
        Bundle arguments = workIntent.getExtras();
        String type = arguments.getString(TYPE);
        String JSON = arguments.getString(DATA);
        if ( type != null && type.equals(TYPE_TICKETS)){
            JSONArray jsonArray = JSONParser.parseServiceTickets(JSON);
            UpdateDatabase update = new UpdateDatabase(getApplicationContext());
            update.updateServiceTickets(jsonArray);
            LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(UPDATE_TICKETS_DB_SERVICE_BROADCAST));
        }else if ( type != null && type.equals(TYPE_NOTES)){
            // TODO: Implement notes update
            //JSONArray jsonArray = JSONParser.parseServiceTickets(JSON);
            //UpdateDatabase update = new UpdateDatabase(getApplicationContext());
            //update.updateServiceTickets(jsonArray);
            //LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(UPDATE_TICKETS_DB_SERVICE_BROADCAST));
        }


    }
}
