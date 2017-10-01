package com.codepath.apps.restclienttemplate.application;

import android.content.Context;

import com.codepath.apps.restclienttemplate.data.TwitterClient;
import com.codepath.apps.restclienttemplate.models.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowLog;
import com.raizlabs.android.dbflow.config.FlowManager;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

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
    static User user;

    @Override
    public void onCreate() {
        super.onCreate();
        FlowManager.init(new FlowConfig.Builder(this).build());
        FlowLog.setMinimumLoggingLevel(FlowLog.Level.V);
        Application.context = this;
    }

    public static User getUser() {
        return user;
    }

    public static TwitterClient getRestClient() {
        return (TwitterClient) TwitterClient.getInstance(TwitterClient.class, Application.context);
    }

    public static void initUser() {
        getRestClient().verifyCredentials(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (response != null) {
                    Gson gson = new GsonBuilder().create();
                    JsonObject jsonObject = gson.fromJson(response.toString(), JsonObject.class);
                    if (jsonObject != null) {
                        user = User.fromJsonObjectToUser(jsonObject);
                    }
                }
            }


        });
    }
}