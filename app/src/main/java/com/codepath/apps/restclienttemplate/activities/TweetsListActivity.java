package com.codepath.apps.restclienttemplate.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.Utils;
import com.codepath.apps.restclienttemplate.adapter.TweetAdapter;
import com.codepath.apps.restclienttemplate.application.Application;
import com.codepath.apps.restclienttemplate.data.TweetDatabase;
import com.codepath.apps.restclienttemplate.data.TwitterClient;
import com.codepath.apps.restclienttemplate.listener.EndlessRecyclerViewScrollListener;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class TweetsListActivity extends AppCompatActivity  {

    TwitterClient client;
    RecyclerView recyclerView;
    ArrayList<Tweet> tweets= new ArrayList<>();
    TweetAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    SwipeRefreshLayout swipeRefreshLayout;
    public static final int REQUEST_CODE =1111;
    boolean firstEnter = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweets_list);
        client = Application.getRestClient();
        loadHomeTimeline(false, true);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(!Utils.isOnline()&&!Utils.isNetworkAvailable(this)){
            Toast.makeText(this, getString(R.string.connection), Toast.LENGTH_LONG);
        }
        toolbar.setTitle("");
        tweets = TweetDatabase.readFromDB();
        firstEnter = true;
        System.err.println(" get from database ____________" + tweets);
        toolbar.setLogo(getResources().getDrawable(R.drawable.tw__composer_logo_white));
        setSupportActionBar(toolbar);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new TweetAdapter(this, tweets);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                loadHomeTimeline(true, false);

            }
        });

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.srlTweet);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadHomeTimeline(false,true);
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TweetsListActivity.this, NewTweetActivity.class);
                startActivityForResult(intent,REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            Tweet tweet = (Tweet) data.getExtras().get(Utils.TWEET);
            tweets.add(0, tweet);
            adapter.notifyItemRangeInserted(0,1);
            linearLayoutManager.scrollToPosition(0);
        }

        if (resultCode == RESULT_OK && requestCode == Utils.VIEW_TWEET_REQUEST) {
            int position = data.getIntExtra(Utils.POSITION, -1);
            if (data.hasExtra(Utils.FAVORITE)) {
                tweets.get(position).setFavorited(data.getBooleanExtra(Utils.FAVORITE, false));
            }
            if (data.hasExtra(Utils.RETWEET)) {
                tweets.get(position).setRetweeted(data.getBooleanExtra(Utils.RETWEET, false));
            }
            if (data.hasExtra(Utils.REPLY)) {
                adapter.notifyItemChanged(position);
                tweets.add(0, (Tweet) data.getParcelableExtra(Utils.REPLY));
                adapter.notifyItemInserted(0);
            }
            adapter.notifyItemChanged(position);
        }

    }

    private void loadHomeTimeline(final Boolean isScrolled, final Boolean isRefreshed) {
        long maxId =1;
        long startId = 1;
        if(!tweets.isEmpty()){
            maxId = Long.parseLong(tweets.get(tweets.size()-1).getIdStr()) - 1;
            startId = Long.parseLong(tweets.get(0).getIdStr());
        }


        client.getHomeTimeline(isScrolled, isRefreshed, maxId, startId, new JsonHttpResponseHandler(){

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                throwable.printStackTrace();
                System.err.println(" statusCode  "+ statusCode);
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                    List<Tweet> tweetsList = new ArrayList<Tweet>();
                    if (response != null) {
                        System.out.println(" response "+ response);
                        try {
                            Gson gson = new GsonBuilder().create();
                            JsonArray jsonArray = gson.fromJson(response.toString(), JsonArray.class);
                            if (jsonArray != null) {
                                for (int i = 0; i < jsonArray.size(); i++) {
                                    JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                                    if (jsonObject != null) {
                                        tweetsList.add(Tweet.fromJsonObjectToTweet(jsonObject));
                                    }
                                }


                                if(isScrolled) {
                                    tweets.addAll(tweetsList);
                                    TweetDatabase.saveTweets(tweetsList);

                                }
                                if(isRefreshed){
                                    if(!tweets.isEmpty()) {
                                        if(!firstEnter) {
                                            ArrayList<Tweet> list = new ArrayList<Tweet>();
                                            list.addAll(tweets);
                                            tweets.clear();
                                            tweets.addAll(tweetsList);
                                            tweets.addAll(list);
                                            TweetDatabase.clearDB();
                                            TweetDatabase.saveTweets(list);

                                        } else {
                                            TweetDatabase.clearDB();
                                            TweetDatabase.saveTweets(tweetsList);
                                            tweets.clear();
                                            tweets.addAll(tweetsList);
                                        }
                                    } else {
                                        tweets.addAll(tweetsList);
                                        TweetDatabase.clearDB();
                                        TweetDatabase.saveTweets(tweetsList);
                                    }
                                    swipeRefreshLayout.setRefreshing(false);
                                    linearLayoutManager.scrollToPosition(0);

                                }
                                    adapter.notifyDataSetChanged();
                                firstEnter = false;
                            }
                        } catch (JsonParseException e) {
                            Log.d("Async onSuccess", "Json parsing error:" + e.getMessage(), e);
                        }
                    }
            }

        });
    }



}
