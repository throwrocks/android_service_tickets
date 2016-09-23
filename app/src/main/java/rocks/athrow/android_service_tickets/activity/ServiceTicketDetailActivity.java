package rocks.athrow.android_service_tickets.activity;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import rocks.athrow.android_service_tickets.R;
import rocks.athrow.android_service_tickets.data.ServiceTicket;
import rocks.athrow.android_service_tickets.util.Utilities;

public class ServiceTicketDetailActivity extends AppCompatActivity {

    public TextView ticketOrg;
    public TextView ticketSite;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_ticket_detail);
        Intent intent = getIntent();
        Bundle arguments = intent.getExtras();
        // Set the variables
        final String ticketId = arguments.getString(ServiceTicket.ID);
        final String serialNumber = arguments.getString(ServiceTicket.SERIAL_NUMBER);
        final String priority = arguments.getString(ServiceTicket.PRIORITY);
        final String status = arguments.getString(ServiceTicket.STATUS);
        final String technician = arguments.getString(ServiceTicket.TECH_NAME);
        final String createdDate = arguments.getString(ServiceTicket.CREATED_DATE);
        final String assignedDate = arguments.getString(ServiceTicket.ASSIGNED_DATE);
        final String closedDate = arguments.getString(ServiceTicket.CLOSED_DATE);
        final String org = arguments.getString(ServiceTicket.ORG);
        final String site = arguments.getString(ServiceTicket.SITE);
        final String site_address = arguments.getString(ServiceTicket.SITE_ADDRESS);
        final String site_phone = arguments.getString(ServiceTicket.SITE_PHONE);
        final String description = arguments.getString(ServiceTicket.DESCRIPTION);
        final String issues = arguments.getString(ServiceTicket.ISSUES);

        ticketOrg = (TextView) findViewById(R.id.org);
        ticketSite = (TextView) findViewById(R.id.site);

        ticketOrg.setText(org);
        ticketSite.setText(site);

    }
}
