package com.codepath.apps.mysimpletweets.fragments;

import android.util.Log;

import com.codepath.apps.mysimpletweets.TwitterApplication;

import static com.loopj.android.http.AsyncHttpClient.log;

/**
 * Created by lin1000 on 2017/3/10.
 */

public class MyTweetsFragment extends TweetsListFragment {

    private String screenName;

    //send api quest to get mentions
    public void populateTimeline(int count , long since_id, long max_id){
        Log.d("DEBUG", "populateTimeline=max_id="+max_id);
        log.d("DEBUG", "populateTimeline MyTweetsFragement.screenName="+ screenName );
        TwitterApplication.getRestClient().getUserTimeline(screenName, count, since_id, max_id,new TweetsListFragment.DefaultJsonHttpResponseHandler());
    }

    public void setScreenName(String screenName){
        log.d("DEBUG", "setScreenName MyTweetsFragment.screenName="+ screenName );
        this.screenName = screenName;
    }


}
