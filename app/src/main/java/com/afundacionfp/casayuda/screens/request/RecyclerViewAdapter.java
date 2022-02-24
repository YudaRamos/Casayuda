package com.afundacionfp.casayuda.screens.request;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.afundacionfp.casayuda.R;
import com.afundacionfp.casayuda.client.dtos.JobRequestObjectDto;
import com.afundacionfp.casayuda.screens.nearbyworker.NearbyWorkersEntryData;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<JobRequestObjectDto> mData;
    private LayoutInflater inflater;
    private Context context;

    /**
     * Default constructor
     * @param itemList Data recived
     * @param context Context of the activity
     */
    public RecyclerViewAdapter(List<JobRequestObjectDto> itemList, Context context) {
        this.mData = itemList;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    /**
     * @return The size of the data
     */
    @Override
    public int getItemCount() {
        return mData.size();
    }

    /**
     * Method to create a ViewHolder
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recycler_view_element, null);
        return new RecyclerViewAdapter.ViewHolder(view);
    }

    public void updateData(List<JobRequestObjectDto> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    /**
     * Use the data saved and post it in the cells
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(RecyclerViewAdapter.ViewHolder holder, int position) {
        holder.bindData(mData.get(position));
        holder.launchSeeRequestActivity(mData.get(position));
    }

    /**
     * Class to create a viewholder to use with the adapter
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView hirer;
        TextView date;

        /**
         * Default constrcutor to save the ids
         *
         * @param itemView
         */
        ViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            hirer = itemView.findViewById(R.id.hirerElement);
            date = itemView.findViewById(R.id.dateElement);
        }

        /**
         * Data in every cell
         * @param jobRequest
         */
        void bindData(final JobRequestObjectDto jobRequest) {
            hirer.setText(jobRequest.getHirerName());
            date.setText(jobRequest.getRequestedDatetime());
        }

        /**
         * When clicked in the cell, it launchs the SeeRequestsActivity passing the data from
         * YourRequestsActivity.
         *
         * @param jobRequestObjectDto Object to pass when clicked
         */
        public void launchSeeRequestActivity(JobRequestObjectDto jobRequestObjectDto) {
            cardView.setOnClickListener(v -> {
                Intent seeRequestActivity = new Intent(v.getContext(), SeeRequestActivity.class);
                seeRequestActivity.putExtra("hirer", jobRequestObjectDto.getHirerName());
                seeRequestActivity.putExtra("date", jobRequestObjectDto.getRequestedDatetime());
                seeRequestActivity.putExtra("message", jobRequestObjectDto.getMessage());
                seeRequestActivity.putExtra("id", jobRequestObjectDto.getRequestId());

                v.getContext().startActivity(seeRequestActivity);

            });
        }

    }
}
