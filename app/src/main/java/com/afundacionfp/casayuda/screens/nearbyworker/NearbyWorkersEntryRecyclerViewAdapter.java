package com.afundacionfp.casayuda.screens.nearbyworker;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.afundacionfp.casayuda.R;

/**
 * Nearby Worker RecyclerView Adapter
 */
public class NearbyWorkersEntryRecyclerViewAdapter extends RecyclerView.Adapter<NearbyWorkersEntryViewHolder> {

    private List<NearbyWorkersEntryData> dataSet;
    private Activity activity;

    public NearbyWorkersEntryRecyclerViewAdapter(List<NearbyWorkersEntryData> dataSet, Activity activity) {
        this.dataSet = dataSet;
        this.activity = activity;
    }
    @NonNull
    @Override
    public NearbyWorkersEntryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.nearby_worker_view_entry_holder, parent, false);
        return new NearbyWorkersEntryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NearbyWorkersEntryViewHolder holder, int position) {
        NearbyWorkersEntryData entry = dataSet.get(position);
        holder.loadData(entry, activity);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
