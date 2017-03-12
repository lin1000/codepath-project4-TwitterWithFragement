package com.codepath.apps.mysimpletweets.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.ProfileActivity;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.listeners.AbstractTweetsImageViewClickListener;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

import static android.util.Log.d;

/**
 * Created by lin1000 on 2017/3/5.
 */

public class TweetsArrayAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static class GeneralTweetViewHolder  extends RecyclerView.ViewHolder {
        public ImageView ivProfileImage;
        public TextView tvPreferredName;
        public TextView tvScreenName;
        public TextView tvTweetBody;
        public ImageView ivTweetImage1;
        public ImageView ivTweetImage2;
        public TextView tvRelativeTimeAgo;
//        ArrayList<ImageView> ivTweetImages;

        public GeneralTweetViewHolder(View view) {
            super(view);
            d("DEBUG", "ViewHolder");
            ivProfileImage = (ImageView) view.findViewById(R.id.profile_image);
            tvPreferredName = (TextView) view.findViewById(R.id.preferred_name);
            tvScreenName = (TextView) view.findViewById(R.id.screen_name);
            tvRelativeTimeAgo = (TextView) view.findViewById(R.id.relative_time_ago);
            tvTweetBody = (TextView) view.findViewById(R.id.tweet_body);
            ivTweetImage1 = (ImageView) view.findViewById(R.id.tweet_image1);
            ivTweetImage2 = (ImageView) view.findViewById(R.id.tweet_image2);
//            if(ivTweetImages == null) {
//                ivTweetImages =  new ArrayList<>();
//                ivTweetImages.add(ivTweetImage1);
//                ivTweetImages.add(ivTweetImage2);
//            }

        }
    }

    public static class ProgressViewHolder  extends RecyclerView.ViewHolder {
        public ProgressBar ivProgressBar;
        public View loadingPanel;

        public ProgressViewHolder(View view) {
            super(view);
            ivProgressBar = (ProgressBar) view.findViewById(R.id.progressbar_image);
            loadingPanel = view.findViewById(R.id.loadingPanel);
        }

    }
    // Store a member variable for the contacts
    private List<Tweet> tweetsList;
    // Store the context for easy access
    private Context context;

    //Different View Types
    private static final int VIEW_GENERAL_TWEET = 1;
    private static final int VIEW_PROGRESS = 9;

    public TweetsArrayAdapter(Context context, ArrayList<Tweet> tweets){
        tweetsList = tweets;
        this.context = context;

    }
    private Context getContext(){
        return context;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        RecyclerView.ViewHolder viewHolder = null;
        View tweetView = null;
        switch(viewType){
            case VIEW_GENERAL_TWEET:
                // Inflate the custom layout
                tweetView = inflater.inflate(R.layout.item_tweet, parent, false);
                // Return a new holder instance
                viewHolder = new GeneralTweetViewHolder(tweetView);

                break;
            case VIEW_PROGRESS:
                // Inflate the custom layout
                tweetView = inflater.inflate(R.layout.item_progress, parent, false);
                // Return a new holder instance
                viewHolder = new ProgressViewHolder(tweetView);
                break;
        }


        Log.d("DEBUG","onCreateViewHolder viewType="+ viewType);

        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        d("DEBUG", "onBindViewHolder");
        // Get the data model based on position
        final Tweet tweet = tweetsList.get(position);

        if(viewHolder instanceof GeneralTweetViewHolder) {
            GeneralTweetViewHolder vh = (GeneralTweetViewHolder) viewHolder;

            // Set item views based on your views and data model
            ImageView ivProfileImage = vh.ivProfileImage;
            TextView tvPreferredName = vh.tvPreferredName;
            TextView tvScreenName = vh.tvScreenName;
            TextView tvTweetBody = vh.tvTweetBody;
            ImageView ivTweetImage1 = vh.ivTweetImage1;
            ImageView ivTweetImage2 = vh.ivTweetImage2;
            TextView tvRelativeTimeAgo = vh.tvRelativeTimeAgo;
            //ArrayList<ImageView> ivTweetImages = viewHolder.ivTweetImages;

            vh.ivProfileImage.setImageResource(0);
            if (tweet.getUser().getProfileImageUrl() != null) {
                d("DEBUG", "tweet.getUser().getProfileImageUrl()=" + tweet.getUser().getProfileImageUrl());
                String profileImageUrl = tweet.getUser().getProfileImageUrl();
                Picasso.with(getContext()).load(profileImageUrl).resize(55, 55).into(vh.ivProfileImage);
            }

            ivProfileImage.setOnClickListener(new AbstractTweetsImageViewClickListener(position) {
                @Override
                public void onClick(View view) {

                    d("DEBUG", "TweetProfileImageClicked");
                    final Intent intent = new Intent(getContext(), ProfileActivity.class);
                    d("DEBUG", "tweet.getUser().getScreenName()=" + tweet.getUser().getScreenName());
                    d("DEBUG", " getItem(position).getUser().getScreenName()=" + tweetsList.get(position).getUser().getScreenName());
                    TwitterApplication.getRestClient().userLookup(tweet.getUser().getScreenName(), new JsonHttpResponseHandler() {

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONArray jsonArray) {
                            d("DEBUG", jsonArray.toString());
                            try {
                                if (jsonArray.get(0) != null) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                                    d("DEBUG", jsonObject.toString());

                                    String userProfileBannerUrl = null;
                                    String userProfileBackgroundImageUrl = null;
                                    String userProfileImageUrl = null;
                                    String userPreferredName = null;
                                    String userScreenName = null;
                                    String userDescription = null;
                                    int userFollowerCount = 0;
                                    int userFollowingCount = 0;
                                    try {
                                        userProfileBannerUrl = jsonObject.getString("profile_banner_url");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        try {
                                            userProfileBannerUrl = jsonObject.getString("profile_background_image_url");
                                        } catch (JSONException e1) {
                                            e1.printStackTrace();
                                        }
                                    }
                                    try {
                                        userProfileBackgroundImageUrl = jsonObject.getString("profile_background_image_url");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        try {
                                            userProfileBackgroundImageUrl = jsonObject.getString("profile_banner_url");
                                        } catch (JSONException e1) {
                                            e1.printStackTrace();
                                        }
                                    }
                                    try {
                                        userProfileImageUrl = jsonObject.getString("profile_image_url");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    try {
                                        userPreferredName = jsonObject.getString("name");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    try {
                                        userScreenName = jsonObject.getString("screen_name");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    try {
                                        userDescription = jsonObject.getString("description");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    try {
                                        userFollowerCount = jsonObject.getInt("followers_count");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    try {
                                        userFollowingCount = jsonObject.getInt("following");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    intent.putExtra("userProfileBannerUrl", userProfileBannerUrl);
                                    intent.putExtra("userProfileBackgroundImageUrl", userProfileBackgroundImageUrl);
                                    intent.putExtra("userProfileImageUrl", userProfileImageUrl);
                                    intent.putExtra("userPreferredName", userPreferredName);
                                    intent.putExtra("userScreenName", userScreenName);
                                    intent.putExtra("userFollowerCount", userFollowerCount);
                                    intent.putExtra("userFollowingCount", userFollowingCount);
                                    intent.putExtra("userDescription", userDescription);

                                    getContext().startActivity(intent);

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        //failure
                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            d("DEBUG", errorResponse.toString());
                        }
                    });
                }
            });


            vh.tvPreferredName.setText(tweet.getUser().getName());
            vh.tvScreenName.setText("@" + tweet.getUser().getScreenName());
            vh.tvRelativeTimeAgo.setText(tweet.getRelativeTimeAgo());
            vh.tvRelativeTimeAgo.setTextColor(Color.GRAY);
            vh.tvTweetBody.setText(tweet.getBody());

        } else if(viewHolder instanceof ProgressViewHolder){
            ProgressViewHolder vh = (ProgressViewHolder) viewHolder;
            vh.loadingPanel.setVisibility(View.VISIBLE);
        }
    }


    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        d("DEBUG", "TweetsArrayAdapter getItemCount = " +tweetsList.size());
        return tweetsList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return tweetsList.get(position) != null ? VIEW_GENERAL_TWEET : VIEW_PROGRESS;
    }

}
