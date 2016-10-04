package rocks.athrow.android_service_tickets.data;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * ServiceTicketNote
 * Created by joselopez on 9/20/16.
 */
@SuppressWarnings({"All"})
public class ServiceTicketNote extends RealmObject {
    final static String ID = "id";
    public final static String SERIAL_NUMBER = "serial_number";
    public final static String SERVICE_TICKET_ID = "service_ticket_id";
    final static String NOTE = "note";
    final static String CREATION_DATE = "creation_date";
    final static String CREATED_BY ="created_by";

    @PrimaryKey
    private String id;
    private int serial_number;
    private String service_ticket_id;
    private String note;
    private Date creation_date;
    private String created_by;

    public ServiceTicketNote(){

    }

    public void setSerial_number(int serial_number) {
        this.serial_number = serial_number;
    }

    public void setId(String id) {
        this.id = id;
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
