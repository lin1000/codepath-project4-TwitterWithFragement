package com.codepath.apps.mysimpletweets;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.bumptech.glide.Glide;
import com.codepath.apps.mysimpletweets.adapters.TimelineFragmentAdapter;
import com.codepath.apps.mysimpletweets.fragments.ComposeDialogueFragment;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import cz.msebera.android.httpclient.Header;

import static com.loopj.android.http.AsyncHttpClient.log;

public class TimelineActivity extends AppCompatActivity implements  ComposeDialogueFragment.ComposeDialogueFragmentListener{

    private Toolbar tlToolbar;
    private TwitterClient client;

    private String userProfileBannerUrl;
    private String userProfileBackgroundImageUrl;
    private String userProfileImageUrl;
    private String userPreferredName;
    private String userScreenName;
    private int userFollowerCount;
    private int userFollowingCount;
    private String userDescription;


    ComposeDialogueFragment composeDialogueFragment;
    private ImageView ivProfileImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

       //Toolbar
       // tlToolbar = (Toolbar) findViewById(R.id.toolbar) ;
       // setSupportActionBar(tlToolbar);
        //getSupportActionBar().setLogo(R.drawable.twitter);
       // getSupportActionBar().setDisplayUseLogoEnabled (true);

        //ViewPager
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new TimelineFragmentAdapter(getSupportFragmentManager()));

        // Give the PagerSlidingTabStrip the ViewPager
        PagerSlidingTabStrip tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        // Attach the view pager to the tab strip
        tabsStrip.setViewPager(viewPager);

        // Attach the page change listener to tab strip and **not** the view pager insideivProfileImage. the activity
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

        ivProfileImage = (ImageView) findViewById(R.id.myProfile);

        populateUserProfile();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_timeline, menu);
        //MenuItem composeItem = menu.findItem(R.id.action_compose);

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

    private void populateUserProfile(){

        TwitterApplication.getRestClient().verifyCredentials(new JsonHttpResponseHandler(){
            //success
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                //deserialize
                //create model
                //load into view
                Log.d("DEBUG",jsonObject.toString());
                try {
                    TimelineActivity.this.userProfileBannerUrl = jsonObject.getString("profile_banner_url");
                    TimelineActivity.this.userProfileBackgroundImageUrl = jsonObject.getString("profile_background_image_url");
                    TimelineActivity.this.userProfileImageUrl = jsonObject.getString("profile_image_url");
                    TimelineActivity.this.userPreferredName = jsonObject.getString("screen_name");
                    TimelineActivity.this.userScreenName =jsonObject.getString("name");
                    TimelineActivity.this.userDescription =jsonObject.getString("description");
                    TimelineActivity.this.userFollowerCount = jsonObject.getInt("followers_count");
                    TimelineActivity.this.userFollowingCount = jsonObject.getInt("following");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String profileBackgroundImageUrl = TimelineActivity.this.userProfileBackgroundImageUrl;
                String profileImageUrl = TimelineActivity.this.userProfileImageUrl;
                String preferredName = TimelineActivity.this.userPreferredName;
                String screenName = TimelineActivity.this.userScreenName;
                int userFollowerCount = TimelineActivity.this.userFollowerCount;
                int userFollowingCount = TimelineActivity.this.userFollowingCount;
                String userProfileBannerUrl = TimelineActivity.this.userProfileBannerUrl;
                log.d("DEBUG", "userProfileBannerUrl="+ userProfileBannerUrl);

                Glide.with(TimelineActivity.this).load(userProfileBannerUrl).asBitmap().centerCrop().into(ivProfileImage);


                //new ProfileOperation().execute(userProfileBackgroundImageUrl);
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

        TwitterApplication.getRestClient().verifyCredentials(new JsonHttpResponseHandler(){
            //success
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                super.onSuccess(statusCode, headers, jsonObject);
                //deserialize
                //create model
                //load into view
                Log.d("DEBUG",jsonObject.toString());
                try {
                    TimelineActivity.this.userProfileBackgroundImageUrl = jsonObject.getString("profile_background_image_url");
                    TimelineActivity.this.userProfileImageUrl = jsonObject.getString("profile_image_url");
                    TimelineActivity.this.userPreferredName = jsonObject.getString("screen_name");
                    TimelineActivity.this.userScreenName =jsonObject.getString("name");
                    TimelineActivity.this.userFollowerCount = jsonObject.getInt("followers_count");
                    TimelineActivity.this.userFollowingCount = jsonObject.getInt("following");
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


    public void launchProfilelView(){
        // first parameter is the context, second is the class of the activity to launch
        Intent intent = new Intent(TimelineActivity.this, ProfileActivity.class);
        // put "extras" into the bundle for access in the second activity

        intent.putExtra("userProfileBannerUrl", userProfileBannerUrl);
        intent.putExtra("userProfileBackgroundImageUrl", userProfileBackgroundImageUrl);
        intent.putExtra("userProfileImageUrl", userProfileImageUrl);
        intent.putExtra("userPreferredName", userPreferredName);
        intent.putExtra("userScreenName", userScreenName);
        intent.putExtra("userFollowerCount", userFollowerCount);
        intent.putExtra("userFollowingCount", userFollowingCount);
        intent.putExtra("userDescription",userDescription);
        log.d("DEBUG","userDescription="+userDescription);

        startActivityForResult(intent,20);
    }

    public void onClickProfileImage(View v){
        Log.d("DEBUG", "composeButton clicked");
        launchProfilelView();
    }




    private class ProfileOperation extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... params) {
            String profileBackgroundImageUrl = params[0];
            Log.d("DEBUG",profileBackgroundImageUrl);

            Bitmap theBitmap = null;
                try {
                    theBitmap = Glide.
                            with(TimelineActivity.this).
                            load(profileBackgroundImageUrl).
                            asBitmap().
                            into(100, 100). // Width and height
                            get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

            return theBitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {

            //ivFloatingProfileImageButton.setImageBitmap(createRoundedBitmapDrawableWithBorder(getResources(),result).getBitmap());
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    public RoundedBitmapDrawable createRoundedBitmapDrawableWithBorder(Resources mResource, Bitmap bitmap){

        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();
        int borderWidthHalf = 10; // In pixels
        //Toast.makeText(mContext,""+bitmapWidth+"|"+bitmapHeight,Toast.LENGTH_SHORT).show();

        Log.d("DEBUG","bitmapWidth="+bitmapWidth);
        Log.d("DEBUG","bitmapHeight="+bitmapHeight);

        // Calculate the bitmap radius
        int bitmapRadius = Math.min(bitmapWidth,bitmapHeight)/2;

        int bitmapSquareWidth = Math.min(bitmapWidth,bitmapHeight);
        //Toast.makeText(mContext,""+bitmapMin,Toast.LENGTH_SHORT).show();

        int newBitmapSquareWidth = bitmapSquareWidth+borderWidthHalf;
        //Toast.makeText(mContext,""+newBitmapMin,Toast.LENGTH_SHORT).show();

        /*
            Initializing a new empty bitmap.
            Set the bitmap size from source bitmap
            Also add the border space to new bitmap
        */
        Bitmap roundedBitmap = Bitmap.createBitmap(newBitmapSquareWidth,newBitmapSquareWidth,Bitmap.Config.ARGB_8888);

        /*
            Canvas
                The Canvas class holds the "draw" calls. To draw something, you need 4 basic
                components: A Bitmap to hold the pixels, a Canvas to host the draw calls (writing
                into the bitmap), a drawing primitive (e.g. Rect, Path, text, Bitmap), and a paint
                (to describe the colors and styles for the drawing).

            Canvas(Bitmap bitmap)
                Construct a canvas with the specified bitmap to draw into.
        */
        // Initialize a new Canvas to draw empty bitmap
        Canvas canvas = new Canvas(roundedBitmap);

        /*
            drawColor(int color)
                Fill the entire canvas' bitmap (restricted to the current clip) with the specified
                color, using srcover porterduff mode.
        */
        // Draw a solid color to canvas
        canvas.drawColor(Color.RED);

        // Calculation to draw bitmap at the circular bitmap center position
        int x = borderWidthHalf + bitmapSquareWidth - bitmapWidth;
        int y = borderWidthHalf + bitmapSquareWidth - bitmapHeight;

        /*
            drawBitmap(Bitmap bitmap, float left, float top, Paint paint)
                Draw the specified bitmap, with its top/left corner at (x,y), using the specified
                paint, transformed by the current matrix.
        */
        /*
            Now draw the bitmap to canvas.
            Bitmap will draw its center to circular bitmap center by keeping border spaces
        */
        canvas.drawBitmap(bitmap, x, y, null);

        // Initializing a new Paint instance to draw circular border
        Paint borderPaint = new Paint();
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(borderWidthHalf*1);
        borderPaint.setColor(Color.DKGRAY);

        /*
            drawCircle(float cx, float cy, float radius, Paint paint)
                Draw the specified circle using the specified paint.
        */
        /*
            Draw the circular border to bitmap.
            Draw the circle at the center of canvas.
         */
        canvas.drawCircle(canvas.getWidth()/2, canvas.getWidth()/2, newBitmapSquareWidth/2, borderPaint);

        /*
            RoundedBitmapDrawable
                A Drawable that wraps a bitmap and can be drawn with rounded corners. You can create
                a RoundedBitmapDrawable from a file path, an input stream, or from a Bitmap object.
        */
        /*
            public static RoundedBitmapDrawable create (Resources res, Bitmap bitmap)
                Returns a new drawable by creating it from a bitmap, setting initial target density
                based on the display metrics of the resources.
        */
        /*
            RoundedBitmapDrawableFactory
                Constructs RoundedBitmapDrawable objects, either from Bitmaps directly, or from
                streams and files.
        */
        // Create a new RoundedBitmapDrawable
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(mResource,roundedBitmap);

        /*
            setCornerRadius(float cornerRadius)
                Sets the corner radius to be applied when drawing the bitmap.
        */
        // Set the corner radius of the bitmap drawable
        roundedBitmapDrawable.setCornerRadius(bitmapRadius);

        /*
            setAntiAlias(boolean aa)
                Enables or disables anti-aliasing for this drawable.
        */
        roundedBitmapDrawable.setAntiAlias(true);
        // Return the RoundedBitmapDrawable
        return roundedBitmapDrawable;
    }
}
