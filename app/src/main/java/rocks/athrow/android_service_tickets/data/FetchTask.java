package rocks.athrow.android_service_tickets.data;

import android.os.AsyncTask;

import rocks.athrow.android_service_tickets.interfaces.OnTaskComplete;

/**
 * FetchTask
 * Created by joselopez on 9/21/16.
 */
public class FetchTask extends AsyncTask<String, Void, APIResponse> {
    public static final String VALIDATE_KEY = "validateKey";
    public static final String OPEN_TICKETS = "openTickets";
    public static final String TICKET_NOTES = "ticketNotes";
    public static final String CREATE_NOTE = "createNote";
    private static final String CLOSE_TICKET = "closeTicket";
    private OnTaskComplete mListener = null;

    public FetchTask(OnTaskComplete listener) {
        this.mListener = listener;
    }

    @Override
    protected APIResponse doInBackground(String... String) {
        APIResponse apiResponse = new APIResponse();
        String type = String[0];
        switch (type) {
            case VALIDATE_KEY:
                apiResponse = API.validateKey(String[1]);
                apiResponse.setMeta(VALIDATE_KEY);
                break;
            case OPEN_TICKETS:
                apiResponse = API.getOpenServiceTickets();
                apiResponse.setMeta(OPEN_TICKETS);
                break;
            case TICKET_NOTES:
                apiResponse = API.getNotesByTicket(String[1]);
                apiResponse.setMeta(TICKET_NOTES);
                break;
            case CREATE_NOTE:
                apiResponse = API.createNote(String[1], Integer.parseInt(String[2]), String[3], String[4]);
                apiResponse.setMeta(CREATE_NOTE);
                break;
            case CLOSE_TICKET:
                apiResponse = API.closeTicket(String[1]);
                apiResponse.setMeta(CLOSE_TICKET);
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