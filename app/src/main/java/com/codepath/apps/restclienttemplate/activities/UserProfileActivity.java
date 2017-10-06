package com.codepath.apps.restclienttemplate.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.Utils;
import com.codepath.apps.restclienttemplate.adapter.SectionsUserContentAdapter;
import com.codepath.apps.restclienttemplate.application.Application;
import com.codepath.apps.restclienttemplate.data.TwitterClient;
import com.codepath.apps.restclienttemplate.models.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class UserProfileActivity extends AppCompatActivity {

    private FragmentPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    boolean isMeProfile;
    String userId;
    TwitterClient client;
    User user;
    ImageView profile;
    ImageView header;
    TextView name;
    TextView screenName;
    TextView description;
    TextView followers;
    TextView following;
    ImageButton addUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        profile = (ImageView) findViewById(R.id.userImageProfile);
        name = (TextView) findViewById(R.id.userProfileName);
        header = (ImageView) findViewById(R.id.header);
        screenName = (TextView) findViewById(R.id.userProfileScreenName);
        description = (TextView) findViewById(R.id.userDescription);
        followers = (TextView) findViewById(R.id.userFollowers);
        following = (TextView) findViewById(R.id.userFollowing);
        addUser = (ImageButton)findViewById(R.id.userAddButton);

        client = Application.getRestClient();
        user = Application.getUser();


        if(getIntent().getBooleanExtra(Utils.IS_ME, true)){
            user = Application.getUser();
            userId = user.getUser_id()+"";
            initView();
            addUser.setVisibility(View.GONE);

        } else {
            userId = getIntent().getStringExtra(Utils.USER_ID);
            getUser(userId);
        }

        mSectionsPagerAdapter = new SectionsUserContentAdapter(getSupportFragmentManager(), userId);
        mViewPager = (ViewPager) findViewById(R.id.userContainer);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.userTabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    private void initView() {
        initImageAdd(user.getFollowing());
        Glide.with(this).load(user.getBackgroundImageUrl())
                .placeholder(R.drawable.tw__ic_logo_default)
                .centerCrop()
                .into(header);
        Glide.with(this).load(user.getProfileImageUrl())
                .placeholder(R.drawable.tw__ic_logo_default)
                .fitCenter()
                .into(profile);
        name.setText(user.getUserName());
        screenName.setText("@"+user.getScreenName());
        description.setText(user.getDescription());
        following.setText(user.getFollowingsCount() + " " + getString(R.string.following));
        followers.setText(user.getFollowersCount() + " " + getString(R.string.followers));
    }

    public void onFollowers(View v){
        Intent intent = new Intent(UserProfileActivity.this, FollowersActivity.class);
        intent.putExtra(Utils.USER_ID, userId);
        intent.putExtra(Utils.IS_FOLLOWERS, true);
        startActivity(intent);
    }

    public  void onFollowing (View v){
        Intent intent = new Intent(UserProfileActivity.this, FollowersActivity.class);
        intent.putExtra(Utils.USER_ID, userId);
        intent.putExtra(Utils.IS_FOLLOWERS, false);
        startActivity(intent);
    }
    public void onAddNewUser(View v){
        final boolean isFriends = !user.getFollowing();
        client.isFriends(userId, isFriends,  new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.err.println( " tweet was liked");
               initImageAdd(isFriends);
            }
        });
    }

    private void initImageAdd(boolean isFriends) {
        if(isFriends) {
            addUser.setImageDrawable(getResources().getDrawable(R.drawable.ic_added_user_vector));
        } else {
            addUser.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_user_vector));
        }
    }


    private void getUser(String userId) {
        client.getUser(userId, new JsonHttpResponseHandler(){
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (response != null) {
                    Gson gson = new GsonBuilder().create();
                    JsonObject jsonObject = gson.fromJson(response.toString(), JsonObject.class);
                    if (jsonObject != null) {
                        user = User.fromJsonObjectToUser(jsonObject);
                        initView();
                    }
                }
            }
        });
    }



}
