package rocks.athrow.android_service_tickets.data;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by joselopez on 9/20/16.
 */
public class ServiceTicketNote extends RealmObject {
    @PrimaryKey
    String id;
    String service_ticket_id;
    String note;
    Date creation_date;
    String created_by;

    public ServiceTicketNote(){

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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Date getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(Date creation_date) {
        this.creation_date = creation_date;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }
}
