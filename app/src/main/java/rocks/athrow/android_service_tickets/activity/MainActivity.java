package rocks.athrow.android_service_tickets.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.facebook.stetho.Stetho;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

import org.json.JSONArray;

import rocks.athrow.android_service_tickets.R;
import rocks.athrow.android_service_tickets.data.APIResponse;
import rocks.athrow.android_service_tickets.data.FetchTask;
import rocks.athrow.android_service_tickets.data.JSONParser;
import rocks.athrow.android_service_tickets.data.UpdateDatabase;
import rocks.athrow.android_service_tickets.interfaces.OnTaskComplete;

public class MainActivity extends AppCompatActivity implements OnTaskComplete {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
                        .build());


        OnTaskComplete onTaskCompleted = this;
        FetchTask fetchTask = new FetchTask(onTaskCompleted);
        fetchTask.execute();

    }
    public void onTaskComplete(APIResponse apiResponse) {
        if (apiResponse != null) {
            //Intent updateDBIntent = new Intent(this, UpdateDBService.class);
            switch (apiResponse.getResponseCode()) {
                case 200:
                    String responseText = apiResponse.getResponseText();
                    JSONArray jsonArray = JSONParser.parseServiceTickets(responseText);
                    UpdateDatabase update = new UpdateDatabase(getApplicationContext());
                    update.updateServiceTickets(jsonArray);
                    //updateDBIntent.putExtra(INTENT_EXTRA, apiResponse.getResponseText());
                    //LocalBroadcastManager.getInstance(this).registerReceiver(new ResponseReceiver(), new IntentFilter(UPDATE_DB_BROADCAST));
                    //this.startService(updateDBIntent);
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
    @Override
    public void OnTaskComplete(APIResponse apiResponse) {
        onTaskComplete(apiResponse);
    }
}
