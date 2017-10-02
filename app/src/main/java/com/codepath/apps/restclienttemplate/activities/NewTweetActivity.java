package com.codepath.apps.restclienttemplate.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.data.TwitterClient;
import com.codepath.apps.restclienttemplate.Utils;
import com.codepath.apps.restclienttemplate.application.Application;
import com.codepath.apps.restclienttemplate.databinding.ActivityNewTweetBinding;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.loopj.android.http.TextHttpResponseHandler;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.util.TextUtils;

import static com.codepath.apps.restclienttemplate.Utils.MAX_TWEET_LENGHT;

public class NewTweetActivity extends AppCompatActivity {
    ActivityNewTweetBinding binding;
    TwitterClient client;
    User user = new User();
    Tweet tweet = new Tweet();
    String  replyID = "";
    EditText text;
    SharedPreferences pref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = Application.getRestClient();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_tweet);
        user = Application.getUser();
        text = binding.twEditTweet;
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        text.setText(pref.getString(Utils.TWEET, ""));
        replyID = pref.getString(Utils.ID_REPLY, "");
        binding.twCharCount.setText(MAX_TWEET_LENGHT - text.length()+"");
        text.setSelection(text.length());
        if(getIntent().hasExtra(Utils.TWEET)){
            tweet = getIntent().getParcelableExtra(Utils.TWEET);
            replyID = tweet.getIdStr();
            text.setText("@"+tweet.getUser().getScreenName() +" ");
            int length = text.getText().length();
            text.setSelection(length);
            binding.twCharCount.setText(MAX_TWEET_LENGHT - length+"");
        }
        if (user != null && !TextUtils.isEmpty(user.getProfileImageUrl())) {
            Glide.with(this).load(user.getProfileImageUrl())
                   .placeholder(R.drawable.tw__ic_logo_default)
                    .fitCenter()
                    .into(binding.twAuthorAvatar);
        }
        binding.twComposerClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int length = 0;
                if (s.length() > MAX_TWEET_LENGHT) {
                    binding.twCharCount.setTextColor(Color.RED);
                    binding.twPostTweet.setEnabled(false);
                } else {
                    binding.twCharCount.setTextColor(Color.BLACK);
                    binding.twPostTweet.setEnabled(true);
                }
                length = MAX_TWEET_LENGHT - s.length();
                binding.twCharCount.setText(length+"");

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        getSupportActionBar().setTitle("");
        getSupportActionBar().setLogo(R.drawable.tw__composer_logo_white);
    }

    @Override
    public void finish() {

        SharedPreferences.Editor edit = pref.edit();
        edit.putString(Utils.TWEET, binding.twEditTweet.getText().toString());
        edit.putString(Utils.ID_REPLY, replyID);
        edit.commit();
        super.finish();
    }

    public void onTweet(View view) {
        client.sendTweet(text.getText().toString(), replyID, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        throwable.getMessage();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Tweet newTweet;
                if (responseString != null) {
                    try {
                        Gson gson = new GsonBuilder().create();
                        JsonObject jsonObject = gson.fromJson(responseString, JsonObject.class);
                        if (jsonObject != null) {
                            newTweet = Tweet.fromJsonObjectToTweet(jsonObject);
                            Intent data = new Intent();
                            data.putExtra(Utils.TWEET, newTweet);
                            setResult(RESULT_OK, data);
                            SharedPreferences.Editor edit = pref.edit();
                            edit.clear();
                            edit.commit();
                            finish();
                        }
                    } catch (JsonParseException e) {
                       e.printStackTrace();
                    }
                }
            }
        });
    }
}