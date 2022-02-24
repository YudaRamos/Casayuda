package com.afundacionfp.casayuda.screens.comment;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.afundacionfp.casayuda.R;

import java.util.List;

public class CommentsRecyclerViewAdapter extends RecyclerView.Adapter<CommentsViewHolder> {

    private List<CommentData> todaAData;
    private Activity activity;

    public CommentsRecyclerViewAdapter(List<CommentData> dataset, Activity activity) {
        this.todaAData = dataset;
        this.activity = activity;
    }

    @NonNull
    @Override

    public CommentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comments_view_holder, parent, false);
        return new CommentsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsViewHolder holder, int position) {
        CommentData dataInPositionToBeRendered = todaAData.get(position);
        holder.mostrarData(dataInPositionToBeRendered, activity);

    }

    @Override
    public int getItemCount() {
        return todaAData.size();
    }
}
