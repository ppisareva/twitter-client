package com.codepath.apps.restclienttemplate.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.codepath.apps.restclienttemplate.data.TwitterClient;
import com.codepath.apps.restclienttemplate.listener.EndlessRecyclerViewScrollListener;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class SearchFragment extends Fragment {


    private String query;

    TwitterClient client;
    RecyclerView recyclerView;
    ArrayList<Tweet> tweets= new ArrayList<>();
    TweetAdapter adapter;
    LinearLayoutManager linearLayoutManager;


    int position;


    public SearchFragment() {
        // Required empty public constructor
    }

    public static SearchFragment newInstance(String query) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(Utils.SEARCH, query);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            query = getArguments().getString(Utils.SEARCH);
        }
        client = Application.getRestClient();
        loadTimeline(query, false, true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
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
                loadTimeline(query, true, false);

            }
        });


    }

    private void loadTimeline(String query, Boolean isScrolled, Boolean isRefreshed) {
        long maxId =1;
        long startId = 1;
        if(!tweets.isEmpty()){
            maxId = Long.parseLong(tweets.get(tweets.size()-1).getIdStr()) - 1;
            startId = Long.parseLong(tweets.get(0).getIdStr());
        }


                    client.getSearchTweet(isScrolled, isRefreshed, query, maxId, startId, new JsonHttpResponseHandler(){

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                            throwable.printStackTrace();
                            System.err.println(" statusCode  "+ statusCode);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {throwable.printStackTrace();
                            System.err.println(" statusCode  "+ statusCode);

                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            List<Tweet> tweetsList = new ArrayList<Tweet>();
                            if (response != null) {
                                System.out.println(" response "+ response);
                                try {
                                    Gson gson = new GsonBuilder().create();
                                    JsonObject jsonObject = gson.fromJson(response.toString(), JsonObject.class);
                                    JsonArray jsonArray =  jsonObject.get("statuses").getAsJsonArray();

                                    if (jsonArray != null) {
                                        for (int i = 0; i < jsonArray.size(); i++) {
                                            JsonObject jsonObject1 = jsonArray.get(i).getAsJsonObject();
                                            if (jsonObject1 != null) {
                                                tweetsList.add(Tweet.fromJsonObjectToTweet(jsonObject1));
                                            }
                                        }
                                        tweets.addAll(tweetsList);
                                        adapter.notifyDataSetChanged();
                                    }
                                } catch (Exception e) {
                                    Log.d("Async onSuccess", "Json parsing error:" + e.getMessage(), e);
                                }
                            }
                        }

                    });
        }
}
