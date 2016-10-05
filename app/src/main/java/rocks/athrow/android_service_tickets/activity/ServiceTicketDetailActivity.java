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
import android.widget.Toast;

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
    private final OnTaskComplete onTaskCompleted = this;
    private NotesAdapter mAdapter;
    private RealmResults<ServiceTicketNote> mRealmResults;
    private String ticketId;

    private TextView ticketNotesLabel;

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
        @SuppressWarnings("UnusedAssignment") final String closedDate = arguments.getString(ServiceTicket.CLOSED_DATE);
        final String org = arguments.getString(ServiceTicket.ORG);
        final String site = arguments.getString(ServiceTicket.SITE);
        final String description = arguments.getString(ServiceTicket.DESCRIPTION);
        final String issues = arguments.getString(ServiceTicket.ISSUES);
        final String issuesDisplay = Utilities.getBulletedList(issues, ",", 2);

        TextView ticketSerialNumber = (TextView) findViewById(R.id.ticket_number);
        TextView ticketPriority = (TextView) findViewById(R.id.priority);
        TextView ticketStatus = (TextView) findViewById(R.id.status);
        TextView ticketTechnician = (TextView) findViewById(R.id.technician);
        TextView ticketCreatedDate = (TextView) findViewById(R.id.created_date);
        TextView ticketAssignedDate = (TextView) findViewById(R.id.assigned_date);
        TextView ticketOrg = (TextView) findViewById(R.id.org);
        TextView ticketSite = (TextView) findViewById(R.id.site);
        TextView ticketIssues = (TextView) findViewById(R.id.issues);
        TextView ticketDescription = (TextView) findViewById(R.id.description);
        Button createNote = (Button) findViewById(R.id.create_note);
        ticketNotesLabel = (TextView) findViewById(R.id.notes_label);

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
                Bundle args = new Bundle();
                args.putString(ServiceTicket.ID, ticketId);
                noteDialog.setArguments(args);
                noteDialog.show(getSupportFragmentManager(), "notes");
            }
        });
        mRealmResults = getNotes(ticketId);
        getNotesFromAPI();

    }

    /**
     * getNotesFromAPI
     * Wrapper method to fetch the notes from the API
     */
    private void getNotesFromAPI() {
        // Get the ticket notes
        FetchTask fetchTask = new FetchTask(onTaskCompleted);
        fetchTask.execute(FetchTask.TICKET_NOTES, ticketId);
    }

    /**
     * setupRecyclerView
     * Method to set the bind the notes list (RecyclerView) with the notes data
     */
    private void setupRecyclerView() {
        if (mRealmResults != null && mRealmResults.size() > 0) {
            ticketNotesLabel.setVisibility(View.VISIBLE);
            ticketNotesLabel.setText(getResources().getString(R.string.label_notes));
            mAdapter = new NotesAdapter(getApplicationContext());
            RealmNotesListAdapter realmNotesListAdapter =
                    new RealmNotesListAdapter(getApplicationContext(), mRealmResults);
            mAdapter.setRealmAdapter(realmNotesListAdapter);
            RecyclerView ticketNotesList = (RecyclerView) findViewById(R.id.notes_list);
            assert ticketNotesList != null;
            ticketNotesList.setAdapter(mAdapter);
        } else {
            ticketNotesLabel.setVisibility(View.GONE);
        }
    }

    /**
     * onTaskComplete
     * Callback method to handle results from the fetch task and the note dialog
     *
     * @param apiResponse the APIResponse object
     */
    private void onTaskComplete(APIResponse apiResponse) {
        switch (apiResponse.getMeta()) {
            case FetchTask.TICKET_NOTES:
                if (apiResponse.getResponseCode() == 200) {
                    Intent updateDBIntent = new Intent(this, UpdateDBService.class);
                    String responseText = apiResponse.getResponseText();
                    updateDBIntent.putExtra(UpdateDBService.TYPE, UpdateDBService.TYPE_NOTES);
                    updateDBIntent.putExtra(UpdateDBService.DATA, responseText);
                    LocalBroadcastManager.getInstance(this).
                            registerReceiver(new ResponseReceiver(),
                                    new IntentFilter(UpdateDBService.
                                            UPDATE_NOTES_DB_SERVICE_BROADCAST));
                    this.startService(updateDBIntent);
                }
                break;
            case FetchTask.CREATE_NOTE:
                if (apiResponse.getResponseCode() == 200) {
                    getNotesFromAPI();
                } else {
                    Utilities.showToast(
                            getApplicationContext(),
                            getResources().getString(R.string.error_new_note),
                            Toast.LENGTH_SHORT);
                }
        }

    }

    /**
     * getNotes
     *
     * @param serviceTicketId the service ticket id to get the notes for
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
                findAll().sort(ServiceTicketNote.SERIAL_NUMBER, Sort.DESCENDING);

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
            setupRecyclerView();
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * onNoteCreated
     *
     * @param note the note text from the NoteDialog
     */
    public void onNoteCreated(String note) {
        FetchTask fetchTask = new FetchTask(onTaskCompleted);
        fetchTask.execute(FetchTask.CREATE_NOTE, ticketId,
                Integer.toString(MainActivity.EMPLOYEE_ID),
                MainActivity.EMPLOYEE_NAME, note);
    }

    /**
     * onTicketClosed
     */
    @SuppressWarnings("unused")
    public void onTicketClosed() {
        RealmConfiguration realmConfig = new RealmConfiguration.
                Builder(getApplicationContext()).build();
        Realm.setDefaultConfiguration(realmConfig);
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmResults<ServiceTicket> serviceTicket;

        serviceTicket = realm.where(ServiceTicket.class).
                equalTo(ServiceTicket.ID, ticketId).
                findAll();

        serviceTicket.get(0).setStatus("Closed");
        realm.commitTransaction();
    }

}
