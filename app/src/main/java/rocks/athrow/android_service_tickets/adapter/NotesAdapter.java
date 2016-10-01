package rocks.athrow.android_service_tickets.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import rocks.athrow.android_service_tickets.R;
import rocks.athrow.android_service_tickets.data.ServiceTicketNote;
import rocks.athrow.android_service_tickets.realmadapter.RealmRecyclerViewAdapter;
/**
 * NotesAdapter
 * Created by jose on 9/23/16.
 */
public class NotesAdapter extends RealmRecyclerViewAdapter<ServiceTicketNote> {
    private final static String DATE_FORMAT = "MM/dd/yyyy";
    private Context mContext;

    private class ViewHolder extends RecyclerView.ViewHolder {
        // Declare views
        TextView note;

        ViewHolder(View view) {
            super(view);
            // Initialize views
            note = (TextView) view.findViewById(R.id.note);
        }
    }

    public NotesAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View serviceNote = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_item, parent, false);
        return new ViewHolder(serviceNote);

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int position) {
        ViewHolder noteViewHolder = (ViewHolder) viewHolder;
        ServiceTicketNote noteRecord = getItem(position);
        // Set the variables
        final String note = noteRecord.getNote();
        // Set the views
        noteViewHolder.note.setText(note);
    }

    @Override
    public int getItemCount() {
        if (getRealmAdapter() != null) {
            return getRealmAdapter().getCount();
        }
        return 0;
    }
}

