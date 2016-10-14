package rocks.athrow.android_service_tickets.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import rocks.athrow.android_service_tickets.R;
import rocks.athrow.android_service_tickets.data.APIResponse;
import rocks.athrow.android_service_tickets.data.FetchTask;
import rocks.athrow.android_service_tickets.data.JSONParser;
import rocks.athrow.android_service_tickets.interfaces.OnTaskComplete;
import rocks.athrow.android_service_tickets.util.PreferencesHelper;
import rocks.athrow.android_service_tickets.util.Utilities;

import static android.R.attr.name;
import static android.view.View.GONE;

public class SettingsActivity extends AppCompatActivity implements OnTaskComplete {
    private final OnTaskComplete onTaskCompleted = this;

    LinearLayout apiEntryView;
    LinearLayout apiDisplayView;
    TextView employeeIdView;
    TextView employeeNameView;
    LinearLayout resetDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        apiEntryView = (LinearLayout) findViewById(R.id.api_entry_group);
        apiDisplayView = (LinearLayout) findViewById(R.id.api_display_group);
        resetDatabase = (LinearLayout) findViewById(R.id.reset_database);
        employeeIdView = (TextView) findViewById(R.id.employee_id);
        employeeNameView = (TextView) findViewById(R.id.employee_name);
        setupUi();
    }

    private void setupUi() {
        PreferencesHelper prefs = new PreferencesHelper(getApplicationContext());
        String key = prefs.loadString(Utilities.EMPLOYEE_KEY, Utilities.NULL);
        String id = prefs.loadString(Utilities.EMPLOYEE_ID, Utilities.NULL);
        String name = prefs.loadString(Utilities.EMPLOYEE_NAME, Utilities.NULL);
        if (id.equals(Utilities.NULL)) {
            apiEntryView.setVisibility(View.VISIBLE);
            apiDisplayView.setVisibility(GONE);
            employeeIdView.setText(getResources().getString(R.string.unknown));
            employeeNameView.setText(getResources().getString(R.string.unknown));
            resetDatabase.setVisibility(GONE);
        } else {
            apiEntryView.setVisibility(View.GONE);
            apiDisplayView.setVisibility(View.VISIBLE);
            resetDatabase.setVisibility(View.VISIBLE);
            TextView apiKeyDisplay = (TextView) findViewById(R.id.api_key_display);
            employeeIdView = (TextView) findViewById(R.id.employee_id);
            employeeNameView = (TextView) findViewById(R.id.employee_name);
            apiKeyDisplay.setText(key);
            employeeIdView.setText(id);
            employeeNameView.setText(name);
        }
    }

    public void validateAPIKey(View view) {
        EditText apiKeyEntry = (EditText) findViewById(R.id.api_key_entry);
        String apiKey = apiKeyEntry.getText().toString();
        apiEntryView.setVisibility(GONE);
        FetchTask fetchTask = new FetchTask(onTaskCompleted);
        fetchTask.execute(FetchTask.VALIDATE_KEY, apiKey);
    }

    /**
     * onTaskComplete
     *
     * @param apiResponse the API's server response object
     */
    private void onTaskComplete(APIResponse apiResponse) {
        if (apiResponse.getResponseCode() == 200) {
            String JSON = apiResponse.getResponseText();
            JSONArray jsonArray = JSONParser.getJSONArray(JSON);
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                String key = jsonObject.getString(Utilities.EMPLOYEE_KEY);
                String employeeName = jsonObject.getString(Utilities.EMPLOYEE_NAME);
                String employeeId = jsonObject.getString(Utilities.EMPLOYEE_ID);
                PreferencesHelper prefs = new PreferencesHelper(getApplicationContext());
                prefs.save(Utilities.EMPLOYEE_KEY, key);
                prefs.save(Utilities.EMPLOYEE_ID, employeeId);
                prefs.save(Utilities.EMPLOYEE_NAME, employeeName);
                setupUi();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            EditText apiKeyEntry = (EditText) findViewById(R.id.api_key_entry);
            apiKeyEntry.setText("");
            setupUi();
            Utilities.showToast(getApplicationContext(), getResources().getString(R.string.error), Toast.LENGTH_SHORT);
        }
    }

    /**
     * resetApiKeyButton
     *
     * @param view the button view
     */
    public void resetAPIKeyButton(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getResources().getString(R.string.reset_key_message))
                .setTitle(getResources().getString(R.string.reset_key_title));
        builder.setPositiveButton(getResources().getString(R.string.reset), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteAllTickets();
                EditText apiKeyEntry = (EditText) findViewById(R.id.api_key_entry);
                apiKeyEntry.setText("");
                PreferencesHelper prefs = new PreferencesHelper(getApplicationContext());
                prefs.save(Utilities.EMPLOYEE_KEY, Utilities.NULL);
                prefs.save(Utilities.EMPLOYEE_ID, Utilities.NULL);
                prefs.save(Utilities.EMPLOYEE_NAME, Utilities.NULL);
                setupUi();
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
     * deleteAllTicketsButton
     *
     * @param view the button view
     */
    public void deleteAllTicketsButton(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getResources().getString(R.string.delete_database_title))
                .setTitle(getResources().getString(R.string.delete_database));
        builder.setPositiveButton(getResources().getString(R.string.delete), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteAllTickets();
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
     * deleteAllTickets
     * A method to delete all the records from the database
     */
    private void deleteAllTickets() {
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(getApplicationContext()).build();
        Realm.setDefaultConfiguration(realmConfig);
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.deleteAll();
        realm.commitTransaction();
    }

    @Override
    public void OnTaskComplete(APIResponse apiResponse) {
        onTaskComplete(apiResponse);
    }
}
