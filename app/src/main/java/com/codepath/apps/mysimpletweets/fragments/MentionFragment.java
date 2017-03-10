package com.codepath.apps.mysimpletweets.fragments;

import android.util.Log;

import com.codepath.apps.mysimpletweets.TwitterApplication;

/**
 * Created by lin1000 on 2017/3/8.
 */

public class MentionFragment extends TweetsListFragment {
    //send api quest to get mentions
    //populate listview by creating tweets object from json
    public void populateTimeline(int count , long since_id, long max_id){
        Log.d("DEBUG", "populateTimeline=max_id="+max_id);
        TwitterApplication.getRestClient().getUserMention(count, since_id, max_id,new DefaultJsonHttpResponseHandler());
    }
}
