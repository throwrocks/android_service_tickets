package rocks.athrow.android_service_tickets.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import io.realm.RealmResults;
import rocks.athrow.android_service_tickets.R;
import rocks.athrow.android_service_tickets.adapter.NotesAdapter;
import rocks.athrow.android_service_tickets.data.APIResponse;
import rocks.athrow.android_service_tickets.data.FetchTask;
import rocks.athrow.android_service_tickets.data.ServiceTicket;
import rocks.athrow.android_service_tickets.data.ServiceTicketNote;
import rocks.athrow.android_service_tickets.fragment.ServiceTicketNotesFragment;
import rocks.athrow.android_service_tickets.interfaces.OnTaskComplete;
import rocks.athrow.android_service_tickets.realmadapter.RealmNotesListAdapter;
import rocks.athrow.android_service_tickets.service.UpdateDBService;
import rocks.athrow.android_service_tickets.util.Utilities;

public class ServiceTicketDetailActivity extends AppCompatActivity implements OnTaskComplete {
    final OnTaskComplete onTaskCompleted = this;
    private NotesAdapter mAdapter;
    RecyclerView mRecyclerView;
    RealmResults<ServiceTicketNote> mRealmResults;
    public TextView ticketOrg;
    public TextView ticketSite;
    public TextView ticketDescription;
    public TextView ticketIssues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_ticket_detail);
        Intent intent = getIntent();
        Bundle arguments = intent.getExtras();
        // Set the variables
        final String ticketId = arguments.getString(ServiceTicket.ID);
        final String serialNumber = arguments.getString(ServiceTicket.SERIAL_NUMBER);
        final String priority = arguments.getString(ServiceTicket.PRIORITY);
        final String status = arguments.getString(ServiceTicket.STATUS);
        final String technician = arguments.getString(ServiceTicket.TECH_NAME);
        final String createdDate = arguments.getString(ServiceTicket.CREATED_DATE);
        final String assignedDate = arguments.getString(ServiceTicket.ASSIGNED_DATE);
        final String closedDate = arguments.getString(ServiceTicket.CLOSED_DATE);
        final String org = arguments.getString(ServiceTicket.ORG);
        final String site = arguments.getString(ServiceTicket.SITE);
        final String site_address = arguments.getString(ServiceTicket.SITE_ADDRESS);
        final String site_phone = arguments.getString(ServiceTicket.SITE_PHONE);
        final String description = arguments.getString(ServiceTicket.DESCRIPTION);
        final String issues = arguments.getString(ServiceTicket.ISSUES);
        final String issuesDisplay = Utilities.getBulletedList(issues, ",", 2);

        ticketOrg = (TextView) findViewById(R.id.org);
        ticketSite = (TextView) findViewById(R.id.site);
        ticketIssues = (TextView) findViewById(R.id.issues);
        ticketDescription = (TextView) findViewById(R.id.description);

        ticketOrg.setText(org);
        ticketSite.setText(site);
        ticketDescription.setText(description);
        ticketIssues.setText(issuesDisplay);

        // Get the ticket notes
        FetchTask fetchTask = new FetchTask(onTaskCompleted);
        fetchTask.execute(FetchTask.TICKET_NOTES,ticketId);

        ServiceTicketNotesFragment fragment = new ServiceTicketNotesFragment();
        fragment.setArguments(arguments);
        getFragmentManager().beginTransaction()
                .add(R.id.notes_frame, fragment)
                .commit();
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        mAdapter = new NotesAdapter(getApplicationContext());
        RealmNotesListAdapter realmNotesListAdapter =
                new RealmNotesListAdapter(getApplicationContext(), mRealmResults);
        mAdapter.setRealmAdapter(realmNotesListAdapter);

        mRecyclerView = (RecyclerView) findViewById(R.id.service_tickets_list);
        assert mRecyclerView != null;
        mRecyclerView.setAdapter(mAdapter);
    }

    private void onTaskComplete(APIResponse apiResponse) {
        Intent updateDBIntent = new Intent(this, UpdateDBService.class);
        if ( apiResponse.getResponseCode() == 200 ){
            // TODO: Implement service to update the database
            String responseText = apiResponse.getResponseText();
            updateDBIntent.putExtra(UpdateDBService.DATA, responseText);
            LocalBroadcastManager.getInstance(this).
                    registerReceiver(new ResponseReceiver(),
                            new IntentFilter(UpdateDBService.UPDATE_NOTES_DB_SERVICE_BROADCAST));
            this.startService(updateDBIntent);
        }

    }

    @Override
    public void OnTaskComplete(APIResponse apiResponse) {
        onTaskComplete(apiResponse);
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
            mAdapter.notifyDataSetChanged();
        }
    }



}
