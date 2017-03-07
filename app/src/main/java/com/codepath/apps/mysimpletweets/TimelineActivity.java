package com.codepath.apps.mysimpletweets;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.mysimpletweets.fragments.ComposeDialogueFragment;
import com.codepath.apps.mysimpletweets.fragments.TweetsListFragment;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity implements  ComposeDialogueFragment.ComposeDialogueFragmentListener{

    private Toolbar tlToolbar;
    private TwitterClient client;

    private TweetsListFragment tweetsListFragment;

    private static long oldestTweetId=1;
    private static int perRequestTweetCount = 20;

    private String userProfileImageUrl;
    private String userPreferredName;
    private String userScreenName;
    ComposeDialogueFragment composeDialogueFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        //Toolbar
        tlToolbar = (Toolbar) findViewById(R.id.toolbar) ;
        setSupportActionBar(tlToolbar);
        getSupportActionBar().setLogo(R.drawable.twitter);
        getSupportActionBar().setDisplayUseLogoEnabled (true);

        //Get tweetsListFragment
        tweetsListFragment = (TweetsListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_timeline);

        client = TwitterApplication.getRestClient(); //singleton client
        populateTimeline(perRequestTweetCount,1L,1L);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        MenuItem composeItem = menu.findItem(R.id.action_compose);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Log.d("DEBUG", "item.getItemId()="+ item.getItemId());

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_compose) {
            Log.d("DEBUG", " R.id.action_compose clicked");
            showComposeDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    // Append the next page of data into the adapter
    // This method probably sends out a network request and appends new data items to your adapter.
    public void loadNextDataFromApi(int offset) {
        // Send an API request to retrieve appropriate paginated data
        //  --> Send the request including an offset value (i.e `page`) as a query parameter.
        populateTimeline(perRequestTweetCount,1,TimelineActivity.oldestTweetId-1);
        //  --> Deserialize and construct new model objects from the API response
        //  --> Append the new data objects to the existing set of items inside the array of items
        //  --> Notify the adapter of the new items made with `notifyDataSetChanged()`
    }

    //send api quest to get tweets
    //populate listview by creating tweets object from json
    private void populateTimeline(int count , long since_id, long max_id){
        Log.d("DEBUG", "populateTimeline=max_id="+max_id);

        client.getHomeTimeline(count, since_id, max_id, new JsonHttpResponseHandler(){
            //success
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                super.onSuccess(statusCode, headers, json);
                //deserialize
                //create model
                //load into view
                ArrayList<Tweet> tweets = Tweet.fromJSONArray(json);
                if (tweets!=null && tweets.size()>1) {
                    Tweet oldestTweet = tweets.get(tweets.size()-1);
                    TimelineActivity.oldestTweetId = oldestTweet.getUid();
                    Log.d("DEBUG","oldestTweetId="+ oldestTweetId);
                }
                tweetsListFragment.addAll(tweets);
            }

            //failure
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.d("DEBUG",errorResponse.toString());
            }
        });
    }

    private void showComposeDialog(){

        client.verifyCredentials(new JsonHttpResponseHandler(){
            //success
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                super.onSuccess(statusCode, headers, jsonObject);
                //deserialize
                //create model
                //load into view
                Log.d("DEBUG",jsonObject.toString());
                try {
                    TimelineActivity.this.userProfileImageUrl = jsonObject.getString("profile_image_url");
                    TimelineActivity.this.userPreferredName = jsonObject.getString("screen_name");
                    TimelineActivity.this.userScreenName =jsonObject.getString("name");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String profileImageUrl = userProfileImageUrl;
                String preferredName = userPreferredName;
                String screenName = userScreenName;
                FragmentManager fm = getSupportFragmentManager();
                //composeDialogueFragment = ComposeDialogueFragmentFactory.getInstance();
                composeDialogueFragment = ComposeDialogueFragment.newInstance(profileImageUrl,preferredName,screenName);
                composeDialogueFragment.show(fm, "compose_fragment");
            }

            //failure
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.d("DEBUG",errorResponse.toString());
            }
        });
    }

    @Override
    public void onFinishCompose() {
        tweetsListFragment.clear();
        populateTimeline(perRequestTweetCount,1L,1L);
    }
}
