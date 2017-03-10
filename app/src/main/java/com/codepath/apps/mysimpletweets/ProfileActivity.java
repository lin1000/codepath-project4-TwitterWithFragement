package com.codepath.apps.mysimpletweets;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.mysimpletweets.adapters.ProfileFragmentAdapter;

/**
 * Created by lin1000 on 2017/3/10.
 */

public class ProfileActivity extends AppCompatActivity {

    private Toolbar tlToolbar;
    private ImageView ivProfileImage;
    private TextView tvFollowerCount;
    private TextView tvFollowingCount;
    private TextView tvTagline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        final String userProfileBannerUrl = getIntent().getStringExtra("userProfileBannerUrl");
        final String userProfileBackgroundImageUrl = getIntent().getStringExtra("userProfileBackgroundImageUrl");
        final String userProfileImageUrl = getIntent().getStringExtra("userProfileImageUrl");
        final String userPreferredName = getIntent().getStringExtra("userPreferredName");
        final String userScreenName = getIntent().getStringExtra("userScreenName");
        final int userFollowerCount = getIntent().getIntExtra("userFollowerCount",0);
        final int userFollowingCount = getIntent().getIntExtra("userFollowingCount",0);
        final String userDescription = getIntent().getStringExtra("userDescription");


        //Toolbar
        tlToolbar = (Toolbar) findViewById(R.id.profileToolbar) ;
        setSupportActionBar(tlToolbar);
        // getSupportActionBar().setDisplayUseLogoEnabled (true);

        ivProfileImage = (ImageView) findViewById(R.id.profileImageView);
        Glide.with(ProfileActivity.this).load(userProfileBannerUrl).into(ivProfileImage);

        tvFollowerCount = (TextView) findViewById(R.id.followerCount);
        tvFollowerCount.setText(String.valueOf(userFollowerCount) + " followers");

        tvFollowingCount = (TextView) findViewById(R.id.followingCount);
        tvFollowingCount.setText(String.valueOf(userFollowingCount) + " followings");

        tvTagline = (TextView) findViewById(R.id.tagline);
        tvTagline.setText(String.valueOf(userDescription) );


        //Reuse View Pager
        Log.d("DEBUG", "ProfileActivity.userScreenName="+userScreenName );
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        //Must give PreferredName
        viewPager.setAdapter(new ProfileFragmentAdapter(getSupportFragmentManager(),userScreenName));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);

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
        if (id == R.id.action_back) {
            Log.d("DEBUG", " R.id.action_back clicked");
            onClickBack();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClickBack(){
        finish();
    }
}
