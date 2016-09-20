package rocks.athrow.android_service_tickets.data;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by joselopez on 9/20/16.
 */
public class ServiceTicketContact extends RealmObject {
    @PrimaryKey
    String id;
    String service_ticket_id;
    int org;
    String name;
    String position;
    String email;
    String type;
    Date created_date;

    public ServiceTicketContact(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getService_ticket_id() {
        return service_ticket_id;
    }

    public void setService_ticket_id(String service_ticket_id) {
        this.service_ticket_id = service_ticket_id;
    }

    public int getOrg() {
        return org;
    }

    public void setOrg(int org) {
        this.org = org;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getCreated_date() {
        return created_date;
    }

    public void setCreated_date(Date created_date) {
        this.created_date = created_date;
    }
}
