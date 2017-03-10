package com.codepath.apps.mysimpletweets.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.models.Tweet;

import java.util.ArrayList;

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
    public View getView(int position, View convertView, ViewGroup parent) {
        Tweet tweet = getItem(position);

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
            Glide.with(getContext()).load(profileImageUrl).asBitmap().centerCrop().into(viewHolder.ivProfileImage);
        }

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
            Log.d("DEBUG","viewHolder.ivTweetImages.size()="+viewHolder.ivTweetImages.size());
            Log.d("DEBUG","tweet.getExtendedEntities().getMedia().size())="+tweet.getExtendedEntities().getMedia().size());
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
