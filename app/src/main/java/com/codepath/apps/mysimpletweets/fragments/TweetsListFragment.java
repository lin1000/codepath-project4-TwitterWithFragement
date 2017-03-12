package com.codepath.apps.mysimpletweets.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
    protected int perRequestTweetCount = 5;

    protected ArrayList<Tweet> tweets;
    protected TweetsArrayAdapter tweetsAdapter;


    //RecyclerView
    protected RecyclerView rvTweetsList;
    protected AbstractEndlessScrollListener endlessScrollListener;
    protected ImageView ivRateLimit;
    protected TextView tvRateLimitText;


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
                getRvTweetsView().setVisibility(View.INVISIBLE);
                getIvRateLimit().setVisibility(View.VISIBLE);
                getTvRateLimitText().setVisibility(View.VISIBLE);
                getTvRateLimitText().setText("Oops! Rate Limit Reached.");

            }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("DEBUG", "onCreate");
        tweets = new ArrayList<>();
        tweetsAdapter = new TweetsArrayAdapter(getActivity(),tweets);
        Log.d("DEBUG", "TweetListFragement tweets = " +tweets);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tweets_list, container, false);

        Log.d("DEBUG", "onCreateView");
        Log.d("DEBUG", "tweetsAdapter="+tweetsAdapter);
        rvTweetsList = (RecyclerView) v.findViewById(R.id.rvTweetsList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext().getApplicationContext());
        rvTweetsList.setAdapter(tweetsAdapter);
        rvTweetsList.setLayoutManager(layoutManager);
        // Attach the listener to the AdapterView onCreate
        rvTweetsList.addOnScrollListener(new AbstractEndlessScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView recyclerView) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
                Log.d("DEBUG","onLoadMore:page"+ page);
                Log.d("DEBUG","onLoadMore:totalItemsCount"+ totalItemsCount);
                loadNextDataFromApi(page);
            }
        });

        rvTweetsList.setItemAnimator(new DefaultItemAnimator());

        ivRateLimit = (ImageView) v.findViewById(R.id.rateLimit);
        ivRateLimit.setVisibility(View.INVISIBLE);

        tvRateLimitText = (TextView) v.findViewById(R.id.rateLimitText);
        tvRateLimitText.setVisibility(View.INVISIBLE);

        populateTimeline(perRequestTweetCount,1L,1L);
        return v;
    }

    public void addAll(List<Tweet> tweets){
        Log.d("DEBUG", "tweets.size()="+tweets.size());
        this.tweets.addAll(tweets);
        tweetsAdapter.notifyDataSetChanged();
    }

    public void clear(){

        // 1. First, clear the array of data
        tweets.clear();
        // 2. Notify the adapter of the update
        tweetsAdapter.notifyDataSetChanged(); // or notifyItemRangeRemoved
        // 3. Reset endless scroll listener when performing a new search
        endlessScrollListener.resetState();
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

    public RecyclerView getRvTweetsView() {
        return rvTweetsList;
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

    public TextView getTvRateLimitText() {
        return tvRateLimitText;
    }
}