package rocks.athrow.android_service_tickets.adapter;

import android.content.Context;

import io.realm.RealmResults;
import rocks.athrow.android_service_tickets.data.ServiceTicket;
/**
 * RealmServiceTicketsListAdapter
 * I'm going to need one more convenience class to help create a RealmModelAdapter
 * supporting the RealmObject type I want
 * http://gradlewhy.ghost.io/realm-results-with-recyclerview/
 */
public class RealmServiceTicketsListAdapter extends RealmModelAdapter<ServiceTicket> {
    public RealmServiceTicketsListAdapter(Context context, RealmResults<ServiceTicket> realmResults) {
        super(context, realmResults);
    }
}