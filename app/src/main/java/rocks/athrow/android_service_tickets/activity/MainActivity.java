package rocks.athrow.android_service_tickets.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.facebook.stetho.Stetho;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;


import java.util.Date;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.Sort;
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
    private final static String DATE_FORMAT = "MM/dd/yyy";
    private int techId = 50491;
    private RecyclerView mRecyclerView;
    private ServiceTicketsAdapter mAdapter;
    private RealmResults<ServiceTicket> mRealmResults;
    private TabLayout tabLayout;
    private SwipeRefreshLayout swipeContainer;
    private static final Boolean DEBUG = false;

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

        mRealmResults = getTickets("today");
        setupRecyclerView();
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int tabPosition = tab.getPosition();
                switch (tabPosition) {
                    case 0:
                        mRealmResults = getTickets("today");
                        setupRecyclerView();
                        break;
                    case 1:
                        mRealmResults = getTickets("my_open");
                        setupRecyclerView();
                        break;
                    case 2:
                        mRealmResults = getTickets("all_open");
                        setupRecyclerView();
                        break;
                    case 3:
                        mRealmResults = getTickets("all_closed");
                        setupRecyclerView();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        final OnTaskComplete onTaskCompleted = this;
        // Set up the SwipeRefreshLayout
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
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
     * getTickets
     * @param query the query type (based on the tab selection)
     * @return the RealmResults
     */
    private RealmResults<ServiceTicket> getTickets(String query) {
        RealmConfiguration realmConfig = new RealmConfiguration.
                Builder(getApplicationContext()).build();
        Realm.setDefaultConfiguration(realmConfig);
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmResults<ServiceTicket> serviceTickets;
        switch (query) {
            case "today":
                Date todayDateRaw = new Date();
                String todayDateString = Utilities.getDateAsString(todayDateRaw, DATE_FORMAT, null);
                Date todayDate = Utilities.getStringAsDate(todayDateString, DATE_FORMAT, null);
                serviceTickets = realm.where(ServiceTicket.class).
                        equalTo(ServiceTicket.TECH_ID, techId).
                        equalTo(ServiceTicket.ASSIGNED_DATE, todayDate).
                        findAll().sort(ServiceTicket.ORG);
                break;
            case "my_open":
                serviceTickets = realm.where(ServiceTicket.class).
                        equalTo(ServiceTicket.TECH_ID, techId).
                        equalTo(ServiceTicket.STATUS, "Open").
                        findAll().
                        sort(ServiceTicket.ORG);
                break;
            case "all_open":
                serviceTickets = realm.where(ServiceTicket.class).
                        equalTo(ServiceTicket.STATUS, "Open").
                        findAll().sort(ServiceTicket.CREATED_DATE);
                break;
            case "all_closed":
                serviceTickets = realm.where(ServiceTicket.class).
                        equalTo(ServiceTicket.STATUS, "Closed").
                        findAll().sort(ServiceTicket.CLOSED_DATE, Sort.DESCENDING);
                break;
            default:
                serviceTickets = realm.where(ServiceTicket.class).findAll().sort(ServiceTicket.ORG);
                break;
        }
        realm.commitTransaction();
        return serviceTickets;
    }

    /**
     * onTaskComplete
     *
     * @param apiResponse the API Response
     */
    private void onTaskComplete(APIResponse apiResponse) {
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

    /**
     * setupRecyclerView
     */
    private void setupRecyclerView(){
        mAdapter = new ServiceTicketsAdapter(getApplicationContext());
        RealmServiceTicketsListAdapter realmServiceTicketsListAdapter =
                new RealmServiceTicketsListAdapter(getApplicationContext(), mRealmResults);
        mAdapter.setRealmAdapter(realmServiceTicketsListAdapter);

        mRecyclerView = (RecyclerView) findViewById(R.id.service_tickets_list);
        assert mRecyclerView != null;
        mRecyclerView.setAdapter(mAdapter);
    }

    /**
     * ResponseReceiver
     * A class to manage handling the UpdateDBService response
     */
    private class ResponseReceiver extends BroadcastReceiver {

        private ResponseReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            String text = getString(R.string.tickets_up_to_date);
            int duration = Toast.LENGTH_SHORT;
            final Toast toast = Toast.makeText(getApplicationContext(), text, duration);
            toast.show();
            mAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onPause() {
        super.onPause();
        swipeContainer.setRefreshing(false);
    }

    @Override
    public void OnTaskComplete(APIResponse apiResponse) {
        onTaskComplete(apiResponse);
    }
}
