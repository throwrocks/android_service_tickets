package rocks.athrow.android_service_tickets.realmadapter;

import android.content.Context;

import io.realm.RealmResults;
import rocks.athrow.android_service_tickets.data.TicketNote;

/**
 * RealmNotesListAdapter
 * Created by jose on 9/23/16.
 */
public class RealmNotesListAdapter extends RealmModelAdapter<TicketNote> {
    public RealmNotesListAdapter(Context context, RealmResults<TicketNote> realmResults) {
        super(context, realmResults);
    }
}