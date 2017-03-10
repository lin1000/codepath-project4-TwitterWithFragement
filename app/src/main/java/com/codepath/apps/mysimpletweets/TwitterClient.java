package com.codepath.apps.mysimpletweets;

import android.content.Context;
import android.util.Log;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
	public static final String REST_URL = "https://api.twitter.com/1.1/"; // Change this, base API URL
	public static final String REST_CONSUMER_KEY = "cYTnqWRCjTRgzMY51BwMnDbEr";       // Change this
	public static final String REST_CONSUMER_SECRET = "J1mbBYVFr9Sdwac2r9IrLUAKZhGICjQYOPOeTGbd1rbKDbK5is"; // Change this
	public static final String REST_CALLBACK_URL = "oauth://codepath-lin1000"; // Change this (here and in manifest)

	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}

	//Home Timeline Endpoint
    //    Endpoints:
    //      - Get the home timeline for the user
    //      GET statuses/home_timeline.json
    //            count=25
    //            since_id=1
    public void getHomeTimeline(AsyncHttpResponseHandler handler){
        String apiUrl = getApiUrl("statuses/home_timeline.json");
        RequestParams params = new RequestParams();
        params.put("count", 25);
        params.put("since_id",1);
        //Execute the request
        getClient().get(apiUrl, params, handler);
    }

	public void getHomeTimeline(int count, long since_id, long max_id, AsyncHttpResponseHandler handler){
        String apiUrl = getApiUrl("statuses/home_timeline.json");
        RequestParams params = new RequestParams();
        params.put("count", count);
        params.put("since_id",since_id);
        Log.d("DEBUG", "mmax_id="+ max_id);
        if(max_id!=1L)
            params.put("max_id",max_id);
        //Execute the request
        getClient().get(apiUrl, params, handler);
    }

	//statuses/update.json
	public void composeTweet(String status, AsyncHttpResponseHandler handler){
		String apiUrl = getApiUrl("statuses/update.json");
		RequestParams params = new RequestParams();
		params.put("status", status);
		getClient().post(apiUrl, params, handler);
	}

    //account/verify_credentials.json
    public void verifyCredentials( AsyncHttpResponseHandler handler){
        String apiUrl = getApiUrl("account/verify_credentials.json");
        RequestParams params = new RequestParams();
        getClient().get(apiUrl, params, handler);
    }

	//GetMention
	//https://api.twitter.com/1.1/statuses/user_timeline.json?count=25&since_id=1&user_id=557851482&screen_name=Irene09106916
    public void getUserMention( int count, long since_id, long max_id, AsyncHttpResponseHandler handler){
        String apiUrl = getApiUrl("statuses/mentions_timeline.json");
        RequestParams params = new RequestParams();
        params.put("count", count);
        params.put("since_id", since_id);
        if(max_id!=1L)
            params.put("max_id",max_id);
        getClient().get(apiUrl, params, handler);
    }

    public void getUserMention( int count, long since_id, AsyncHttpResponseHandler handler){
        getUserMention(count, since_id, 1L , handler);
    }

	/* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
	 * 	  i.e getApiUrl("statuses/home_timeline.json");
	 * 2. Define the parameters to pass to the request (query or body)
	 *    i.e RequestParams params = new RequestParams("foo", "bar");
	 * 3. Define the request method and make a call to the client
	 *    i.e client.get(apiUrl, params, handler);
	 *    i.e client.post(apiUrl, params, handler);
	 */
}
