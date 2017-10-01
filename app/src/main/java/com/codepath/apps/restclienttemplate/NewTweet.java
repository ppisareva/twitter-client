package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.bumptech.glide.Glide;
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

public class NewTweet extends AppCompatActivity {
    ActivityNewTweetBinding binding;
    TwitterClient client;
    User user = new User();
    Tweet tweet = new Tweet();
    String  replyID = "";
    EditText text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_tweet);
        user = Application.getUser();
        text = binding.twEditTweet;
        if(getIntent().hasExtra(Utils.TWEET)){
            tweet = getIntent().getParcelableExtra(Utils.TWEET);
            replyID = tweet.getIdStr();
            text.setText("@"+tweet.getUser().getScreenName() +" ");
            int length = text.getText().length();
            text.setSelection(length);
            binding.twCharCount.setText(140 - length+"");

        }
        if (user != null && !TextUtils.isEmpty(user.getProfileImageUrl())) {
            Glide.with(this).load(user.getProfileImageUrl())
                   .placeholder(R.drawable.tw__ic_logo_default)
                    .fitCenter()
                    .into(binding.twAuthorAvatar);
        }
        client = Application.getRestClient();
        //todo
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
                if (s.length() > 140) {
                    binding.twCharCount.setTextColor(Color.RED);
                    binding.twPostTweet.setEnabled(false);
                } else {
                    binding.twCharCount.setTextColor(Color.BLACK);
                    binding.twPostTweet.setEnabled(true);
                }
                length = 140 - s.length();
                binding.twCharCount.setText(length+"");

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }


    public void onTweet(View view) {
        client.sendTweet(text.getText().toString(), replyID, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        throwable.getMessage();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Tweet newTweet = new Tweet();
                if (responseString != null) {
                    try {
                        Gson gson = new GsonBuilder().create();
                        JsonObject jsonObject = gson.fromJson(responseString, JsonObject.class);
                        if (jsonObject != null) {
                            newTweet = Tweet.fromJsonObjectToTweet(jsonObject);
                            Intent data = new Intent();
                            data.putExtra(Utils.TWEET, newTweet);
                            setResult(RESULT_OK, data);
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