package com.codepath.apps.restclienttemplate.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

import static android.app.Activity.RESULT_OK;

public class TweetListFragment extends Fragment {

    TwitterClient client;
    RecyclerView recyclerView;
    ArrayList<Tweet> tweets= new ArrayList<>();
    TweetAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    SwipeRefreshLayout swipeRefreshLayout;
    public static final int REQUEST_CODE =1111;
    boolean firstEnter = false;
    boolean isUserTweets;
    String userID = "";


    int position;



    public TweetListFragment() {
    }

    public static TweetListFragment newInstance(int position, Boolean isUserTweets, String userId) {
        TweetListFragment fragment = new TweetListFragment();
        Bundle args = new Bundle();
        if(userId!=null){
            args.putString(Utils.USER_ID, userId);
        }
        args.putInt(Utils.POSITION, position);
        args.putBoolean(Utils.USER, isUserTweets);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {

            position = getArguments().getInt(Utils.POSITION);
            isUserTweets = getArguments().getBoolean(Utils.USER);
            if(getArguments().containsKey(Utils.USER_ID)){
                userID = getArguments().getString(Utils.USER_ID, "");
            }
        }
       // tweets = TweetDatabase.readFromDB();
        firstEnter = true;
        System.err.println(" get from database ____________" + tweets);
        client = Application.getRestClient();
        loadTimeline(false, true , isUserTweets);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tweetlist_list, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new TweetAdapter(this, tweets);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                loadTimeline(true, false, isUserTweets);

            }
        });

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.srlTweet);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadTimeline(false,true, isUserTweets);
            }
        });
    }

    private void loadTimeline(final Boolean isScrolled, final Boolean isRefreshed, boolean isUserTweets) {
        long maxId =1;
        long startId = 1;
        if(!tweets.isEmpty()){
            maxId = Long.parseLong(tweets.get(tweets.size()-1).getIdStr()) - 1;
            startId = Long.parseLong(tweets.get(0).getIdStr());
        }

             switch (position) {
        case 0:
            if(isUserTweets){
                client.getUserTimeline(isScrolled, isRefreshed, userID, maxId, startId, new JsonHttpResponseHandler(){

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        TweetListFragment.this.onFailure(throwable, statusCode);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        TweetListFragment.this.onFailure(throwable, statusCode);
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        TweetListFragment.this.onSuccess(response, isScrolled, isRefreshed);
                    }

                });
            } else {
                client.getHomeTimeline(isScrolled, isRefreshed, maxId, startId, new JsonHttpResponseHandler() {

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        TweetListFragment.this.onFailure(throwable, statusCode);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        TweetListFragment.this.onFailure(throwable, statusCode);
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        TweetListFragment.this.onSuccess(response, isScrolled, isRefreshed);
                    }

                });
            }
            break;
        case 1:
            if(isUserTweets){
                client.getUserFavourite(isScrolled, isRefreshed, userID, maxId, startId, new JsonHttpResponseHandler(){

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        TweetListFragment.this.onFailure(throwable, statusCode);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        TweetListFragment.this.onFailure(throwable, statusCode);
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        TweetListFragment.this.onSuccess(response, isScrolled, isRefreshed);
                    }

                });
            } else {
                client.getMentionsTimeline(isScrolled, isRefreshed, maxId, startId, new JsonHttpResponseHandler() {

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        TweetListFragment.this.onFailure(throwable, statusCode);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        TweetListFragment.this.onFailure(throwable, statusCode);
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        TweetListFragment.this.onSuccess(response, isScrolled, isRefreshed);
                    }

                });
            }
           break;

    }



    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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

    private void onFailure(Throwable throwable, int statusCode) {
        throwable.printStackTrace();
        System.err.println(" statusCode  "+ statusCode);
        swipeRefreshLayout.setRefreshing(false);
    }

    private void onSuccess(JSONArray response, final Boolean isScrolled, final Boolean isRefreshed) {
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
}
