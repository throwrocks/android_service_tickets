package rocks.athrow.android_service_tickets.interfaces;

import rocks.athrow.android_service_tickets.data.APIResponse;

/**
 * OnTaskComplete
 * Created by joselopez on 9/21/16.
 */
public interface OnTaskComplete {
    @SuppressWarnings("MethodNameSameAsClassName")
    void OnTaskComplete(APIResponse apiResponses);
}