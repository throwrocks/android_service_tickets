package rocks.athrow.android_service_tickets.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import rocks.athrow.android_service_tickets.R;
import rocks.athrow.android_service_tickets.data.TicketNote;
import rocks.athrow.android_service_tickets.realmadapter.RealmRecyclerViewAdapter;
import rocks.athrow.android_service_tickets.util.Utilities;

/**
 * NotesAdapter
 * Created by jose on 9/23/16.
 */
public class NotesAdapter extends RealmRecyclerViewAdapter<TicketNote> {
    private final static String DATE_FORMAT = "MM/dd/yyyy";
    private final Context mContext;

    private class ViewHolder extends RecyclerView.ViewHolder {
        // Declare views
        final LinearLayout noteItem;
        final TextView noteAuthor;
        final TextView noteDate;
        final TextView note;

        ViewHolder(View view) {
            super(view);
            // Initialize views
            noteItem = (LinearLayout) view.findViewById(R.id.note_item);
            noteAuthor = (TextView) view.findViewById(R.id.note_author);
            noteDate = (TextView) view.findViewById(R.id.note_date);
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
        TicketNote noteRecord = getItem(position);
        // Set the variables
        final String author = noteRecord.getCreated_by();
        final String date = Utilities.getDateAsString(noteRecord.getCreation_date(), DATE_FORMAT, null);
        final String note = noteRecord.getNote();
        // Set the views
        noteViewHolder.noteItem.setBackground(ContextCompat.getDrawable(mContext, R.drawable.notepad));
        noteViewHolder.noteAuthor.setText(author);
        noteViewHolder.noteDate.setText(date);
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

