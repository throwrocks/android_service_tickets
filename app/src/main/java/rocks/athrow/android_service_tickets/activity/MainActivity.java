package rocks.athrow.android_service_tickets.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.facebook.stetho.Stetho;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;


import io.realm.RealmResults;
import rocks.athrow.android_service_tickets.R;
import rocks.athrow.android_service_tickets.adapter.ServiceTicketsListAdapter;
import rocks.athrow.android_service_tickets.data.APIResponse;
import rocks.athrow.android_service_tickets.data.FetchTask;
import rocks.athrow.android_service_tickets.data.ServiceTicket;
import rocks.athrow.android_service_tickets.interfaces.OnTaskComplete;
import rocks.athrow.android_service_tickets.service.UpdateDBService;

public class MainActivity extends AppCompatActivity implements OnTaskComplete {
    ServiceTicketsListAdapter mAdapter;
    RealmResults<ServiceTicket> mRealmResults;
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
            Intent updateDBIntent = new Intent(this, UpdateDBService.class);
            switch (apiResponse.getResponseCode()) {
                case 200:
                    String responseText = apiResponse.getResponseText();
                    updateDBIntent.putExtra("serviceTicketsJSON", responseText);
                    LocalBroadcastManager.getInstance(this).registerReceiver(new ResponseReceiver(), new IntentFilter("UpdateServiceTicketsBroadcast"));
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
            String x = "";
            /* RealmConfiguration realmConfig = new RealmConfiguration.Builder(getContext()).build();
        Realm.setDefaultConfiguration(realmConfig);
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmResults<RealmReview> reviews = realm.where(RealmReview.class).findAll().sort(MODULE_COMPLETED_AT, Sort.DESCENDING);
        realm.commitTransaction();*/
        }
    }

    /**
     * setupRecyclerView
     * This method handles setting the adapter to the RecyclerView
     *
     * @param recyclerView the movie's list RecyclerView
     */
    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        mAdapter = new ServiceTicketsListAdapter(this, mRealmResults);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void OnTaskComplete(APIResponse apiResponse) {
        onTaskComplete(apiResponse);
    }
}
