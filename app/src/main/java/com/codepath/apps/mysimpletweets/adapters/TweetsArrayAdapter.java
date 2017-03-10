package com.codepath.apps.mysimpletweets.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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

import cz.msebera.android.httpclient.Header;

import static android.util.Log.d;

/**
 * Created by lin1000 on 2017/3/5.
 */

public class TweetsArrayAdapter extends ArrayAdapter<Tweet> {

    static class ViewHolder{
        ImageView ivProfileImage;
        TextView tvPreferredName;
        TextView tvScreenName;
        TextView tvTweetBody;
        ImageView ivTweetImage1;
        ImageView ivTweetImage2;
        ArrayList<ImageView> ivTweetImages;
        TextView tvRelativeTimeAgo;

//        @Nullable @BindView(R.id.preferred_name) TextView tvPreferredName;
//        @Nullable @BindView(R.id.screen_name) TextView tvScreenName;
//        @Nullable @BindView(R.id.tweet_image1) ImageView ivTweetImage1;


        public ViewHolder(View view) {

        }
    }

    public TweetsArrayAdapter(Context context, ArrayList<Tweet> tweets){
        super(context, android.R.layout.simple_list_item_1, tweets);
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Tweet tweet = getItem(position);

        //Get ViewHolder
        final ViewHolder viewHolder;

        if(convertView==null){

            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_tweet,parent,false);

            //initialize view holder
            viewHolder = new ViewHolder(convertView);

            //conver view keep the reference for view holder
            convertView.setTag(viewHolder);
        }else{
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.ivProfileImage = (ImageView) convertView.findViewById(R.id.profile_image);
        viewHolder.ivProfileImage.setImageResource(0);
        if(tweet.getUser().getProfileImageUrl()!=null){
            System.out.println(tweet.getUser().getProfileImageUrl());
            String profileImageUrl = tweet.getUser().getProfileImageUrl();
            Picasso.with(getContext()).load(profileImageUrl).resize(55,55).into(viewHolder.ivProfileImage);
        }

        viewHolder.ivProfileImage.setOnClickListener(new AbstractTweetsImageViewClickListener(position) {
            @Override
            public void onClick(View view) {

                d("DEBUG", "TweetProfileImageClicked");
                final Intent intent = new Intent(getContext(), ProfileActivity.class);
                Log.d("DEBUG","tweet.getUser().getScreenName()=" + tweet.getUser().getScreenName());
                Log.d("DEBUG"," getItem(position).getUser().getScreenName()=" + getItem(position).getUser().getScreenName());
                TwitterApplication.getRestClient().userLookup(tweet.getUser().getScreenName(),new JsonHttpResponseHandler(){

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray jsonArray) {
                        Log.d("DEBUG",jsonArray.toString());
                        try {
                            if(jsonArray.get(0)!=null){
                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                Log.d("DEBUG",jsonObject.toString());

                                String userProfileBannerUrl = null;
                                String userProfileBackgroundImageUrl = null;
                                String userProfileImageUrl = null;
                                String userPreferredName = null;
                                String userScreenName = null;
                                String userDescription = null;
                                int userFollowerCount=0;
                                int userFollowingCount=0;
                                try {
                                     userProfileBannerUrl = jsonObject.getString("profile_banner_url");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    try {userProfileBannerUrl =  jsonObject.getString("profile_background_image_url"); }catch (JSONException e1) {e1.printStackTrace();}
                                }
                                try {userProfileBackgroundImageUrl =  jsonObject.getString("profile_background_image_url"); }catch (JSONException e) {
                                    e.printStackTrace();
                                    try {userProfileBackgroundImageUrl =  jsonObject.getString("profile_banner_url"); }catch (JSONException e1) {e1.printStackTrace();}
                                }
                                try {userProfileImageUrl = jsonObject.getString("profile_image_url"); }catch (JSONException e) {e.printStackTrace();}
                                try {userPreferredName = jsonObject.getString("name"); }catch (JSONException e) {e.printStackTrace();}
                                try {userScreenName =jsonObject.getString("screen_name"); }catch (JSONException e) {e.printStackTrace();}
                                try {userDescription =jsonObject.getString("description"); }catch (JSONException e) {e.printStackTrace();}
                                try {userFollowerCount =  jsonObject.getInt("followers_count"); }catch (JSONException e) {e.printStackTrace();}
                                try {userFollowingCount =jsonObject.getInt("following"); }catch (JSONException e) {e.printStackTrace();}

                                intent.putExtra("userProfileBannerUrl", userProfileBannerUrl);
                                intent.putExtra("userProfileBackgroundImageUrl", userProfileBackgroundImageUrl);
                                intent.putExtra("userProfileImageUrl", userProfileImageUrl);
                                intent.putExtra("userPreferredName", userPreferredName);
                                intent.putExtra("userScreenName", userScreenName);
                                intent.putExtra("userFollowerCount", userFollowerCount);
                                intent.putExtra("userFollowingCount", userFollowingCount);
                                intent.putExtra("userDescription",userDescription);

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
                        Log.d("DEBUG",errorResponse.toString());
                    }
                });


                d("DEBUG","view.toString()="+view.toString());



            }
        });


        viewHolder.tvPreferredName = (TextView) convertView.findViewById(R.id.preferred_name);
        viewHolder.tvPreferredName.setText(tweet.getUser().getName());

        viewHolder.tvScreenName = (TextView) convertView.findViewById(R.id.screen_name);
        viewHolder.tvScreenName.setText("@"+tweet.getUser().getScreenName());

        viewHolder.tvRelativeTimeAgo = (TextView) convertView.findViewById(R.id.relative_time_ago);
        viewHolder.tvRelativeTimeAgo.setText(tweet.getRelativeTimeAgo());

        if(tweet.getRelativeTimeAgo().contains("seconds ago")){
            viewHolder.tvRelativeTimeAgo.setTextColor(Color.RED);
        }
        else if(tweet.getRelativeTimeAgo().equalsIgnoreCase("1 minute ago")) {
            viewHolder.tvRelativeTimeAgo.setTextColor(Color.GREEN);
        }else{
            viewHolder.tvRelativeTimeAgo.setTextColor(Color.DKGRAY);
        }

        viewHolder.tvTweetBody = (TextView) convertView.findViewById(R.id.tweet_body);
        viewHolder.tvTweetBody.setText(tweet.getBody());


        viewHolder.ivTweetImage1 = (ImageView) convertView.findViewById(R.id.tweet_image1);
        viewHolder.ivTweetImage1.setImageResource(0);
        viewHolder.ivTweetImage2 = (ImageView) convertView.findViewById(R.id.tweet_image2);
        viewHolder.ivTweetImage2.setImageResource(0);

        if(viewHolder.ivTweetImages == null) {
            viewHolder.ivTweetImages =  new ArrayList<>();
            viewHolder.ivTweetImages.add(viewHolder.ivTweetImage1);
            viewHolder.ivTweetImages.add(viewHolder.ivTweetImage2);
        }


        if(tweet.getExtendedEntities() != null){
            d("DEBUG","viewHolder.ivTweetImages.size()="+viewHolder.ivTweetImages.size());
            d("DEBUG","tweet.getExtendedEntities().getMedia().size())="+tweet.getExtendedEntities().getMedia().size());
            for(int i=0 ; i < tweet.getExtendedEntities().getMedia().size() ; i++){
                if(i >=  viewHolder.ivTweetImages.size()) break;
                String tweetImage1Url = tweet.getExtendedEntities().getMedia().get(i).getMediaUrl();
                int medium_w = tweet.getExtendedEntities().getMedia().get(i).getMedium_w();
                int medium_h = tweet.getExtendedEntities().getMedia().get(i).getMedium_h();
                ImageView tweetImageView = viewHolder.ivTweetImages.get(i);
                Glide.with(getContext()).load(tweetImage1Url).into(tweetImageView);
            }
        }
//        viewHolder.ivTweetImage1 = (ImageView) convertView.findViewById(R.id.tweet_image1);
//        if(tweet.getTweetImage1Url()!=null){
//            Log.d("DEBUG",tweet.getTweetImage1Url());
//            String tweetImage1Url = tweet.getUser().getProfileImageUrl();
//            Glide.with(getContext()).load(tweetImage1Url).fitCenter().centerCrop().into(viewHolder.ivTweetImage1);
//        }


//        //TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
//        tvTitle.setText(article.getHeadLine());
//
//        if(article.getNytMultimedia() != null) {
//            String thumbnail = article.getNytMultimedia().getUrl();
//            Glide.with(getContext()).load(thumbnail).into(imageView);
//        }

        return convertView;
    }


}
