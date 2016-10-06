package rocks.athrow.android_service_tickets.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import rocks.athrow.android_service_tickets.R;
import rocks.athrow.android_service_tickets.data.APIResponse;
import rocks.athrow.android_service_tickets.data.FetchTask;
import rocks.athrow.android_service_tickets.data.JSONParser;
import rocks.athrow.android_service_tickets.interfaces.OnTaskComplete;
import rocks.athrow.android_service_tickets.util.PreferencesHelper;
import rocks.athrow.android_service_tickets.util.Utilities;

import static android.view.View.GONE;

public class SettingsActivity extends AppCompatActivity implements OnTaskComplete {
    private final OnTaskComplete onTaskCompleted = this;
    private static final String EMPLOYEE_KEY = "key";
    private static final String EMPLOYEE_ID = "employee_number";
    private static final String EMPLOYEE_NAME = "name";
    LinearLayout apiEntryView;
    LinearLayout apiDisplayView;
    LinearLayout employeeInfo;
    private final static boolean DEBUG = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        apiEntryView = (LinearLayout) findViewById(R.id.api_entry_group);
        apiDisplayView = (LinearLayout) findViewById(R.id.api_display_group);
        employeeInfo = (LinearLayout) findViewById(R.id.employee_info);
        setupUi();
    }

    private void setupUi() {
        PreferencesHelper prefs = new PreferencesHelper(getApplicationContext());
        String key = prefs.loadString(EMPLOYEE_KEY, "null");
        String id = prefs.loadString(EMPLOYEE_ID, "null");
        String name = prefs.loadString(EMPLOYEE_NAME, "null");
        if (id.equals("null")) {
            apiEntryView.setVisibility(View.VISIBLE);
            apiDisplayView.setVisibility(GONE);
            employeeInfo.setVisibility(View.GONE);
        } else {
            apiEntryView.setVisibility(View.GONE);
            apiDisplayView.setVisibility(View.VISIBLE);
            employeeInfo.setVisibility(View.VISIBLE);
            TextView apiKeyDisplay = (TextView) findViewById(R.id.api_key_display);
            TextView employeeIdView = (TextView) findViewById(R.id.employee_id);
            TextView employeeNameView = (TextView) findViewById(R.id.employee_name);
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

    private void onTaskComplete(APIResponse apiResponse) {
        if (apiResponse.getResponseCode() == 200) {
            String JSON = apiResponse.getResponseText();
            JSONArray jsonArray = JSONParser.getJSONArray(JSON);
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                String key = jsonObject.getString(EMPLOYEE_KEY);
                String employeeName = jsonObject.getString(EMPLOYEE_NAME);
                String employeeId = jsonObject.getString(EMPLOYEE_ID);
                PreferencesHelper prefs = new PreferencesHelper(getApplicationContext());
                prefs.save(EMPLOYEE_KEY, key);
                prefs.save(EMPLOYEE_ID, employeeId);
                prefs.save(EMPLOYEE_NAME, employeeName);
                setupUi();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            EditText apiKeyEntry = (EditText) findViewById(R.id.api_key_entry);
            apiKeyEntry.setText("");
            setupUi();
            Utilities.showToast(getApplicationContext(), "Error", Toast.LENGTH_SHORT);
        }
    }

    public void resetAPIKey(View view) {
        EditText apiKeyEntry = (EditText) findViewById(R.id.api_key_entry);
        apiKeyEntry.setText("");
        PreferencesHelper prefs = new PreferencesHelper(getApplicationContext());
        prefs.save(EMPLOYEE_KEY, "null");
        prefs.save(EMPLOYEE_ID, "null");
        prefs.save(EMPLOYEE_NAME, "null");
        setupUi();
    }

    @Override
    public void OnTaskComplete(APIResponse apiResponse) {
        onTaskComplete(apiResponse);
    }
}
