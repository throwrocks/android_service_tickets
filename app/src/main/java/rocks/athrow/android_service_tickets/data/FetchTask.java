package rocks.athrow.android_service_tickets.data;

import android.os.AsyncTask;

import rocks.athrow.android_service_tickets.interfaces.OnTaskComplete;

/**
 * FetchTask
 * Created by joselopez on 9/21/16.
 */
public class FetchTask extends AsyncTask<String, Void, APIResponse> {
    public static final String OPEN_TICKETS = "openTickets";
    public static final String TICKET_NOTES = "ticketNotes";
    private OnTaskComplete mListener = null;

    public FetchTask(OnTaskComplete listener) {
        this.mListener = listener;
    }

    @Override
    protected APIResponse doInBackground(String... String) {
        APIResponse apiResponse = new APIResponse();
        String type = String[0];
        switch (type) {
            case OPEN_TICKETS:
                apiResponse = API.getOpenServiceTickets();
                break;
            case TICKET_NOTES:
                apiResponse = API.getNotesByTicket(String[1]);
                break;
        }
        return apiResponse;
    }

    @Override
    protected void onPostExecute(APIResponse apiResponse) {
        super.onPostExecute(apiResponse);
        mListener.OnTaskComplete(apiResponse);
    }
}