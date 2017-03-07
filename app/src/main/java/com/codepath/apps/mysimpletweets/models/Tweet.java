package com.codepath.apps.mysimpletweets.models;

import android.text.format.DateUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by lin1000 on 2017/3/5.
 */

//Parse the JSON  + Store the data  + encapsulate logics
public class Tweet {
    // list out the attributes
    private String body;
    private long uid; //unique id for the tweet
    private User user;
    private String createdAt;
    private ExtendedEntities extendedEntities;

    //deserialize the json
    //Tweet.fromJSON("{...}") => <Tweet>
    public static Tweet fromJSON(JSONObject jsonObject){
        Tweet tweet = new Tweet();
        //Extract the values from
        try {
            tweet.body = jsonObject.getString("text");
            tweet.uid = jsonObject.getLong("id");
            tweet.createdAt = jsonObject.getString("created_at");
            tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));

            try {
                jsonObject.getJSONObject("extended_entities");
            } catch (JSONException e) {
                //for tweets with org.json.JSONException: No value for extended_entities
                Log.d("DEBUG", "Catch No Value on extended_entities");
                tweet.extendedEntities = null;
            }
            tweet.extendedEntities = ExtendedEntities.formJSONObject(jsonObject.getJSONObject("extended_entities"));

            tweet.createdAt = jsonObject.getString("created_at");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Return the tweet object
        return tweet;
    }

    public static ArrayList<Tweet> fromJSONArray(JSONArray jsonArray){
        ArrayList<Tweet> tweets = new ArrayList<>();

        for(int i=0 ; i < jsonArray.length() ;i++){
            try {
                JSONObject tweetJson = jsonArray.getJSONObject(i);
                Tweet tweet = Tweet.fromJSON(tweetJson);
                if(tweet != null){
                    tweets.add(tweet);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
        }

        return tweets;
    }

    public String getBody() {
        return body;
    }

    public long getUid() {
        return uid;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public User getUser() {
        return user;
    }

    public ExtendedEntities getExtendedEntities() {
        return extendedEntities;
    }

    public String getRelativeTimeAgo() {
        String rawJsonDate = this.createdAt;
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }
}
