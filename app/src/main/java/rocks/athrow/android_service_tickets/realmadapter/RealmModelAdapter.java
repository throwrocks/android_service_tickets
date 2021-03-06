package rocks.athrow.android_service_tickets.realmadapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import io.realm.RealmBaseAdapter;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * RealmBaseAdapter
 * Provides the inner workings of RealmRecyclerAdapter
 * Requires the implementation of getView() but is not needed
 * http://gradlewhy.ghost.io/realm-results-with-recyclerview/
 */
class RealmModelAdapter<T extends RealmObject> extends RealmBaseAdapter<T> {
    RealmModelAdapter(Context context, RealmResults<T> realmResults) {
        super(context, realmResults);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}