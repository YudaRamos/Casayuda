package com.afundacionfp.casayuda.screens.comment;

import org.json.JSONException;
import org.json.JSONObject;

// This class contains the data of each comment
public class CommentData {
    private String author;
    private String comment;
    private float rating;

    public CommentData(String author, String comment, float rating) {
        this.author = author;
        this.comment = comment;
        this.rating = rating;
    }

    public CommentData(JSONObject json) {
        try {
            author = json.getString("author_name");
            comment = json.getString("comment");
            rating = (float) json.getDouble("rating");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getAuthor() {
        return author;
    }

    public String getComment() {
        return comment;
    }

    public float getRating() {
        return rating;
    }
}
