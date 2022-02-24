package com.afundacionfp.casayuda.client.dtos.workerdetailbodyresponsedto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Response DTO: /version/1/workers/{id}
 */
public class WorkerDetailBodyResponseDto {
    private int userId;
    private int pricePerHour;
    private String name;
    private String surname;
    private List<String> jobs;
    private List<WorkerHiredBefore> hiredBefore;
    private List<WorkerComment> comments;

    public WorkerDetailBodyResponseDto(JSONObject data) throws JSONException {

        this.userId = data.getInt("user_id");
        this.pricePerHour = data.getInt("price_per_hour");
        this.name = data.getString("name");
        this.surname = data.getString("surname");

        this.jobs = new ArrayList<>();
        JSONArray jobs = data.getJSONArray("jobs");
        for (int i = 0; i < jobs.length(); i++) {
            this.jobs.add(jobs.getString(i));
        }

        this.hiredBefore = new ArrayList<>();
        JSONArray hiredBefore = data.getJSONArray("hired_before");
        for (int i = 0; i < hiredBefore.length(); i++) {
            JSONObject current = hiredBefore.getJSONObject(i);
            this.hiredBefore.add(new WorkerHiredBefore(
                    current.getString("datetime"),
                    current.getInt("hirer_id"),
                    current.getBoolean("is_accepted")));
        }

        this.comments = new ArrayList<>();
        JSONArray comments = data.getJSONArray("comments");
        for (int i = 0; i < comments.length(); i++) {
            JSONObject current = comments.getJSONObject(i);
            this.comments.add(new WorkerComment(
                    current.getInt("rating"),
                    current.getString("author_name"),
                    current.getString("comment")
            ));
        }

    }

    public int getUserId() {
        return userId;
    }

    public int getPricePerHour() {
        return pricePerHour;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public List<String> getJobs() {
        return jobs;
    }

    public List<WorkerHiredBefore> getHiredBefore() {
        return hiredBefore;
    }

    public List<WorkerComment> getComments() {
        return comments;
    }
}
