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
    private Context mContext;
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
                ServiceTicket serviceTicket = new ServiceTicket();
                JSONObject record = jsonArray.getJSONObject(i);
                // Parse the JSON Object
                String id = record.getString(ServiceTicket.ID);
                int serialNumber = record.getInt(ServiceTicket.SERIAL_NUMBER);
                String type = record.getString(ServiceTicket.TYPE);
                String description = record.getString(ServiceTicket.DESCRIPTION);
                int org = record.getInt(ServiceTicket.ORG);
                String site = record.getString(ServiceTicket.SITE);
                String site_address = record.getString(ServiceTicket.SITE_ADDRESS);
                String site_phone = record.getString(ServiceTicket.SITE_PHONE);
                Date createdDate = Utilities.getStringAsDate(record.getString(ServiceTicket.CREATED_DATE), DATE_FORMAT, null);
                Date assignedDate = Utilities.getStringAsDate(record.getString(ServiceTicket.ASSIGNED_DATE), DATE_FORMAT, null);
                Date closedDate = Utilities.getStringAsDate(record.getString(ServiceTicket.CLOSED_DATE), DATE_FORMAT, null);
                int techId = record.getInt(ServiceTicket.TECH_ID);
                String techName = record.getString(ServiceTicket.TECH_NAME);
                int userId = record.getInt(ServiceTicket.USER_ID);
                String userName = record.getString(ServiceTicket.USER_NAME);
                String priority = record.getString(ServiceTicket.PRIORITY);
                String issues = record.getString(ServiceTicket.ISSUES);
                String status = record.getString(ServiceTicket.STATUS);
                // Create the service ticket object (Realm Object)
                serviceTicket.setId(id);
                serviceTicket.setSerial_number(serialNumber);
                serviceTicket.setType(type);
                serviceTicket.setDescription(description);
                serviceTicket.setOrg(org);
                serviceTicket.setSite(site);
                serviceTicket.setSite_address(site_address);
                serviceTicket.setSite_phone(site_phone);
                serviceTicket.setCreated_date(createdDate);
                serviceTicket.setAssigned_date(assignedDate);
                serviceTicket.setClosed_date(closedDate);
                serviceTicket.setTech_id(techId);
                serviceTicket.setTech_name(techName);
                serviceTicket.setUser_id(userId);
                serviceTicket.setUser_name(userName);
                serviceTicket.setPriority(priority);
                serviceTicket.setIssues(issues);
                serviceTicket.setStatus(status);
                // Save to the database and close the transaction
                realm.copyToRealmOrUpdate(serviceTicket);
                realm.commitTransaction();
                realm.close();
            }catch (JSONException e){
                realm.cancelTransaction();
                e.printStackTrace();
            }
        }

    }

    // TODO: Implemente update notes method
    public void updateNotes(JSONArray jsonArray){

    }

}
