package rocks.athrow.android_service_tickets.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import rocks.athrow.android_service_tickets.R;
import rocks.athrow.android_service_tickets.data.ServiceTicket;

/**
 * Created by joselopez on 9/21/16.
 */
public class ServiceTicketsListAdapter extends RealmRecyclerViewAdapter<ServiceTicket> {
    private Context mContext;

    private class ViewHolder extends RecyclerView.ViewHolder {

        // Declare views

        public ViewHolder(View view) {
            super(view);
            // Initialize views
        }
    }


    public ServiceTicketsListAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View reviewListRecyclerView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.service_ticket_item, parent, false);
        return new ViewHolder(reviewListRecyclerView);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ViewHolder serviceTicketRecyclerView = (ViewHolder) viewHolder;
        ServiceTicket serviceTicket = getItem(position);
        // Get the record

        //------------------------------------------------------------------------------------------
        // Set thr variables
        //------------------------------------------------------------------------------------------

        //------------------------------------------------------------------------------------------
        // Set the views
        //------------------------------------------------------------------------------------------

        // Set clic listener
        /*reviewListRecyclerView.viewReviewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewDetailsActivity = new Intent(context, ReviewsDetailActivity.class);
                viewDetailsActivity.putExtra("id", idString);
                viewDetailsActivity.putExtra("project_name", projectName);
                viewDetailsActivity.putExtra("price", price);
                viewDetailsActivity.putExtra("assigned_at", assignedAtDetailDisplay);
                viewDetailsActivity.putExtra("completed_at", completedAtDetailDisplay);
                viewDetailsActivity.putExtra("user_name", userName);
                viewDetailsActivity.putExtra("result", result);
                viewDetailsActivity.putExtra("archive_url", archiveUrl);
                viewDetailsActivity.putExtra("filename", fileName);
                viewDetailsActivity.putExtra("elapsed_time", elapsedTime);
                viewDetailsActivity.putExtra("notes", studentNotes);
                viewDetailsActivity.putExtra("rating", rating);
                context.startActivity(viewDetailsActivity);
            }
        });*/

    }
    /* The inner RealmBaseAdapter
     * view reports_count is applied here.
     *
     * getRealmAdapter is defined in RealmRecyclerViewAdapter.
     */
    @Override
    public int getItemCount() {
        if (getRealmAdapter() != null) {
            return getRealmAdapter().getCount();
        }
        return 0;
    }
}
