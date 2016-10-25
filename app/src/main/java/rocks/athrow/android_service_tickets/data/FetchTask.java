package rocks.athrow.android_service_tickets.data;

import android.os.AsyncTask;

import rocks.athrow.android_service_tickets.BuildConfig;
import rocks.athrow.android_service_tickets.interfaces.OnTaskComplete;
import rocks.athrow.android_service_tickets.util.Utilities;

/**
 * FetchTask
 * Created by joselopez on 9/21/16.
 */
@SuppressWarnings("WeakerAccess")
public class FetchTask extends AsyncTask<String, Void, APIResponse> {
    public static final String VALIDATE_KEY = "validateKey";
    public static final String ALL_TICKETS = "allTickets";
    public static final String TICKET_NOTES = "ticketNotes";
    public static final String CREATE_NOTE = "createNote";
    public static final String START_TICKET = "startTicket";
    public static final String STOP_CLOSE_TICKET = "stopCloseTicket";
    public static final String STOP_TICKET = "stopTicket";
    public static final String CLOSE_TICKET = "closeTicket";
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
            case ALL_TICKETS:
                apiResponse = API.getAllServiceTickets();
                apiResponse.setMeta(ALL_TICKETS);
                break;
            case TICKET_NOTES:
                apiResponse = API.getNotesByTicket(String[1]);
                apiResponse.setMeta(TICKET_NOTES);
                break;
            case CREATE_NOTE:
                apiResponse = API.createNote(String[1], Integer.parseInt(String[2]), String[3], String[4]);
                apiResponse.setMeta(CREATE_NOTE);
                break;
            case START_TICKET:
                apiResponse = API.trackTime(String[1], String[2], String[3], String[4]);
                apiResponse.setMeta(START_TICKET);
                break;
            case STOP_TICKET:
                apiResponse = API.trackTime(String[1], String[2], String[3], String[4]);
                apiResponse.setMeta(STOP_TICKET);
                break;
            case STOP_CLOSE_TICKET:
                APIResponse timeApiResponse = API.trackTime(String[1], String[2], String[3], String[4]);
                APIResponse closeApiResponse = API.closeTicket(String[1], String[2], String[4]);
                if ( timeApiResponse.getResponseCode() == 200  && closeApiResponse.getResponseCode() == 200){
                    apiResponse.setResponseCode(200);
                }
                apiResponse.setMeta(STOP_CLOSE_TICKET);
                break;
            case CLOSE_TICKET:
                apiResponse = API.closeTicket(String[1], String[2], String[3]);
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