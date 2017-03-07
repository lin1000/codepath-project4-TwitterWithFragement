package com.codepath.apps.mysimpletweets.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lin1000 on 2017/3/5.
 */

public class User {
    //list the attributes
    private String name;
    private long uid;
    private String screenName;
    private String profileImageUrl;


    //deserialize user json into User object
    public static User fromJSON(JSONObject jsonObject){
        User u = new User();
        //extract
        try {
            u.name = jsonObject.getString("name");
            u.uid = jsonObject.getLong("id");
            u.screenName = jsonObject.getString("screen_name");
            u.profileImageUrl = jsonObject.getString("profile_image_url");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return u;
    }

    public String getName() {
        return name;
    }

    public long getUid() {
        return uid;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }
}
