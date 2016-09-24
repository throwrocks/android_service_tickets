package rocks.athrow.android_service_tickets.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import rocks.athrow.android_service_tickets.R;
import rocks.athrow.android_service_tickets.activity.ServiceTicketDetailActivity;
import rocks.athrow.android_service_tickets.data.ServiceTicket;
import rocks.athrow.android_service_tickets.data.ServiceTicketNote;
import rocks.athrow.android_service_tickets.realmadapter.RealmRecyclerViewAdapter;
import rocks.athrow.android_service_tickets.util.Utilities;

/**
 * NotesAdapter
 * Created by jose on 9/23/16.
 */
public class NotesAdapter extends RealmRecyclerViewAdapter<ServiceTicketNote> {
    private final static String DATE_FORMAT = "MM/dd/yyyy";
    private Context mContext;

    public class ViewHolder extends RecyclerView.ViewHolder {
        // Declare views


        public ViewHolder(View view) {
            super(view);
            // Initialize views

        }
    }


    public NotesAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View serviceTicketCardView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_item, parent, false);
        return new ViewHolder(serviceTicketCardView);

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int position) {
        ViewHolder serviceTicketViewHolder = (ViewHolder) viewHolder;
        ServiceTicketNote note = getItem(position);
        // Set the variables

        // Set the views

    }
    @Override
    public int getItemCount() {
        if (getRealmAdapter() != null) {
            return getRealmAdapter().getCount();
        }
        return 0;
    }
}

