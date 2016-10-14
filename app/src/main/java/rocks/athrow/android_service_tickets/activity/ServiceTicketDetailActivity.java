package rocks.athrow.android_service_tickets.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
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
import rocks.athrow.android_service_tickets.data.Ticket;
import rocks.athrow.android_service_tickets.data.TicketNote;
import rocks.athrow.android_service_tickets.interfaces.OnTaskComplete;
import rocks.athrow.android_service_tickets.realmadapter.RealmNotesListAdapter;
import rocks.athrow.android_service_tickets.service.UpdateDBService;
import rocks.athrow.android_service_tickets.util.PreferencesHelper;
import rocks.athrow.android_service_tickets.util.Utilities;

import static android.view.View.GONE;


public class ServiceTicketDetailActivity extends AppCompatActivity implements OnTaskComplete {
    private final OnTaskComplete onTaskCompleted = this;
    private NotesAdapter mAdapter;
    private RealmResults<TicketNote> mRealmResults;
    private String ticketId;
    private String employeeId;
    private String employeeName;
    private int timeTrackStatus;
    private TextView ticketStatus;
    private TextView notesLabelView;
    private TextView timeTrackerView;
    private Button timeTrackerStart;
    private Button timeTrackerStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_ticket_detail);

        PreferencesHelper prefs = new PreferencesHelper(getApplicationContext());
        employeeId = prefs.loadString(Utilities.EMPLOYEE_ID, Utilities.NULL);
        employeeName = prefs.loadString(Utilities.EMPLOYEE_NAME, Utilities.NULL);

        Intent intent = getIntent();
        Bundle arguments = intent.getExtras();
        // Set the variables
        ticketId = arguments.getString(Ticket.ID);
        final String serialNumber = arguments.getString(Ticket.SERIAL_NUMBER);
        final String priority = arguments.getString(Ticket.PRIORITY);
        String status = arguments.getString(Ticket.STATUS);
        final String technician = arguments.getString(Ticket.TECH_NAME);
        final String createdDate = arguments.getString(Ticket.CREATED_DATE);
        final String assignedDate = arguments.getString(Ticket.ASSIGNED_DATE);
        @SuppressWarnings("UnusedAssignment") final String closedDate = arguments.getString(Ticket.CLOSED_DATE);
        final String org = arguments.getString(Ticket.ORG);
        final String site = arguments.getString(Ticket.SITE);
        final String description = arguments.getString(Ticket.DESCRIPTION);
        final String issues = arguments.getString(Ticket.ISSUES);
        final String issuesDisplay = Utilities.getBulletedList(issues, ",", 2);
        timeTrackStatus = arguments.getInt(Ticket.TIME_TRACK_STATUS);

        TextView ticketSerialNumber = (TextView) findViewById(R.id.ticket_number);
        timeTrackerView = (TextView) findViewById(R.id.time_track);
        TextView ticketPriority = (TextView) findViewById(R.id.priority);
        ticketStatus = (TextView) findViewById(R.id.status);
        TextView ticketTechnician = (TextView) findViewById(R.id.technician);
        TextView ticketCreatedDate = (TextView) findViewById(R.id.created_date);
        TextView ticketAssignedDate = (TextView) findViewById(R.id.assigned_date);
        TextView ticketOrg = (TextView) findViewById(R.id.org);
        TextView ticketSite = (TextView) findViewById(R.id.site);
        TextView ticketIssues = (TextView) findViewById(R.id.issues);
        TextView ticketDescription = (TextView) findViewById(R.id.description);
        Button createNote = (Button) findViewById(R.id.create_note);
        notesLabelView = (TextView) findViewById(R.id.notes_label);
        timeTrackerStart = (Button) findViewById(R.id.time_tracker_start);
        timeTrackerStop = (Button) findViewById(R.id.time_tracker_stop);

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
        setUpTrackerView();

        createNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NoteDialog noteDialog = new NoteDialog();
                Bundle args = new Bundle();
                args.putString(Ticket.ID, ticketId);
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
            notesLabelView.setVisibility(View.VISIBLE);
            notesLabelView.setText(getResources().getString(R.string.label_notes));
            mAdapter = new NotesAdapter(getApplicationContext());
            RealmNotesListAdapter realmNotesListAdapter =
                    new RealmNotesListAdapter(getApplicationContext(), mRealmResults);
            mAdapter.setRealmAdapter(realmNotesListAdapter);
            RecyclerView ticketNotesList = (RecyclerView) findViewById(R.id.notes_list);
            assert ticketNotesList != null;
            ticketNotesList.setAdapter(mAdapter);
        } else {
            notesLabelView.setVisibility(GONE);
        }
    }

    /**
     * onTaskComplete
     * Callback method to handle results from the fetch task and the note dialog
     *
     * @param apiResponse the APIResponse object
     */
    private void onTaskComplete(APIResponse apiResponse) {
        int responseCode = apiResponse.getResponseCode();
        String meta = apiResponse.getMeta();
        switch (meta) {
            case FetchTask.TICKET_NOTES:
                if (responseCode == 200) {
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
                if (responseCode == 200) {
                    getNotesFromAPI();
                } else {
                    Utilities.showToast(
                            getApplicationContext(),
                            getResources().getString(R.string.error_new_note),
                            Toast.LENGTH_SHORT);
                }
                break;
            case FetchTask.START_TICKET:
                if (responseCode == 200) {
                    timeTrackStatus = 1;
                    setUpTrackerView();
                } else {
                    Utilities.showToast(
                            getApplicationContext(),
                            getResources().getString(R.string.error_starting_ticket),
                            Toast.LENGTH_SHORT);
                }
                break;
            case FetchTask.STOP_TICKET:
                if (responseCode == 200) {
                    timeTrackStatus = 0;
                    setUpTrackerView();
                } else {
                    Utilities.showToast(
                            getApplicationContext(),
                            getResources().getString(R.string.error_stopping_ticket),
                            Toast.LENGTH_SHORT);
                }
                break;
            case FetchTask.STOP_CLOSE_TICKET:
                if (responseCode == 200) {
                    timeTrackStatus = 0;
                    setUpTrackerView();
                    onTicketClosed();
                } else {
                    Utilities.showToast(
                            getApplicationContext(),
                            getResources().getString(R.string.error_closing_ticket),
                            Toast.LENGTH_SHORT);
                }
                break;
        }
    }

    /**
     * getNotes
     *
     * @param serviceTicketId the service ticket id to get the notes for
     * @return the RealmResults
     */
    private RealmResults<TicketNote> getNotes(String serviceTicketId) {
        RealmConfiguration realmConfig = new RealmConfiguration.
                Builder(getApplicationContext()).build();
        Realm.setDefaultConfiguration(realmConfig);
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmResults<TicketNote> ticketNotes;

        ticketNotes = realm.where(TicketNote.class).
                equalTo(TicketNote.SERVICE_TICKET_ID, serviceTicketId).
                findAll().sort(TicketNote.SERIAL_NUMBER, Sort.DESCENDING);

        realm.commitTransaction();
        return ticketNotes;
    }


    /**
     * onNoteCreated
     *
     * @param note the note text from the NoteDialog
     */
    public void onNoteCreated(String note) {
        FetchTask fetchTask = new FetchTask(onTaskCompleted);
        fetchTask.execute(FetchTask.CREATE_NOTE, ticketId, employeeId, employeeName, note);
    }

    /**
     * onTicketClosed
     */
    public void onTicketClosed() {
        RealmConfiguration realmConfig = new RealmConfiguration.
                Builder(getApplicationContext()).build();
        Realm.setDefaultConfiguration(realmConfig);
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmResults<Ticket> ticket;

        ticket = realm.where(Ticket.class).
                equalTo(Ticket.ID, ticketId).
                findAll();

        String status = "Closed";
        ticket.get(0).setStatus(status);
        realm.commitTransaction();
        ticketStatus.setText(status);
        Utilities.formatStatusView(ticketStatus,status,getApplicationContext());
    }

    /**
     * setUpTrackerView
     */
    private void setUpTrackerView() {
        if (timeTrackStatus == 1) {
            timeTrackerStart.setVisibility(GONE);
            timeTrackerStop.setVisibility(View.VISIBLE);
            timeTrackerView.setVisibility(View.VISIBLE);
            Utilities.formatInProgress(timeTrackerView, getApplicationContext());
        } else {
            timeTrackerStart.setVisibility(View.VISIBLE);
            timeTrackerStop.setVisibility(GONE);
            timeTrackerView.setVisibility(GONE);
        }
    }

    /**
     * startTicket
     * @param view the button view
     */
    public void startTicket(@SuppressWarnings("UnusedParameters") View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getResources().getString(R.string.start_ticket_message))
                .setTitle(getResources().getString(R.string.start_ticket));
        builder.setPositiveButton(getResources().getString(R.string.start), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                FetchTask fetchTask = new FetchTask(onTaskCompleted);
                fetchTask.execute(FetchTask.START_TICKET,
                        ticketId,
                        employeeName,
                        getResources().getString(R.string.start_time),
                        Utilities.getDateAsString(new java.util.Date(), Utilities.FMDataFormat, null));
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * stopTicket
     * @param view the button view
     */
    public void stopTicket(@SuppressWarnings("UnusedParameters") View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getResources().getString(R.string.stop_ticket_messange))
                .setTitle(getResources().getString(R.string.stop_ticket));
        builder.setPositiveButton(getResources().getString(R.string.close_ticket), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                FetchTask fetchTask = new FetchTask(onTaskCompleted);
                fetchTask.execute(FetchTask.STOP_CLOSE_TICKET,
                        ticketId,
                        employeeName,
                        getResources().getString(R.string.stop_time),
                        Utilities.getDateAsString(new java.util.Date(),Utilities.FMDataFormat, null));
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.reschedule_ticket), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                FetchTask fetchTask = new FetchTask(onTaskCompleted);
                fetchTask.execute(FetchTask.STOP_TICKET,
                        ticketId,
                        employeeName,
                        getResources().getString(R.string.stop_time),
                        Utilities.getDateAsString(new java.util.Date(), Utilities.FMDataFormat, null));
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
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
}
