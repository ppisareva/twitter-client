package com.codepath.apps.restclienttemplate.data;

import android.content.Context;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.oauth.OAuthBaseClient;
import com.github.scribejava.apis.TwitterApi;
import com.github.scribejava.core.builder.api.BaseApi;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class TwitterClient extends OAuthBaseClient {
	public static final BaseApi REST_API_INSTANCE = TwitterApi.instance();
	public static final String REST_URL = "https://api.twitter.com/1.1";
	public static final String REST_CONSUMER_KEY = com.codepath.apps.restclienttemplate.BuildConfig.CONSUMER_KEY;
	public static final String REST_CONSUMER_SECRET = com.codepath.apps.restclienttemplate.BuildConfig.CONSUMER_SECRET;
	public static final String FALLBACK_URL = "https://codepath.github.io/android-rest-client-template/success.html";
	public static final String REST_CALLBACK_URL_TEMPLATE = "intent://%s#Intent;action=android.intent.action.VIEW;scheme=%s;package=%s;S.browser_fallback_url=%s;end";

	public TwitterClient(Context context) {
		super(context, REST_API_INSTANCE,
				REST_URL,
				REST_CONSUMER_KEY,
				REST_CONSUMER_SECRET,
				String.format(REST_CALLBACK_URL_TEMPLATE, context.getString(R.string.intent_host),
						context.getString(R.string.intent_scheme), context.getPackageName(), FALLBACK_URL));
	}


	public void getHomeTimeline( Boolean isScrolled, Boolean isRefreshed, long maxId, long sinceId, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/home_timeline.json");
		RequestParams params = new RequestParams();
		if (isScrolled) {
			params.put("max_id", maxId);
			params.put("count", 25);
		} else if(isRefreshed) {
			params.put("since_id", sinceId);
		} else {
			params.put("count", 25);
			params.put("since_id", 1);
		}
		getClient().get(apiUrl, params, handler);
	}


	public void getUserTimeline( Boolean isScrolled, Boolean isRefreshed, String userId, long maxId, long sinceId, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/user_timeline.json");
		RequestParams params = new RequestParams();
		params.put("user_id", userId);
		if (isScrolled) {
			params.put("max_id", maxId);
			params.put("count", 25);
		} else if(isRefreshed) {
			params.put("since_id", sinceId);
		} else {
			params.put("count", 25);
			params.put("since_id", 1);
		}
		getClient().get(apiUrl, params, handler);
	}

	public void getUserFavourite(Boolean isScrolled, Boolean isRefreshed, String userId, long maxId, long sinceId, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("favorites/list.json");
		RequestParams params = new RequestParams();
		params.put("user_id", userId);
		if (isScrolled) {
			params.put("max_id", maxId);
			params.put("count", 25);
		} else if(isRefreshed) {
			params.put("since_id", sinceId);
		} else {
			params.put("count", 25);
			params.put("since_id", 1);
		}
		getClient().get(apiUrl, params, handler);
	}

	public void getMentionsTimeline( Boolean isScrolled, Boolean isRefreshed, long maxId, long sinceId, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/mentions_timeline.json");
		RequestParams params = new RequestParams();
		if (isScrolled) {
			params.put("max_id", maxId);
			params.put("count", 25);
		} else if(isRefreshed) {
			params.put("since_id", sinceId);
		} else {
			params.put("count", 25);
			params.put("since_id", 1);
		}
		getClient().get(apiUrl, params, handler);
	}

	public void sendTweet(String text, String inReplyTo, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/update.json");
		RequestParams params = new RequestParams();
		params.put("status", text);
		if(!inReplyTo .isEmpty()) {
			params.put("in_reply_to_status_id", inReplyTo);
		}
		getClient().post(apiUrl, params, handler);
	}

	public void getUser( String userId, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("users/show.json");
		RequestParams params = new RequestParams();
		params.put("user_id", userId);
		client.get(apiUrl, params, handler);
	}


	public void verifyCredentials(AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("account/verify_credentials.json");
		client.get(apiUrl, null, handler);
	}

    public void retweet(String tweetId, boolean retweet, JsonHttpResponseHandler handler) {
        String endpoint = retweet ? "retweet" : "unretweet";
        String apiUrl = getApiUrl("statuses/" + endpoint + "/" + tweetId + ".json");
        RequestParams params = new RequestParams();
        params.put("id", tweetId);
        client.post(apiUrl, params, handler);
    }

    public void isLike(String tweetId, boolean isLiked, JsonHttpResponseHandler handler) {
        String endpoint = isLiked ? "create" : "destroy";
        String apiUrl = getApiUrl("favorites/" + endpoint + ".json?id=" + tweetId);
        client.post(apiUrl, null, handler);
    }
    public void isFriends(String userId, boolean isFriends, JsonHttpResponseHandler handler) {
        String endpoint = isFriends ? "create" : "destroy";
        String apiUrl = getApiUrl("friendships/" + endpoint + ".json?id=" + userId);
        client.post(apiUrl, null, handler);
    }


}
