package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.util.Log;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowLog;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;

/*
 * This is the Android application itself and is used to configure various settings
 * including the image cache in memory and on disk. This also adds a singleton
 * for accessing the relevant rest client.
\\

 *     TwitterClient client = Application.getRestClient();
 *     // use client to send requests to API
 *
 */
public class Application extends android.app.Application {
	private static Context context;

	@Override
	public void onCreate() {
		super.onCreate();

		FlowManager.init(new FlowConfig.Builder(this).build());
		FlowLog.setMinimumLoggingLevel(FlowLog.Level.V);

		Application.context = this;

		TwitterConfig config = new TwitterConfig.Builder(this)
				.logger(new DefaultLogger(Log.DEBUG))
				.twitterAuthConfig(new TwitterAuthConfig(BuildConfig.CONSUMER_KEY, BuildConfig.CONSUMER_SECRET))
				.debug(true)
				.build();
		Twitter.initialize(config);

	}

	public static TwitterClient getRestClient() {
		return (TwitterClient) TwitterClient.getInstance(TwitterClient.class, Application.context);
	}
}