package rocks.athrow.android_service_tickets.data;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import rocks.athrow.android_service_tickets.util.Utilities;

/**
 * UpdateDatabase
 * Created by joselopez on 9/21/16.
 */
public class UpdateDatabase {
    private final Context mContext;
    private final static String DATE_FORMAT = "MM/dd/yyyy";
    public UpdateDatabase(Context mContext) {
        this.mContext = mContext;
    }

    public void updateServiceTickets(JSONArray jsonArray){
        int count = jsonArray.length();
        for (int i = 0; i < count ; i++) {
            RealmConfiguration realmConfig = new RealmConfiguration.Builder(mContext).build();
            Realm.setDefaultConfiguration(realmConfig);
            Realm realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            try {
                Ticket ticket = new Ticket();
                JSONObject record = jsonArray.getJSONObject(i);
                // Parse the JSON Object
                String id = record.getString(Ticket.ID);
                int serialNumber = record.getInt(Ticket.SERIAL_NUMBER);
                String type = record.getString(Ticket.TYPE);
                String description = record.getString(Ticket.DESCRIPTION);
                int org = record.getInt(Ticket.ORG);
                String site = record.getString(Ticket.SITE);
                String site_address = record.getString(Ticket.SITE_ADDRESS);
                String site_phone = record.getString(Ticket.SITE_PHONE);
                Date createdDate = Utilities.getStringAsDate(record.getString(Ticket.CREATED_DATE), DATE_FORMAT, null);
                String assignedDateString = record.getString(Ticket.ASSIGNED_DATE);
                Date assignedDate = null;
                if ( !assignedDateString.equals("")) {
                    assignedDate = Utilities.getStringAsDate(record.getString(Ticket.ASSIGNED_DATE), DATE_FORMAT, null);
                }
                Date closedDate = Utilities.getStringAsDate(record.getString(Ticket.CLOSED_DATE), DATE_FORMAT, null);
                int techId = record.getInt(Ticket.TECH_ID);
                String techName = record.getString(Ticket.TECH_NAME);
                String priority = record.getString(Ticket.PRIORITY);
                String issues = record.getString(Ticket.ISSUES);
                String status = record.getString(Ticket.STATUS);
                String last_started_on = record.getString(Ticket.STARTED_ON);
                String last_stopped_on = record.getString(Ticket.STOPPED_ON);
                // Create the service ticket object (Realm Object)
                ticket.setId(id);
                ticket.setSerial_number(serialNumber);
                ticket.setDescription(description);
                ticket.setOrg(org);
                ticket.setSite(site);
                ticket.setSite_address(site_address);
                ticket.setSite_phone(site_phone);
                ticket.setCreated_date(createdDate);
                ticket.setAssigned_date(assignedDate);
                ticket.setClosed_date(closedDate);
                ticket.setTech_id(techId);
                ticket.setTech_name(techName);
                ticket.setPriority(priority);
                ticket.setIssues(issues);
                ticket.setStatus(status);
                ticket.setLast_started_on(last_started_on);
                ticket.setLast_stopped_on(last_stopped_on);
                // Save to the database and close the transaction
                realm.copyToRealmOrUpdate(ticket);
                realm.commitTransaction();
                realm.close();
            }catch (JSONException e){
                realm.cancelTransaction();
                e.printStackTrace();
            }
        }

    }

    public void updateNotes(JSONArray jsonArray){
        int count = jsonArray.length();
        for (int i = 0; i < count ; i++) {
            RealmConfiguration realmConfig = new RealmConfiguration.Builder(mContext).build();
            Realm.setDefaultConfiguration(realmConfig);
            Realm realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            try {
                TicketNote ticketNote = new TicketNote();
                JSONObject record = jsonArray.getJSONObject(i);
                // Parse the JSON Object
                String id = record.getString(TicketNote.ID);
                int serial_number = record.getInt(TicketNote.SERIAL_NUMBER);
                String service_ticket_id = record.getString(TicketNote.SERVICE_TICKET_ID);
                String note = record.getString(TicketNote.NOTE);
                Date creation_date = Utilities.getStringAsDate(record.getString(TicketNote.CREATION_DATE), DATE_FORMAT, null);
                String created_by = record.getString(TicketNote.CREATED_BY);
                // Create the service ticket object (Realm Object)
                ticketNote.setId(id);
                ticketNote.setSerial_number(serial_number);
                ticketNote.setService_ticket_id(service_ticket_id);
                ticketNote.setNote(note);
                ticketNote.setCreation_date(creation_date);
                ticketNote.setCreated_by(created_by);
                // Save to the database and close the transaction
                realm.copyToRealmOrUpdate(ticketNote);
                realm.commitTransaction();
                realm.close();
            }catch (JSONException e){
                realm.cancelTransaction();
                e.printStackTrace();
            }
        }

    }

}
