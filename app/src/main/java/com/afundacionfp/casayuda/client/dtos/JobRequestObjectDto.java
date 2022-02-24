package com.afundacionfp.casayuda.client.dtos;

import org.json.JSONException;
import org.json.JSONObject;

public class JobRequestObjectDto {
    private int requestId;
    private String hirerName;
    private String requestedDatetime;
    private String message;
    private boolean isAccepted;

    /**
     * Constructor using json objects
     * @param json
     * @throws JSONException
     */
    public JobRequestObjectDto(JSONObject json) throws JSONException {
        this.requestId = json.getInt("request_id");
        this.hirerName = json.getString("hirer_name");
        this.requestedDatetime = json.getString("requested_datetime");
        this.message = json.getString("message");
        this.isAccepted = json.getBoolean("is_accepted");
    }

    public JobRequestObjectDto(int requestId, String hirerName, String requestedDatetime, String message, boolean isAccepted) {
        this.requestId = requestId;
        this.hirerName = hirerName;
        this.requestedDatetime = requestedDatetime;
        this.message = message;
        this.isAccepted = isAccepted;
    }

    public int getRequestId() {
        return requestId;
    }

    public String getHirerName() {
        return hirerName;
    }

    public String getRequestedDatetime() {
        return requestedDatetime;
    }

    public String getMessage() {
        return message;
    }

    public boolean getIsAccepted() {
        return isAccepted;
    }
}