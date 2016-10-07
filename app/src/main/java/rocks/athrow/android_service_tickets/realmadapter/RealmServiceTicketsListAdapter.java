package rocks.athrow.android_service_tickets.realmadapter;

import android.content.Context;

import io.realm.RealmResults;
import rocks.athrow.android_service_tickets.data.Ticket;

/**
 * RealmServiceTicketsListAdapter
 * I'm going to need one more convenience class to help create a RealmModelAdapter
 * supporting the RealmObject type I want
 * http://gradlewhy.ghost.io/realm-results-with-recyclerview/
 */
public class RealmServiceTicketsListAdapter extends RealmModelAdapter<Ticket> {
    public RealmServiceTicketsListAdapter(Context context, RealmResults<Ticket> realmResults) {
        super(context, realmResults);
    }
}