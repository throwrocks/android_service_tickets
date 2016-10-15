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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.facebook.stetho.Stetho;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.Sort;
import rocks.athrow.android_service_tickets.R;
import rocks.athrow.android_service_tickets.data.Ticket;
import rocks.athrow.android_service_tickets.realmadapter.RealmServiceTicketsListAdapter;
import rocks.athrow.android_service_tickets.adapter.ServiceTicketsAdapter;
import rocks.athrow.android_service_tickets.data.APIResponse;
import rocks.athrow.android_service_tickets.data.FetchTask;
import rocks.athrow.android_service_tickets.interfaces.OnTaskComplete;
import rocks.athrow.android_service_tickets.service.UpdateDBService;
import rocks.athrow.android_service_tickets.util.PreferencesHelper;
import rocks.athrow.android_service_tickets.util.Utilities;


public class MainActivity extends AppCompatActivity implements OnTaskComplete {
    private final static String[] TAB_QUERY = {"today", "my_open", "all_open", "all_closed"};
    private final static String DATE_FORMAT = "MM/dd/yyy";
    private static int employeeId;
    private ServiceTicketsAdapter ticketsAdapter;
    private RealmResults<Ticket> realmResults;
    private SwipeRefreshLayout swipeContainer;
    private View rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rootView = findViewById(R.id.rootView);
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
                        .build());
        setEmployeeInformation();
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int tabPosition = tab.getPosition();
                switch (tabPosition) {
                    case 0:
                        realmResults = getTickets(TAB_QUERY[0]);
                        setupRecyclerView();
                        break;
                    case 1:
                        realmResults = getTickets(TAB_QUERY[1]);
                        setupRecyclerView();
                        break;
                    case 2:
                        realmResults = getTickets(TAB_QUERY[2]);
                        setupRecyclerView();
                        break;
                    case 3:
                        realmResults = getTickets(TAB_QUERY[3]);
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
                PreferencesHelper prefs = new PreferencesHelper(getApplicationContext());
                String employeeIdString = prefs.loadString(Utilities.EMPLOYEE_ID, Utilities.NULL);
                if (employeeIdString != null && !employeeIdString.equals(Utilities.NULL)) {
                    swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                            android.R.color.holo_blue_dark,
                            android.R.color.holo_green_dark,
                            android.R.color.holo_green_light);
                    boolean isConnected = Utilities.isConnected(getApplicationContext());
                    if (isConnected) {
                        FetchTask fetchTask = new FetchTask(onTaskCompleted);
                        fetchTask.execute(FetchTask.ALL_TICKETS);
                    } else {
                        Utilities.showToast(
                                getApplicationContext(),
                                getString(R.string.general_no_network_connection),
                                Toast.LENGTH_SHORT
                        );
                    }
                } else {
                    Utilities.showToast(
                            getApplicationContext(),
                            getString(R.string.invalid_api_key),
                            Toast.LENGTH_SHORT
                    );
                    swipeContainer.setRefreshing(false);
                }
            }
        });
    }

    /**
     * getTickets
     *
     * @param query the query type (based on the tab selection)
     * @return the RealmResults
     */
    private RealmResults<Ticket> getTickets(String query) {
        RealmConfiguration realmConfig = new RealmConfiguration.
                Builder(getApplicationContext()).build();
        Realm.setDefaultConfiguration(realmConfig);
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmResults<Ticket> tickets;
        switch (query) {
            case "today":
                Date todayDateRaw = new Date();
                String todayDateString = Utilities.getDateAsString(todayDateRaw, DATE_FORMAT, null);
                Date todayDate = Utilities.getStringAsDate(todayDateString, DATE_FORMAT, null);
                tickets = realm.where(Ticket.class).
                        equalTo(Ticket.TECH_ID, employeeId).
                        equalTo(Ticket.ASSIGNED_DATE, todayDate).
                        findAll().sort(Ticket.ORG);
                break;
            case "my_open":
                tickets = realm.where(Ticket.class).
                        equalTo(Ticket.TECH_ID, employeeId).
                        equalTo(Ticket.STATUS, "Open").
                        findAll().
                        sort(Ticket.ORG);
                break;
            case "all_open":
                tickets = realm.where(Ticket.class).
                        equalTo(Ticket.STATUS, "Open").
                        findAll().sort(Ticket.CREATED_DATE);
                break;
            case "all_closed":
                tickets = realm.where(Ticket.class).
                        equalTo(Ticket.STATUS, "Closed").
                        findAll().sort(Ticket.CLOSED_DATE, Sort.DESCENDING);
                break;
            default:
                tickets = realm.where(Ticket.class).findAll().sort(Ticket.ORG);
                break;
        }
        realm.commitTransaction();
        return tickets;
    }

    /**
     * onTaskComplete
     *
     * @param apiResponse the API Response
     */
    private void onTaskComplete(APIResponse apiResponse) {
        if (apiResponse != null) {
            Intent updateDBIntent = new Intent(this, UpdateDBService.class);
            switch (apiResponse.getResponseCode()) {
                case 200:
                    String responseText = apiResponse.getResponseText();
                    updateDBIntent.putExtra(UpdateDBService.TYPE, UpdateDBService.TYPE_TICKETS);
                    updateDBIntent.putExtra(UpdateDBService.DATA, responseText);
                    LocalBroadcastManager.getInstance(this).
                            registerReceiver(new ResponseReceiver(),
                                    new IntentFilter(UpdateDBService.UPDATE_TICKETS_DB_SERVICE_BROADCAST));
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
    private void setupRecyclerView() {
        ticketsAdapter = new ServiceTicketsAdapter(getApplicationContext());
        RealmServiceTicketsListAdapter realmServiceTicketsListAdapter =
                new RealmServiceTicketsListAdapter(getApplicationContext(), realmResults);
        ticketsAdapter.setRealmAdapter(realmServiceTicketsListAdapter);
        RecyclerView ticketsList = (RecyclerView) findViewById(R.id.service_tickets_list);
        assert ticketsList != null;
        ticketsList.setAdapter(ticketsAdapter);
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
            swipeContainer.setRefreshing(false);
        }
    }

    /**
     * setEmployeeInformation
     * This method is used to set the employee id which is necessary for the queries
     */
    private void setEmployeeInformation() {
        PreferencesHelper prefs = new PreferencesHelper(getApplicationContext());
        String employeeIdString = prefs.loadString(Utilities.EMPLOYEE_ID, Utilities.NULL);
        if (employeeIdString != null && !employeeIdString.equals(Utilities.NULL)) {
            employeeId = Integer.parseInt(employeeIdString);
        }
    }

    /**
     * onNewIntent
     * Used to reset the view when coming back from the settings activity
     * For example, when a ticket is closed, we want to update the list to show the new status
     *
     * @param intent passed from the calling activity
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setEmployeeInformation();
        if (employeeId > 0) {
            TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
            int position = tabLayout.getSelectedTabPosition();
            realmResults = getTickets(TAB_QUERY[position]);
            setupRecyclerView();
        } else {
            if (ticketsAdapter != null) {
                ticketsAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.api_key:
                openSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void openSettings() {
        Context context = rootView.getContext();
        Intent intent = new Intent(context, SettingsActivity.class);
        context.startActivity(intent);
    }

    /**
     * onPause
     * Used to clear the refreshing indicator when navigating away from the list
     */
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
