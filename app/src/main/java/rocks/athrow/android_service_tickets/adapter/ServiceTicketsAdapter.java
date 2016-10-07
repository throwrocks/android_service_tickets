package rocks.athrow.android_service_tickets.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import rocks.athrow.android_service_tickets.activity.ServiceTicketDetailActivity;
import rocks.athrow.android_service_tickets.data.Ticket;
import rocks.athrow.android_service_tickets.realmadapter.RealmRecyclerViewAdapter;

import rocks.athrow.android_service_tickets.R;
import rocks.athrow.android_service_tickets.util.Utilities;


/**
 * ServiceTicketsListAdapter
 * Created by joselopez on 9/21/16.
 */
public class ServiceTicketsAdapter extends RealmRecyclerViewAdapter<Ticket> {
    private final static String DATE_FORMAT = "MM/dd/yyyy";
    private final Context mContext;

    private class ViewHolder extends RecyclerView.ViewHolder {
        // Declare views
        final TextView serialNumberView;
        final TextView timeTrackerView;
        final TextView priorityView;
        final TextView statusView;
        final TextView technicianView;
        final TextView createdDateView;
        final TextView assignedDateView;
        final TextView orgView;
        final TextView siteView;
        final TextView siteAddressView;
        final TextView sitePhoneView;
        final TextView issuesView;
        final TextView descriptionView;
        final Button openButtonView;

        ViewHolder(View view) {
            super(view);
            // Initialize views
            serialNumberView = (TextView) view.findViewById(R.id.ticket_number);
            timeTrackerView = (TextView) view.findViewById(R.id.time_track);
            priorityView = (TextView) view.findViewById(R.id.priority);
            statusView = (TextView) view.findViewById(R.id.status);
            technicianView = (TextView) view.findViewById(R.id.technician);
            createdDateView = (TextView) view.findViewById(R.id.created_date);
            assignedDateView = (TextView) view.findViewById(R.id.assigned_date);
            orgView = (TextView) view.findViewById(R.id.org);
            siteView = (TextView) view.findViewById(R.id.site);
            siteAddressView = (TextView) view.findViewById(R.id.site_address);
            sitePhoneView = (TextView) view.findViewById(R.id.site_phone);
            issuesView = (TextView) view.findViewById(R.id.issues);
            descriptionView = (TextView) view.findViewById(R.id.description);
            openButtonView = (Button) view.findViewById(R.id.open_ticket_button);
        }
    }


    public ServiceTicketsAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View serviceTicketCardView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.service_ticket_card, parent, false);
        return new ViewHolder(serviceTicketCardView);

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int position) {
        ViewHolder vh = (ViewHolder) viewHolder;
        Ticket ticket = getItem(position);
        // Set the variables
        final String ticketId = ticket.getId();
        final String last_started_on = ticket.getLast_started_on();
        final String last_stopped_on = ticket.getLast_stopped_on();
        final int timeTrackStatus;
        if ( !last_started_on.equals("") && last_stopped_on.equals("")){
            timeTrackStatus = 1;
        }else{
            timeTrackStatus = 0;
        }
        final String serialNumber = "#" + Integer.toString(ticket.getSerial_number());
        final String priority = ticket.getPriority();
        final String status = ticket.getStatus();
        final String technician = ticket.getTech_name();
        final String createdDate = Utilities.getDateAsString(ticket.getCreated_date(), DATE_FORMAT, null );
        final String assignedDate = Utilities.getDateAsString(ticket.getAssigned_date(), DATE_FORMAT, null );
        final String closedDate = Utilities.getDateAsString(ticket.getClosed_date(), DATE_FORMAT, null );
        final String org = Integer.toString(ticket.getOrg());
        final String orgDisplay = Utilities.padLeft(org, "0", 3);
        final String site = ticket.getSite();
        final String siteAddress = ticket.getSite_address();
        final String sitePhone = ticket.getSite_phone();
        final String description = ticket.getDescription();
        final String issues = ticket.getIssues();
        final String issuesDisplay = Utilities.getBulletedList(issues, ",", 1);
        //String issues = ticket.getIssues().replace(",","\n");
        // Set the views
        vh.serialNumberView.setText(serialNumber);
        if ( timeTrackStatus == 1){
            vh.timeTrackerView.setVisibility(View.VISIBLE);
            Utilities.formatInProgress(vh.timeTrackerView, mContext);
        }
        vh.priorityView.setText(priority);
        vh.statusView.setText(status);
        vh.technicianView.setText(technician);
        vh.createdDateView.setText(createdDate);
        vh.assignedDateView.setText(assignedDate);
        //serviceTicketViewHolder.ticketClosedDate.setText(closedDate);
        vh.orgView.setText(orgDisplay);
        vh.siteView.setText(site);
        vh.siteAddressView.setText(siteAddress);
        vh.sitePhoneView.setText(sitePhone);
        vh.issuesView.setText(issuesDisplay);
        vh.descriptionView.setText(description);

        Utilities.formatPriorityView(vh.priorityView, priority, mContext);
        Utilities.formatStatusView(vh.statusView, status, mContext);

        // Set click listener
        vh.openButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle args = new Bundle();
                args.putString(Ticket.ID, ticketId);
                args.putString(Ticket.SERIAL_NUMBER, serialNumber);
                args.putString(Ticket.PRIORITY, priority);
                args.putString(Ticket.STATUS, status);
                args.putString(Ticket.TECH_NAME, technician);
                args.putString(Ticket.CREATED_DATE, createdDate);
                args.putString(Ticket.ASSIGNED_DATE, assignedDate);
                args.putString(Ticket.CLOSED_DATE, closedDate);
                args.putString(Ticket.ORG, orgDisplay);
                args.putString(Ticket.SITE, site);
                args.putString(Ticket.SITE_ADDRESS, siteAddress);
                args.putString(Ticket.SITE_PHONE, sitePhone);
                args.putString(Ticket.ISSUES, issues);
                args.putString(Ticket.DESCRIPTION, description);
                args.putInt(Ticket.TIME_TRACK_STATUS, timeTrackStatus);
                Context context = view.getContext();
                Intent intent = new Intent(context, ServiceTicketDetailActivity.class);
                intent.putExtras(args);
                context.startActivity(intent);
            }
        });

    }
    /* The inner RealmBaseAdapter
     * view reports_count is applied here.
     *
     * getRealmAdapter is defined in RealmRecyclerViewAdapter.
     */

    @Override
    public int getItemCount() {
        if (getRealmAdapter() != null) {
            return getRealmAdapter().getCount();
        }
        return 0;
    }
}
