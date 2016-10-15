package rocks.athrow.android_service_tickets.data;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Ticket
 * Created by joselopez on 9/20/16.
 */


public class Ticket extends RealmObject {

    public final static String ID = "id";
    final static String TYPE = "type";
    public final static String DESCRIPTION = "description";
    public final static String SERIAL_NUMBER = "serial_number";
    public final static String ORG = "org";
    public final static String SITE = "site";
    public final static String SITE_ADDRESS = "site_address";
    public final static String SITE_PHONE = "site_phone";
    public final static String CREATED_DATE = "created_date";
    public final static String ASSIGNED_DATE = "assigned_date";
    public final static String CLOSED_DATE = "closed_date";
    public final static String TECH_ID = "tech_id";
    public final static String TECH_NAME = "tech_name";
    final static String USER_ID = "user_id";
    final static String USER_NAME = "user_name";
    public final static String PRIORITY = "priority";
    public final static String ISSUES = "issues_csv";
    public final static String STATUS = "status";
    final static String STARTED_ON = "last_started_on";
    final static String STOPPED_ON = "last_stopped_on";
    final static String TIME_SPENT = "time_spent";
    public final static String TIME_TRACK_STATUS = "time_track_status";


    @PrimaryKey
    private String id;
    private int serial_number;
    private String description;
    private int org;
    private String site;
    private String site_address;
    private String site_phone;
    private Date created_date;
    private Date assigned_date;
    private Date closed_date;
    private String tech_name;
    private String priority;
    private String issues;
    private String status;
    private String progress_display;

    public Ticket(){

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

    void setSerial_number(int serial_number) {
        this.serial_number = serial_number;
    }

    public String getDescription() {
        return description;
    }

    void setDescription(String description) {
        this.description = description;
    }

    public int getOrg() {
        return org;
    }

    public void setOrg(int org) {
        this.org = org;
    }

    public String getSite() {
        return site;
    }

    void setSite(String site) {
        this.site = site;
    }

    public String getSite_address() {
        return site_address;
    }

    void setSite_address(String site_address) {
        this.site_address = site_address;
    }

    public String getSite_phone() {
        return site_phone;
    }

    void setSite_phone(String site_phone) {
        this.site_phone = site_phone;
    }

    public Date getCreated_date() {
        return created_date;
    }

    void setCreated_date(Date created_date) {
        this.created_date = created_date;
    }

    public Date getAssigned_date() {
        return assigned_date;
    }

    void setAssigned_date(Date assigned_date) {
        this.assigned_date = assigned_date;
    }

    public Date getClosed_date() {
        return closed_date;
    }

    void setClosed_date(Date closed_date) {
        this.closed_date = closed_date;
    }

    public String getTech_name() {
        return tech_name;
    }

    void setTech_name(String tech_name) {
        this.tech_name = tech_name;
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

    void setIssues(String issues) {
        this.issues = issues;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProgress_display() {
        return progress_display;
    }

    public void setProgress_display(String progress_display) {
        this.progress_display = progress_display;
    }
}
