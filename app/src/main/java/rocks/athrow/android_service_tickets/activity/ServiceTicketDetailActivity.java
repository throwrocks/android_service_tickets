package rocks.athrow.android_service_tickets.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.Sort;
import rocks.athrow.android_service_tickets.R;
import rocks.athrow.android_service_tickets.adapter.NotesAdapter;
import rocks.athrow.android_service_tickets.data.APIResponse;
import rocks.athrow.android_service_tickets.data.FetchTask;
import rocks.athrow.android_service_tickets.data.ServiceTicket;
import rocks.athrow.android_service_tickets.data.ServiceTicketNote;
import rocks.athrow.android_service_tickets.interfaces.OnTaskComplete;
import rocks.athrow.android_service_tickets.realmadapter.RealmNotesListAdapter;
import rocks.athrow.android_service_tickets.service.UpdateDBService;
import rocks.athrow.android_service_tickets.util.Utilities;


public class ServiceTicketDetailActivity extends AppCompatActivity implements OnTaskComplete {
    final OnTaskComplete onTaskCompleted = this;
    private NotesAdapter mAdapter;
    RecyclerView mRecyclerView;
    RealmResults<ServiceTicketNote> mRealmResults;
    TextView ticketSerialNumber;
    TextView ticketPriority;
    TextView ticketStatus;
    TextView ticketTechnician;
    TextView ticketCreatedDate;
    TextView ticketAssignedDate;
    public TextView ticketOrg;
    public TextView ticketSite;
    public TextView ticketDescription;
    public TextView ticketIssues;
    private String ticketId;
    private Button createNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_ticket_detail);
        Intent intent = getIntent();
        Bundle arguments = intent.getExtras();
        // Set the variables
        ticketId = arguments.getString(ServiceTicket.ID);
        final String serialNumber = arguments.getString(ServiceTicket.SERIAL_NUMBER);
        final String priority = arguments.getString(ServiceTicket.PRIORITY);
        final String status = arguments.getString(ServiceTicket.STATUS);
        final String technician = arguments.getString(ServiceTicket.TECH_NAME);
        final String createdDate = arguments.getString(ServiceTicket.CREATED_DATE);
        final String assignedDate = arguments.getString(ServiceTicket.ASSIGNED_DATE);
        final String closedDate = arguments.getString(ServiceTicket.CLOSED_DATE);
        final String org = arguments.getString(ServiceTicket.ORG);
        final String site = arguments.getString(ServiceTicket.SITE);
        final String description = arguments.getString(ServiceTicket.DESCRIPTION);
        final String issues = arguments.getString(ServiceTicket.ISSUES);
        final String issuesDisplay = Utilities.getBulletedList(issues, ",", 2);

        ticketSerialNumber = (TextView) findViewById(R.id.ticket_number);
        ticketPriority = (TextView) findViewById(R.id.priority);
        ticketStatus = (TextView) findViewById(R.id.status);
        ticketTechnician = (TextView) findViewById(R.id.technician);
        ticketCreatedDate = (TextView) findViewById(R.id.created_date);
        ticketAssignedDate = (TextView) findViewById(R.id.assigned_date);
        ticketOrg = (TextView) findViewById(R.id.org);
        ticketSite = (TextView) findViewById(R.id.site);
        ticketIssues = (TextView) findViewById(R.id.issues);
        ticketDescription = (TextView) findViewById(R.id.description);
        createNote = (Button) findViewById(R.id.create_note);

        ticketSerialNumber.setText(serialNumber);
        ticketPriority.setText(priority);
        ticketStatus.setText(status);
        ticketTechnician.setText(technician);
        ticketCreatedDate.setText(createdDate);
        ticketAssignedDate.setText(assignedDate);
        ticketOrg.setText(org);
        ticketSite.setText(site);
        ticketDescription.setText(description);
        ticketIssues.setText(issuesDisplay);

        Utilities.formatPriorityView(ticketPriority, priority, getApplicationContext());
        Utilities.formatStatusView(ticketStatus, status, getApplicationContext());

        createNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NoteDialog noteDialog = new NoteDialog();
                noteDialog.show(getSupportFragmentManager(), "notes");
            }
        });

        mRealmResults = getNotes(ticketId);
        setupRecyclerView();

        // Get the ticket notes
        FetchTask fetchTask = new FetchTask(onTaskCompleted);
        fetchTask.execute(FetchTask.TICKET_NOTES, ticketId);

    }

    private void setupRecyclerView() {
        mAdapter = new NotesAdapter(getApplicationContext());
        RealmNotesListAdapter realmNotesListAdapter =
                new RealmNotesListAdapter(getApplicationContext(), mRealmResults);
        mAdapter.setRealmAdapter(realmNotesListAdapter);

        mRecyclerView = (RecyclerView) findViewById(R.id.notes_list);
        assert mRecyclerView != null;
        mRecyclerView.setAdapter(mAdapter);
    }

    private void onTaskComplete(APIResponse apiResponse) {
        if (apiResponse.getResponseCode() == 200) {
            Intent updateDBIntent = new Intent(this, UpdateDBService.class);
            String responseText = apiResponse.getResponseText();
            updateDBIntent.putExtra(UpdateDBService.TYPE, UpdateDBService.TYPE_NOTES);
            updateDBIntent.putExtra(UpdateDBService.DATA, responseText);
            LocalBroadcastManager.getInstance(this).
                    registerReceiver(new ResponseReceiver(),
                            new IntentFilter(UpdateDBService.UPDATE_NOTES_DB_SERVICE_BROADCAST));
            this.startService(updateDBIntent);
        }
    }

    /**
     * getNotes
     *
     * @param serviceTicketId
     * @return the RealmResults
     */
    private RealmResults<ServiceTicketNote> getNotes(String serviceTicketId) {
        RealmConfiguration realmConfig = new RealmConfiguration.
                Builder(getApplicationContext()).build();
        Realm.setDefaultConfiguration(realmConfig);
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmResults<ServiceTicketNote> serviceTicketNotes;

        serviceTicketNotes = realm.where(ServiceTicketNote.class).
                equalTo(ServiceTicketNote.SERVICE_TICKET_ID, serviceTicketId).
                findAll().sort(ServiceTicketNote.CREATION_DATE, Sort.DESCENDING);

        realm.commitTransaction();
        return serviceTicketNotes;
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
