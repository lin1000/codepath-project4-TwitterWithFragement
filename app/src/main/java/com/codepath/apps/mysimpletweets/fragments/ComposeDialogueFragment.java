package com.codepath.apps.mysimpletweets.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by lin1000 on 2017/3/5.
 */

public class ComposeDialogueFragment extends DialogFragment {

    private static int twitterStatusLimit =140;

    public interface ComposeDialogueFragmentListener {
        void onFinishCompose();
    }


    ImageView profileImageView;
    TextView preferredNameView;
    TextView screenNameView;
    EditText composeTextView;
    Button tweetButtonView;
    TextView textCountView;
    TwitterClient client;

    public ComposeDialogueFragment() {}

    public static ComposeDialogueFragment newInstance(String profileImageUrl, String preferredName, String screenName) {
        ComposeDialogueFragment fragmentDemo = new ComposeDialogueFragment();
        Bundle args = new Bundle();
        args.putString("profileImageUrl", profileImageUrl);
        args.putString("preferredName", preferredName);
        args.putString("screenName", screenName);
        fragmentDemo.setArguments(args);
        return fragmentDemo;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.compose_fragment, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        client = TwitterApplication.getRestClient(); //singleton client

        // Get back arguments
        String profileImageUrl = getArguments().getString("profileImageUrl","");
        String preferredName = getArguments().getString("preferredName", "");
        String screenName = getArguments().getString("screenName", "");
        Log.d("DEBUG","profileImageUrl"+ profileImageUrl);
        Log.d("DEBUG","preferredName"+ preferredName);
        Log.d("DEBUG","screenName"+ screenName);

        profileImageView = (ImageView) view.findViewById(R.id.profile_image);
        preferredNameView = (TextView) view.findViewById(R.id.preferred_name);
        screenNameView = (TextView) view.findViewById(R.id.screen_name);
        composeTextView = (EditText) view.findViewById(R.id.composeText);
        tweetButtonView = (Button) view.findViewById(R.id.tweet_button);
        textCountView = (TextView) view.findViewById(R.id.text_count);

        Log.d("DEBUG","profileImageView"+ profileImageView);
        Log.d("DEBUG","preferredNameView"+ preferredNameView);
        Log.d("DEBUG","screenNameView"+ screenNameView);
        Log.d("DEBUG","composeTextView"+ composeTextView);

        profileImageView.setImageResource(0);
        Picasso.with(view.getContext()).load(profileImageUrl).resize(150,150).into(profileImageView);
        //profileImageView.setBackgroundColor(Color.RED);

        preferredNameView.setText(preferredName);
        preferredNameView.setTextColor(Color.DKGRAY);
        screenNameView.setText("@"+screenName);
        screenNameView.setTextColor(Color.DKGRAY);


        tweetButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String status = composeTextView.getText().toString();

                client.composeTweet(status,new JsonHttpResponseHandler(){
                    //success
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                        super.onSuccess(statusCode, headers, json);
                        Log.d("DEBUG","composeTweet onSuccess JSONObject");
                    }


                    //success
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                        super.onSuccess(statusCode, headers, json);
                        Log.d("DEBUG","composeTweet onSuccess JSONArray");
                    }

                    //failure
                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        Log.d("DEBUG",errorResponse.toString());
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        Log.d("DEBUG",errorResponse.toString());
                    }
                });

                ComposeDialogueFragmentListener listener = (ComposeDialogueFragmentListener) getActivity();
                listener.onFinishCompose();
                dismiss();
            }
        });

        composeTextView.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int currentStatusLength =  s.length();
                int wordsLeft = twitterStatusLimit - currentStatusLength;
                textCountView.setText(String.valueOf(wordsLeft));

                if (wordsLeft < 120 ) {
                    textCountView.setTextColor(Color.RED);
                }

            }
        });


    }
}
