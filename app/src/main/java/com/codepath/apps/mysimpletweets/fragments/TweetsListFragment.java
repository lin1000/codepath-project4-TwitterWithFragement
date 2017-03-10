package com.codepath.apps.mysimpletweets.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.adapters.TweetsArrayAdapter;
import com.codepath.apps.mysimpletweets.listeners.AbstractEndlessScrollListener;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by lin1000 on 2017/3/8.
 */

public abstract class TweetsListFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";
    private int mPage;

    protected long oldestTweetId=1;
    protected int perRequestTweetCount = 20;

    protected ArrayList<Tweet> tweets;
    protected TweetsArrayAdapter tweetsAdapter;
    protected ListView lvTweets;
    protected ImageView ivRateLimit;

    protected class DefaultJsonHttpResponseHandler extends JsonHttpResponseHandler{

            //success
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                Log.d("DEBUG", "statusCode X JSON = "+json.toString());
                //deserialize
                //create model
                //load into view
                ArrayList<Tweet> tweets = Tweet.fromJSONArray(json);
                if (tweets!=null && tweets.size()>1) {
                    Tweet oldestTweet = tweets.get(tweets.size()-1);
                    setOldestTweetId(oldestTweet.getUid());
                    Log.d("DEBUG","oldestTweetId="+ oldestTweetId);
                }
                addAll(tweets);
            }

            //failure
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject
            errorResponse) {
                Log.d("DEBUG",errorResponse.toString());
                getLvTweets().setVisibility(View.INVISIBLE);
                getIvRateLimit().setVisibility(View.VISIBLE);
            }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("DEBUG", "onCreate");
        tweets = new ArrayList<>();
        tweetsAdapter = new TweetsArrayAdapter(getActivity(),tweets);
        populateTimeline(perRequestTweetCount,1L,1L);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tweets_list, container, false);

        Log.d("DEBUG", "onCreateView");
        lvTweets = (ListView) v.findViewById(R.id.tweetListView);
        lvTweets.setAdapter(tweetsAdapter);
        // Attach the listener to the AdapterView onCreate
        lvTweets.setOnScrollListener(new AbstractEndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
                loadNextDataFromApi(page);
                Log.d("DEBUG","onLoadMore:page"+ page);
                Log.d("DEBUG","onLoadMore:totalItemsCount"+ totalItemsCount);
                // or loadNextDataFromApi(totalItemsCount);
                return true; // ONLY if more data is actually being loaded; false otherwise.
            }
        });

        ivRateLimit = (ImageView) v.findViewById(R.id.rateLimit);
        ivRateLimit.setVisibility(View.INVISIBLE);

        return v;
    }

    public void addAll(List<Tweet> tweets){
        tweetsAdapter.addAll(tweets);
    }

    public void clear(){
        tweetsAdapter.clear();
    }

    // Append the next page of data into the adapter
    // This method probably sends out a network request and appends new data items to your adapter.
    public void loadNextDataFromApi(int offset) {
        // Send an API request to retrieve appropriate paginated data
        //  --> Send the request including an offset value (i.e `page`) as a query parameter.
        populateTimeline(perRequestTweetCount,1,oldestTweetId-1);
        //  --> Deserialize and construct new model objects from the API response
        //  --> Append the new data objects to the existing set of items inside the array of items
        //  --> Notify the adapter of the new items made with `notifyDataSetChanged()`
    }

    abstract public void populateTimeline(int count , long since_id, long max_id);

    public long getOldestTweetId() {
        return oldestTweetId;
    }

    public int getPerRequestTweetCount() {
        return perRequestTweetCount;
    }

    public ArrayList<Tweet> getTweets() {
        return tweets;
    }

    public TweetsArrayAdapter getTweetsAdapter() {
        return tweetsAdapter;
    }

    public ListView getLvTweets() {
        return lvTweets;
    }

    public ImageView getIvRateLimit() {
        return ivRateLimit;
    }

    public int getmPage() {
        return mPage;
    }

    public void setPerRequestTweetCount(int perRequestTweetCount) {
        this.perRequestTweetCount = perRequestTweetCount;
    }

    public  void setOldestTweetId(long oldestTweetId) {
        this.oldestTweetId = oldestTweetId;
    }
}