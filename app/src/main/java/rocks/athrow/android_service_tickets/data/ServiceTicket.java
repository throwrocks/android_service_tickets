package rocks.athrow.android_service_tickets.data;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * ServiceTicket
 * Created by joselopez on 9/20/16.
 */
public class ServiceTicket extends RealmObject {

    public final static String ID = "id";
    public final static String TYPE = "type";
    public final static String DESCRIPTION = "description";
    public final static String SERIAL_NUMBER = "serial_number";
    public final static String ORG = "org";
    public final static String CREATED_DATE = "created_date";
    public final static String CLOSED_DATE = "closed_date";
    public final static String TECH_ID = "tech_id";
    public final static String TECH_NAME = "tech_name";
    public final static String USER_ID = "user_id";
    public final static String USER_NAME = "user_name";
    public final static String PRIORITY = "priority";
    public final static String ISSUES = "issues_csv";
    public final static String STATUS = "status";


    @PrimaryKey
    String id;
    int serial_number;
    String type;
    String description;
    int org;
    Date created_date;
    Date closed_date;
    int tech_id;
    String tech_name;
    int user_id;
    String user_name;
    String priority;
    String issues;
    String status;

    public ServiceTicket(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getSerial_number() {
        return serial_number;
    }

    public void setSerial_number(int serial_number) {
        this.serial_number = serial_number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getOrg() {
        return org;
    }

    public void setOrg(int org) {
        this.org = org;
    }

    public Date getCreated_date() {
        return created_date;
    }

    public void setCreated_date(Date created_date) {
        this.created_date = created_date;
    }

    public Date getClosed_date() {
        return closed_date;
    }

    public void setClosed_date(Date closed_date) {
        this.closed_date = closed_date;
    }

    public int getTech_id() {
        return tech_id;
    }

    public void setTech_id(int tech_id) {
        this.tech_id = tech_id;
    }

    public String getTech_name() {
        return tech_name;
    }

    public void setTech_name(String tech_name) {
        this.tech_name = tech_name;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getIssues() {
        return issues;
    }

    public void setIssues(String issues) {
        this.issues = issues;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
