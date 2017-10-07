package com.codepath.apps.restclienttemplate.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.Utils;
import com.codepath.apps.restclienttemplate.adapter.SectionsPagerAdapter;
import com.codepath.apps.restclienttemplate.adapter.TweetAdapter;
import com.codepath.apps.restclienttemplate.data.TwitterClient;
import com.codepath.apps.restclienttemplate.models.Tweet;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class TweetsListActivity extends AppCompatActivity  {

    TwitterClient client;
    private static final String CLOSE_BUTTON = "mCloseButton";
    RecyclerView recyclerView;
    ArrayList<Tweet> tweets= new ArrayList<>();
    TweetAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    public static final int REQUEST_CODE =1111;
    boolean firstEnter = false;

    private FragmentPagerAdapter mSectionsPagerAdapter;

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
        toolbar.setTitle("");
        toolbar.setLogo(getResources().getDrawable(R.drawable.tw__composer_logo_white));
        setSupportActionBar(toolbar);

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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_list_of_tweets, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setSubmitButtonEnabled(true);
        int searchImgId = android.support.v7.appcompat.R.id.search_button;
        searchView.requestFocusFromTouch();
        searchView.setFocusable(true);
        hideCloseButton(searchView);
        ImageView v = (ImageView) searchView.findViewById(searchImgId);
        v.setImageResource(android.R.drawable.ic_menu_search);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(TweetsListActivity.this, SearchResultActivity.class);
                intent.putExtra(Utils.SEARCH, query);
                startActivity(intent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });
        return true;
    }

    private void hideCloseButton(SearchView mSearchView) {
        try {
            Field searchField = SearchView.class.getDeclaredField(CLOSE_BUTTON);
            searchField.setAccessible(true);
            ImageView mSearchCloseButton = (ImageView) searchField.get(mSearchView);
            if (mSearchCloseButton != null) {
                mSearchCloseButton.setEnabled(false);
                mSearchCloseButton.setImageDrawable(getResources().getDrawable(android.R.color.transparent));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.user_profile:
                startActivity(new Intent(TweetsListActivity.this, UserProfileActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
