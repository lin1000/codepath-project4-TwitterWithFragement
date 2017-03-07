package com.codepath.apps.mysimpletweets.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.adapters.TweetsArrayAdapter;
import com.codepath.apps.mysimpletweets.listeners.AbstractEndlessScrollListener;
import com.codepath.apps.mysimpletweets.models.Tweet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lin1000 on 2017/3/8.
 */

public class TweetsListFragment extends Fragment {

    private ArrayList<Tweet> tweets;
    private TweetsArrayAdapter tweetsAdapter;
    ListView lvTweets;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tweets_list, container, false);

        Log.d("DEBUG", "onCreatevIEW");
        lvTweets = (ListView) v.findViewById(R.id.tweetListView);
        lvTweets.setAdapter(tweetsAdapter);
        // Attach the listener to the AdapterView onCreate
        lvTweets.setOnScrollListener(new AbstractEndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
                //loadNextDataFromApi(page);
                Log.d("DEBUG","onLoadMore:page"+ page);
                Log.d("DEBUG","onLoadMore:totalItemsCount"+ totalItemsCount);
                // or loadNextDataFromApi(totalItemsCount);
                return true; // ONLY if more data is actually being loaded; false otherwise.
            }
        });

        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("DEBUG", "onCreate");
        tweets = new ArrayList<>();
        tweetsAdapter = new TweetsArrayAdapter(getActivity(),tweets);


    }

    public void addAll(List<Tweet> tweets){
        tweetsAdapter.addAll(tweets);
    }

    public void clear(){
        tweetsAdapter.clear();
    }
}
