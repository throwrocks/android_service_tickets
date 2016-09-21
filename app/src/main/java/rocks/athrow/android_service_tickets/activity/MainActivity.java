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

import org.json.JSONArray;

import rocks.athrow.android_service_tickets.R;
import rocks.athrow.android_service_tickets.data.APIResponse;
import rocks.athrow.android_service_tickets.data.FetchTask;
import rocks.athrow.android_service_tickets.data.JSONParser;
import rocks.athrow.android_service_tickets.data.UpdateDatabase;
import rocks.athrow.android_service_tickets.interfaces.OnTaskComplete;
import rocks.athrow.android_service_tickets.service.UpdateDBService;

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
            /*MoviesProvider moviesProvider = new MoviesProvider(getApplicationContext());
            mMovies = moviesProvider.query(MovieContract.MovieEntry.CONTENT_URI, null, null, null, null);
            View recyclerView = findViewById(R.id.movie_list);
            assert recyclerView != null;
            setupRecyclerView((RecyclerView) recyclerView);
            int numberOfColumns;
            if (mTwoPane) {
                numberOfColumns = 1;
            } else {
                numberOfColumns = 2;
            }
            ((RecyclerView) recyclerView).setLayoutManager(new GridLayoutManager(getApplicationContext(), numberOfColumns));
            ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getApplicationContext(), R.dimen.item_offset);
            ((RecyclerView) recyclerView).addItemDecoration(itemDecoration);
            setupRecyclerView((RecyclerView) recyclerView);
            mAdapter.notifyDataSetChanged();*/
        }
    }

    /**
     * setupRecyclerView
     * This method handles setting the adapter to the RecyclerView
     *
     * @param recyclerView the movie's list RecyclerView
     */
    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        //mAdapter = new MovieListAdapter(this, mTwoPane, mMovies);
        //recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void OnTaskComplete(APIResponse apiResponse) {
        onTaskComplete(apiResponse);
    }
}
