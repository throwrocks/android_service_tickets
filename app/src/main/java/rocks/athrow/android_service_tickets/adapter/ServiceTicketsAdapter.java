package rocks.athrow.android_service_tickets.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import rocks.athrow.android_service_tickets.activity.ServiceTicketDetailActivity;
import rocks.athrow.android_service_tickets.realmadapter.RealmRecyclerViewAdapter;

import rocks.athrow.android_service_tickets.R;
import rocks.athrow.android_service_tickets.data.ServiceTicket;
import rocks.athrow.android_service_tickets.util.Utilities;


/**
 * ServiceTicketsListAdapter
 * Created by joselopez on 9/21/16.
 */
public class ServiceTicketsAdapter extends RealmRecyclerViewAdapter<ServiceTicket> {
    private final static String DATE_FORMAT = "MM/dd/yyyy";
    private Context mContext;

    private class ViewHolder extends RecyclerView.ViewHolder {
        // Declare views
        RelativeLayout ticketItem;
        TextView ticketSerialNumber;
        TextView ticketPriority;
        TextView ticketStatus;
        TextView ticketTechnician;
        TextView ticketCreatedDate;
        TextView ticketAssignedDate;
        TextView ticketOrg;
        TextView ticketSite;
        TextView ticketSiteAddress;
        TextView ticketSitePhone;
        TextView ticketIssues;
        TextView ticketDescription;
        Button ticketOpenButton;

        ViewHolder(View view) {
            super(view);
            // Initialize views
            ticketItem = (RelativeLayout) view.findViewById(R.id.ticket_item);
            ticketSerialNumber = (TextView) view.findViewById(R.id.ticket_number);
            ticketPriority = (TextView) view.findViewById(R.id.priority);
            ticketStatus = (TextView) view.findViewById(R.id.status);
            ticketTechnician = (TextView) view.findViewById(R.id.technician);
            ticketCreatedDate = (TextView) view.findViewById(R.id.created_date);
            ticketAssignedDate = (TextView) view.findViewById(R.id.assigned_date);
            ticketOrg = (TextView) view.findViewById(R.id.org);
            ticketSite = (TextView) view.findViewById(R.id.site);
            ticketSiteAddress = (TextView) view.findViewById(R.id.site_address);
            ticketSitePhone = (TextView) view.findViewById(R.id.site_phone);
            ticketIssues = (TextView) view.findViewById(R.id.issues);
            ticketDescription = (TextView) view.findViewById(R.id.description);
            ticketOpenButton = (Button) view.findViewById(R.id.open_ticket_button);
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
        ViewHolder serviceTicketViewHolder = (ViewHolder) viewHolder;
        ServiceTicket serviceTicket = getItem(position);
        // Set the variables
        final String ticketId = serviceTicket.getId();
        final String serialNumber = "#" + Integer.toString(serviceTicket.getSerial_number());
        final String priority = serviceTicket.getPriority();
        final String status = serviceTicket.getStatus();
        final String technician = serviceTicket.getTech_name();
        final String createdDate = Utilities.getDateAsString(serviceTicket.getCreated_date(), DATE_FORMAT, null );
        final String assignedDate = Utilities.getDateAsString(serviceTicket.getAssigned_date(), DATE_FORMAT, null );
        final String closedDate = Utilities.getDateAsString(serviceTicket.getClosed_date(), DATE_FORMAT, null );
        final String org = Integer.toString(serviceTicket.getOrg());
        final String site = serviceTicket.getSite();
        final String site_address = serviceTicket.getSite_address();
        final String site_phone = serviceTicket.getSite_phone();
        final String description = serviceTicket.getDescription();
        final String issues = serviceTicket.getIssues();
        final String issuesDisplay = Utilities.getBulletedList(issues, ",", 1);
        //String issues = serviceTicket.getIssues().replace(",","\n");
        // Set the views
        serviceTicketViewHolder.ticketSerialNumber.setText(serialNumber);
        serviceTicketViewHolder.ticketPriority.setText(priority);
        serviceTicketViewHolder.ticketStatus.setText(status);
        serviceTicketViewHolder.ticketTechnician.setText(technician);
        serviceTicketViewHolder.ticketCreatedDate.setText(createdDate);
        serviceTicketViewHolder.ticketAssignedDate.setText(assignedDate);
        //serviceTicketViewHolder.ticketClosedDate.setText(closedDate);
        serviceTicketViewHolder.ticketOrg.setText(org);
        serviceTicketViewHolder.ticketSite.setText(site);
        serviceTicketViewHolder.ticketSiteAddress.setText(site_address);
        serviceTicketViewHolder.ticketSitePhone.setText(site_phone);
        serviceTicketViewHolder.ticketIssues.setText(issuesDisplay);
        serviceTicketViewHolder.ticketDescription.setText(description);

        Utilities.formatPriorityView(serviceTicketViewHolder.ticketPriority, priority, mContext);
        Utilities.formatStatusView(serviceTicketViewHolder.ticketStatus, status, mContext);

        // Set click listener
        serviceTicketViewHolder.ticketOpenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle arguments = new Bundle();
                arguments.putString(ServiceTicket.ID, ticketId);
                arguments.putString(ServiceTicket.SERIAL_NUMBER, serialNumber);
                arguments.putString(ServiceTicket.PRIORITY, priority);
                arguments.putString(ServiceTicket.STATUS, status);
                arguments.putString(ServiceTicket.TECH_NAME, technician);
                arguments.putString(ServiceTicket.CREATED_DATE, createdDate);
                arguments.putString(ServiceTicket.ASSIGNED_DATE, assignedDate);
                arguments.putString(ServiceTicket.CLOSED_DATE, closedDate);
                arguments.putString(ServiceTicket.ORG, org);
                arguments.putString(ServiceTicket.SITE, site);
                arguments.putString(ServiceTicket.SITE_ADDRESS, site_address);
                arguments.putString(ServiceTicket.SITE_PHONE, site_phone);
                arguments.putString(ServiceTicket.ISSUES, issues);
                arguments.putString(ServiceTicket.DESCRIPTION, description);
                Context context = view.getContext();
                Intent intent = new Intent(context, ServiceTicketDetailActivity.class);
                intent.putExtras(arguments);
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
