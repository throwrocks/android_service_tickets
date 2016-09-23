package rocks.athrow.android_service_tickets.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.facebook.stetho.Stetho;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;


import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import rocks.athrow.android_service_tickets.R;
import rocks.athrow.android_service_tickets.realmadapter.RealmServiceTicketsListAdapter;
import rocks.athrow.android_service_tickets.adapter.ServiceTicketsAdapter;
import rocks.athrow.android_service_tickets.data.APIResponse;
import rocks.athrow.android_service_tickets.data.FetchTask;
import rocks.athrow.android_service_tickets.data.ServiceTicket;
import rocks.athrow.android_service_tickets.interfaces.OnTaskComplete;
import rocks.athrow.android_service_tickets.service.UpdateDBService;
import rocks.athrow.android_service_tickets.util.Utilities;

public class MainActivity extends AppCompatActivity implements OnTaskComplete {
    ServiceTicketsAdapter mAdapter;
    RealmResults<ServiceTicket> mRealmResults;
    SwipeRefreshLayout swipeContainer;
    private static final Boolean DEBUG = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
                        .build());

        if (DEBUG) {
            // Delete everything (for testing only)
            RealmConfiguration realmConfig = new RealmConfiguration.Builder(this).build();
            Realm.setDefaultConfiguration(realmConfig);
            Realm realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            realm.deleteAll();
            realm.commitTransaction();
        }


        mAdapter = new ServiceTicketsAdapter(getApplicationContext());

        RealmConfiguration realmConfig = new RealmConfiguration.
                Builder(getApplicationContext()).build();
        Realm.setDefaultConfiguration(realmConfig);
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmResults<ServiceTicket> serviceTickets =
                realm.where(ServiceTicket.class).findAll().sort(ServiceTicket.ORG);
        realm.commitTransaction();
        mRealmResults = serviceTickets;

        RealmServiceTicketsListAdapter realmServiceTicketsListAdapter =
                new RealmServiceTicketsListAdapter(getApplicationContext(), mRealmResults);
        mAdapter.setRealmAdapter(realmServiceTicketsListAdapter);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.service_tickets_list);
        assert recyclerView != null;
        recyclerView.setAdapter(mAdapter);

        final OnTaskComplete onTaskCompleted = this;

        // Set up the SwipeRefreshLayout
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // with a callBack to remove itself and present a toast when finishing the FetchReviews task
        final MainActivity.ReviewsListFragmentCallback callback = new MainActivity.ReviewsListFragmentCallback() {
            @Override
            public void onFetchReviewsCompleted(int result) {
                swipeContainer.setRefreshing(false);
                CharSequence text;
                if ( result == -1 ){
                    text = getString(R.string.bad_server_response);
                }
                else{
                    text = getString(R.string.tickets_up_to_date);
                }
                int duration = Toast.LENGTH_SHORT;
                final Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                toast.show();
            }
        };
        // and with a listener to trigger the FetchReviews task
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                boolean isConnected = Utilities.isConnected(getApplicationContext());
                if (isConnected) {
                    FetchTask fetchTask = new FetchTask(onTaskCompleted);
                    fetchTask.execute();
                } else {
                    CharSequence text = getString(R.string.general_no_network_connection);
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                    toast.show();
                    swipeContainer.setRefreshing(false);
                }
            }
        });
        // set up the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_blue_dark,
                android.R.color.holo_green_dark,
                android.R.color.holo_green_light);
    }

    /**
     * ReviewsListFragmentCallback
     * An interface to update the UI after getting new reviews from the API
     */
    public interface ReviewsListFragmentCallback {
        void onFetchReviewsCompleted(int result);
    }

    public void onTaskComplete(APIResponse apiResponse) {
        if (apiResponse != null) {
            swipeContainer.setRefreshing(false);
            Intent updateDBIntent = new Intent(this, UpdateDBService.class);
            switch (apiResponse.getResponseCode()) {
                case 200:
                    String responseText = apiResponse.getResponseText();
                    updateDBIntent.putExtra("serviceTicketsJSON", responseText);
                    LocalBroadcastManager.getInstance(this).
                            registerReceiver(new ResponseReceiver(),
                                    new IntentFilter("UpdateServiceTicketsBroadcast"));
                    this.startService(updateDBIntent);
                    break;
                default:
                    Context context = getApplicationContext();
                    CharSequence text = apiResponse.getResponseText();
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
            }
        }
    }

    private class ResponseReceiver extends BroadcastReceiver {

        private ResponseReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {

            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * setupRecyclerView
     * This method handles setting the adapter to the RecyclerView
     *
     * @param recyclerView the movie's list RecyclerView
     */
    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(mAdapter);

    }


    @Override
    public void onPause() {
        super.onPause();
        swipeContainer.setRefreshing(false);
        /*if (fetchReviews != null) {
            fetchReviews.cancel(true);
        }
        if (fetchFeedbacks != null) {
            fetchFeedbacks.cancel(true);
        }*/
    }

    @Override
    public void OnTaskComplete(APIResponse apiResponse) {
        onTaskComplete(apiResponse);
    }
}
