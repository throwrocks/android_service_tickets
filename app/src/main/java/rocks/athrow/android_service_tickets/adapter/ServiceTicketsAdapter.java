package rocks.athrow.android_service_tickets.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import rocks.athrow.android_service_tickets.realmadapter.RealmRecyclerViewAdapter;

import rocks.athrow.android_service_tickets.R;
import rocks.athrow.android_service_tickets.data.ServiceTicket;
import rocks.athrow.android_service_tickets.util.Utilities;

/**
 * ServiceTicketsListAdapter
 * Created by joselopez on 9/21/16.
 */
public class ServiceTicketsAdapter extends RealmRecyclerViewAdapter<ServiceTicket> {
    private Context mContext;

    public class ViewHolder extends RecyclerView.ViewHolder {
        // Declare views
        public RelativeLayout ticketItem;
        public TextView ticketId;
        public TextView ticketPriority;
        public TextView ticketTechnician;
        public TextView ticketOrg;
        public TextView ticketSite;
        public TextView ticketIssues;
        public TextView ticketDescription;
        public Button ticketOpenButton;
        public ViewHolder(View view) {
            super(view);
            // Initialize views
            ticketItem = (RelativeLayout) view.findViewById(R.id.ticket_item);
            ticketId = (TextView) view.findViewById(R.id.ticket_number);
            ticketPriority = (TextView) view.findViewById(R.id.priority);
            ticketTechnician = (TextView) view.findViewById(R.id.technician);
            ticketOrg = (TextView) view.findViewById(R.id.org);
            ticketSite = (TextView) view.findViewById(R.id.site);
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
                .inflate(R.layout.service_ticket_item, parent, false);
        return new ViewHolder(serviceTicketCardView);

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int position) {
        ViewHolder serviceTicketViewHolder = (ViewHolder) viewHolder;
        ServiceTicket serviceTicket = getItem(position);
        // Set the variables
        String serialNumber = "#" + Integer.toString(serviceTicket.getSerial_number());
        String priority = serviceTicket.getPriority();
        String technician = serviceTicket.getTech_name();
        String org = Integer.toString(serviceTicket.getOrg());
        //String site = serviceTicket.getSite();
        String description = serviceTicket.getDescription();
        String issues = Utilities.getBulletedList(serviceTicket.getIssues(), ",");
        //String issues = serviceTicket.getIssues().replace(",","\n");
        // Set the views
        serviceTicketViewHolder.ticketId.setText(serialNumber);
        serviceTicketViewHolder.ticketPriority.setText(priority);
        serviceTicketViewHolder.ticketTechnician.setText(technician);
        serviceTicketViewHolder.ticketOrg.setText(org);
        //serviceTicketCardView.ticketSite.setText(site);
        serviceTicketViewHolder.ticketIssues.setText(issues);
        serviceTicketViewHolder.ticketDescription.setText(description);

        switch (priority){
            case "High":
                serviceTicketViewHolder.ticketPriority.setText("H");
                serviceTicketViewHolder.ticketPriority.
                        setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.badge_high));
                break;
            case "Medium":
                serviceTicketViewHolder.ticketPriority.setText("M");
                serviceTicketViewHolder.ticketPriority.
                        setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.badge_medium));
                break;
            case "Low":
                serviceTicketViewHolder.ticketPriority.setText("L");
                serviceTicketViewHolder.ticketPriority.
                        setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.badge_low));}

        // Set click listener
        serviceTicketViewHolder.ticketOpenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = mContext;
                CharSequence text = "Hello toast!";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
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
