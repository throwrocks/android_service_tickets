package rocks.athrow.android_service_tickets.realmadapter;

import android.content.Context;

import io.realm.RealmResults;
import rocks.athrow.android_service_tickets.data.ServiceTicketNote;

/**
 * Created by jose on 9/23/16.
 */
public class RealmNotesListAdapter extends RealmModelAdapter<ServiceTicketNote> {
    public RealmNotesListAdapter(Context context, RealmResults<ServiceTicketNote> realmResults) {
        super(context, realmResults);
    }
}