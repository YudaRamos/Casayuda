package com.afundacionfp.casayuda.screens.nearbyworker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.afundacionfp.casayuda.R;

/**
 * Nearby Workers RecyclerView Holder
 */
public class NearbyWorkersEntryViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener {
    // Holder Elements
    private int id;
    private final TextView name;
    private final TextView price;
    private final TextView services;

    /**
     * Associates the resources with
     * @param itemView receives a View with resources.
     */
    public NearbyWorkersEntryViewHolder(@NonNull View itemView){
        super(itemView);
        itemView.setOnClickListener(this);
        name = (TextView) itemView.findViewById(R.id.worker_name);
        price = (TextView) itemView.findViewById(R.id.worker_price);
        services = (TextView) itemView.findViewById(R.id.worker_services);
    }

    /**
     * Data loader
     * Load data entry elements into RecyclerView cell resources.
     * @param data Data entry ArrayList
     * @param activity Current Activity (Unused).
     */
    public void loadData(NearbyWorkersEntryData data, Activity activity) {
        name.setText(data.getName());
        price.setText(data.getPrice());
        services.setText(data.getServices());
        this.id = data.getId();
    }

    // From: https://stackoverflow.com/questions/24471109/recyclerview-onclick
    @Override
    public void onClick(View view) {
        Context context = view.getContext();
        Intent intent = new Intent(context.getApplicationContext(), WorkerDetailActivity.class);
        intent.putExtra("id", id);
        Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(context,
                android.R.anim.fade_in, android.R.anim.fade_out).toBundle();
        context.startActivity(intent, bundle);
    }
}
