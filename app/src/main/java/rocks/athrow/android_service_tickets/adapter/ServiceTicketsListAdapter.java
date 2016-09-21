package rocks.athrow.android_service_tickets.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import io.realm.RealmResults;
import rocks.athrow.android_service_tickets.R;
import rocks.athrow.android_service_tickets.data.ServiceTicket;

/**
 * Created by joselopez on 9/21/16.
 */
public class ServiceTicketsListAdapter extends RealmRecyclerViewAdapter<ServiceTicket> {
    private Context mContext;
    RealmResults<ServiceTicket> mRealmResults;

    private class ViewHolder extends RecyclerView.ViewHolder {

        // Declare views
        RelativeLayout ticketItem;
        TextView ticketId;
        TextView ticketOrg;
        TextView ticketSite;

        public ViewHolder(View view) {
            super(view);
            // Initialize views
            ticketItem = (RelativeLayout) view.findViewById(R.id.ticket_item);
            ticketId = (TextView) view.findViewById(R.id.ticket_number);
            ticketOrg = (TextView) view.findViewById(R.id.org);
            ticketSite = (TextView) view.findViewById(R.id.site);

        }
    }


    public ServiceTicketsListAdapter(Context context,RealmResults<ServiceTicket> realmResults) {
        this.mContext = context;
        this.mRealmResults = realmResults;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View reviewListRecyclerView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.service_ticket_item, parent, false);
        return new ViewHolder(reviewListRecyclerView);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ViewHolder serviceTicketRecyclerView = (ViewHolder) viewHolder;
        ServiceTicket serviceTicket = getItem(position);
        // Set the variables
        int serialNumber = serviceTicket.getSerial_number();
        int org = serviceTicket.getOrg();
        String site = serviceTicket.getSite();
        // Set the views
        serviceTicketRecyclerView.ticketId.setText(serialNumber);
        serviceTicketRecyclerView.ticketOrg.setText(org);
        serviceTicketRecyclerView.ticketSite.setText(site);
        // Set click listener
        serviceTicketRecyclerView.ticketItem.setOnClickListener(new View.OnClickListener() {
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
