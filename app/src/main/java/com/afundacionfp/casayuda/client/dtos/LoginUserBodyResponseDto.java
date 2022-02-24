package com.afundacionfp.casayuda.client.dtos;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginUserBodyResponseDto {
    private int user_id;
    private String namee;
    private boolean is_worker;
    private  String token;

    public LoginUserBodyResponseDto(JSONObject object) throws JSONException {
        this.user_id= object.getInt("user_id");
        this.namee=object.getString("name");
        this.is_worker= object.getBoolean("is_worker");
        this.token=object.getString("token");

    }

    public int getUser_id() {
        return user_id;
    }

    public String getNamee() {
        return namee;
    }

    public boolean isIs_worker() {
        return is_worker;
    }

    public String getToken() {
        return token;
    }
}
