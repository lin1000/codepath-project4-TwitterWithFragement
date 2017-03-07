package com.codepath.apps.mysimpletweets.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by lin1000 on 2017/3/5.
 */

public class ExtendedEntities {
    //list all attributes
    private ArrayList<Media> media;

    public static ExtendedEntities formJSONObject(JSONObject jsonObject){
        ExtendedEntities extendedEntities = new ExtendedEntities();
        try {
            extendedEntities.media = Media.fromJSONArray(jsonObject.getJSONArray("media"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return extendedEntities;
    }

    public ArrayList<Media> getMedia() {
        return media;
    }
}
