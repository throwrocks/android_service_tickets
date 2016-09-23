package rocks.athrow.android_service_tickets.data;

import android.os.AsyncTask;

import rocks.athrow.android_service_tickets.interfaces.OnTaskComplete;

/**
 * Created by joselopez on 9/21/16.
 */
public class FetchTask extends AsyncTask<String, Void, APIResponse> {
    public OnTaskComplete mListener = null;

    public FetchTask(OnTaskComplete listener) {
        this.mListener = listener;
    }

    @Override
    protected APIResponse doInBackground(String... String) {
        return API.getAllServiceTickets();
    }

    @Override
    protected void onPostExecute(APIResponse apiResponse) {
        super.onPostExecute(apiResponse);
        mListener.OnTaskComplete(apiResponse);
    }
}