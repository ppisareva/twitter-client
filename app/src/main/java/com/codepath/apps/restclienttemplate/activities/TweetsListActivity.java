package com.codepath.apps.restclienttemplate.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.Utils;
import com.codepath.apps.restclienttemplate.adapter.SectionsPagerAdapter;
import com.codepath.apps.restclienttemplate.adapter.TweetAdapter;
import com.codepath.apps.restclienttemplate.data.TwitterClient;
import com.codepath.apps.restclienttemplate.models.Tweet;

import java.util.ArrayList;

public class TweetsListActivity extends AppCompatActivity  {

    TwitterClient client;
    RecyclerView recyclerView;
    ArrayList<Tweet> tweets= new ArrayList<>();
    TweetAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    public static final int REQUEST_CODE =1111;
    boolean firstEnter = false;

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweets_list);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);


        toolbar.setLogo(getResources().getDrawable(R.drawable.tw__composer_logo_white));
        setSupportActionBar(toolbar);
//
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

}
