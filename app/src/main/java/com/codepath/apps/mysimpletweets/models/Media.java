package com.codepath.apps.mysimpletweets.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by lin1000 on 2017/3/5.
 */

public class Media {
    //list all attributes
    private String mediaUrl;
    private int medium_w;
    private int medium_h;

    public static Media fromJSONObject(JSONObject jsonObject){
        Media m =  new Media();
        if(jsonObject!=null){
            try {
                m.mediaUrl = jsonObject.getString("media_url");
                m.medium_w = jsonObject.getJSONObject("sizes").getJSONObject("medium").getInt("w");
                m.medium_h = jsonObject.getJSONObject("sizes").getJSONObject("medium").getInt("h");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return m;
    }

    public static ArrayList<Media> fromJSONArray(JSONArray jsonArray){
        ArrayList<Media> medias = new ArrayList<Media>();

        for(int i=0 ; i<jsonArray.length();i++){
            try {
                JSONObject mediaJsonObject = jsonArray.getJSONObject(i);
                if(mediaJsonObject!=null){
                    medias.add(Media.fromJSONObject(mediaJsonObject));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return medias;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public int getMedium_w() {
        return medium_w;
    }

    public int getMedium_h() {
        return medium_h;
    }
}
