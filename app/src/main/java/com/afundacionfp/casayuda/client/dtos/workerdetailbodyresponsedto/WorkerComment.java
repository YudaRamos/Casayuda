package com.afundacionfp.casayuda.client.dtos.workerdetailbodyresponsedto;

/**
 * Stores worker comments
 */
public class WorkerComment {
    private int rating;
    private String authorName;
    private String comment;

    public WorkerComment(int rating, String authorName, String comment) {
        this.rating = rating;
        this.authorName = authorName;
        this.comment = comment;
    }
    public int getRating() {
        return rating;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getComment() {
        return comment;
    }
}
