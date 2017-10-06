package com.codepath.apps.restclienttemplate.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.Utils;
import com.codepath.apps.restclienttemplate.adapter.FollowersAdapter;
import com.codepath.apps.restclienttemplate.application.Application;
import com.codepath.apps.restclienttemplate.data.TwitterClient;
import com.codepath.apps.restclienttemplate.listener.EndlessRecyclerViewScrollListener;
import com.codepath.apps.restclienttemplate.models.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class FollowersActivity extends AppCompatActivity {
    List<User> followers = new ArrayList<>();
    TwitterClient client;
    String userId;
    boolean isFollowers;
    String cursor;
    private FollowersAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers);
        client = Application.getRestClient();
        userId =  getIntent().getStringExtra(Utils.USER_ID);
        isFollowers =  getIntent().getBooleanExtra(Utils.IS_FOLLOWERS, true);

        getFollowers(userId, isFollowers);
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.rrFollowers);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new FollowersAdapter(followers, this);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                getFollowers(userId, isFollowers);

            }
        });

    }

    private void getFollowers(String userId, boolean isFollowers) {

        if (isFollowers) {
            client.getFollowers(userId, cursor, new JsonHttpResponseHandler() {

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    onFailed(throwable, statusCode+"");
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    onFailed(throwable, statusCode+"");
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    FollowersActivity.this.onSuccess(response);
                }

            });
        } else {
            client.getFriends(userId, cursor, new JsonHttpResponseHandler() {

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                  onFailed(throwable, statusCode+"");
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    onFailed(throwable, statusCode+"");
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                 FollowersActivity.this.onSuccess(response);
                }

            });

        }
    }

    private void onSuccess(JSONObject response) {
        List<User> userList = new ArrayList<User>();
        if (response != null) {
            System.out.println(" response " + response);
            try {
                Gson gson = new GsonBuilder().create();
                JsonObject jsonObjectRes = gson.fromJson(response.toString(), JsonObject.class);
                cursor = jsonObjectRes.get("next_cursor_str").getAsString();
                JsonArray jsonArray = jsonObjectRes.get("users").getAsJsonArray();
                if (jsonArray != null) {
                    for (int i = 0; i < jsonArray.size(); i++) {
                        JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                        if (jsonObject != null) {
                            userList.add(User.fromJsonObjectToUser(jsonObject));
                        }
                    }
                    followers.addAll(userList);
                    adapter.notifyDataSetChanged();
                }
            } catch (JsonParseException e) {
                Log.d("Async onSuccess", "Json parsing error:" + e.getMessage(), e);
            }
        }
    }

    private void onFailed(Throwable throwable, String statusCode) {
        throwable.printStackTrace();
        System.err.println(" statusCode  " + statusCode);
    }

}
