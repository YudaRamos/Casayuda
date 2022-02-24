package com.afundacionfp.casayuda.client.dtos;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ObjectSelfEmployedDto {
    private int userId, pricePerHour;
    private String name, surname;
    private List<String> jobs;

    public ObjectSelfEmployedDto(int userId, int pricePerHour, String name, String surname, List<String> jobs) {
        this.userId = userId;
        this.name = name;
        this.surname = surname;
        this.jobs = jobs;
        this.pricePerHour = pricePerHour;
    }

    public ObjectSelfEmployedDto(JSONObject json) throws JSONException {
        this.userId = json.getInt("user_id");
        this.name = json.getString("name");
        this.surname = json.getString("surname");
        this.pricePerHour = json.getInt("price_per_hour");
        this.jobs = new ArrayList<>();

        // Get the JSONArray from the JSONObject
        JSONArray jobsJson = json.getJSONArray("jobs");
        // Add each job to ArrayList
        for (int i = 0; i < jobsJson.length(); i++) {
            jobs.add(jobsJson.getString(i));
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
}
