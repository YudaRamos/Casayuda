package com.afundacionfp.casayuda.screens.comment;

import android.app.Activity;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.afundacionfp.casayuda.R;

public class CommentsViewHolder extends RecyclerView.ViewHolder {
    private final TextView textViewAuthor, textViewComments;
    private RatingBar ratingBar;
    private Activity activity;

    public CommentsViewHolder(@NonNull View itemView) {

        super(itemView);
        textViewAuthor = itemView.findViewById(R.id.comments_name_author);
        textViewComments = itemView.findViewById(R.id.comments_comment);
        ratingBar = itemView.findViewById(R.id.comments_rating);

    }

    public void mostrarData(CommentData data, Activity activity) {
        textViewAuthor.setText(data.getAuthor());
        textViewComments.setText(data.getComment());
        ratingBar.setRating(data.getRating());

        this.activity = activity;
    }
}
