package com.codepath.apps.mysimpletweets;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.mysimpletweets.adapters.TimelineFragmentAdapter;
import com.codepath.apps.mysimpletweets.fragments.ComposeDialogueFragment;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity implements  ComposeDialogueFragment.ComposeDialogueFragmentListener{

    private Toolbar tlToolbar;
    private TwitterClient client;

    private String userProfileImageUrl;
    private String userPreferredName;
    private String userScreenName;
    ComposeDialogueFragment composeDialogueFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        //Toolbar
       // tlToolbar = (Toolbar) findViewById(R.id.toolbar) ;
       // setSupportActionBar(tlToolbar);
       // getSupportActionBar().setLogo(R.drawable.twitter);
       // getSupportActionBar().setDisplayUseLogoEnabled (true);

        //ViewPager
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new TimelineFragmentAdapter(getSupportFragmentManager()));

        // Give the PagerSlidingTabStrip the ViewPager
        PagerSlidingTabStrip tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        // Attach the view pager to the tab strip
        tabsStrip.setViewPager(viewPager);

        // Attach the page change listener to tab strip and **not** the view pager inside the activity
        tabsStrip.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            // This method will be invoked when a new page becomes selected.
            @Override
            public void onPageSelected(int position) {
                Toast.makeText(TimelineActivity.this,
                        "Selected page position: " + position, Toast.LENGTH_SHORT).show();
            }

            // This method will be invoked when the current page is scrolled
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Code goes here
                Log.d("DEBUG","onPageScrolled position:" +position);
            }

            // Called when the scroll state changes:
            // SCROLL_STATE_IDLE, SCROLL_STATE_DRAGGING, SCROLL_STATE_SETTLING
            @Override
            public void onPageScrollStateChanged(int state) {
                // Code goes here
                Log.d("DEBUG","onPageScrollStateChanged state:" +state);
            }
        });


        client = TwitterApplication.getRestClient(); //singleton client
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
        //tweetsListFragment.clear();
        //populateTimeline(perRequestTweetCount,1L,1L);
    }

    public TwitterClient getClient() {
        return client;
    }

    public void composeButton(View v){
        Log.d("DEBUG", "composeButton clicked");
        showComposeDialog();
    }
}
