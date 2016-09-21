package rocks.athrow.android_service_tickets.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * JSONParser
 * Created by joselopez on 9/21/16.
 */
public class JSONParser {

    public static JSONArray parseServiceTickets(String JSON){
        JSONArray jsonArray = null;
        try {
            JSONObject jsonObject = new JSONObject(JSON);
            jsonArray = jsonObject.getJSONArray("data");
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        return jsonArray;
    }

}
