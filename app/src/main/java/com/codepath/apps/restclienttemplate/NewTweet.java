package com.codepath.apps.restclienttemplate;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.codepath.apps.restclienttemplate.databinding.ActivityNewTweetBinding;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class NewTweet extends AppCompatActivity {
    ActivityNewTweetBinding binding;
    TwitterClient client;
//    User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_tweet);
//        user = getIntent().getParcelableExtra(Utils.USER);
//        if (!TextUtils.isEmpty(user.getProfileImageUrl())) {
//            Glide.with(this).load(user.getProfileImageUrl())
////                    .placeholder(R.mipmap.ic_wifi)
//                    .fitCenter()
//                    .into(binding.twAuthorAvatar);
//        }
        client = Application.getRestClient();
    }


    public void onTweet(View view) {
        client.sendTweet(binding.twEditTweet.getText().toString(), new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.w("ComposeActivity", "HTTP Request failure: " + statusCode + " " +
                        throwable.getMessage());
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Tweet composeTweet = new Tweet();
                if (responseString != null) {
                    Log.i("ComposeFragment", responseString);
                    try {
                        Gson gson = new GsonBuilder().create();
                        JsonObject jsonObject = gson.fromJson(responseString, JsonObject.class);
                        if (jsonObject != null) {
                            composeTweet = Tweet.fromJsonObjectToTweet(jsonObject);
                            Log.i("ComposeFragment", composeTweet.getText());
                        }
                    } catch (JsonParseException e) {
                        Log.d("Compose tweet onSuccess", "Json parsing error:" + e.getMessage(), e);
                    }
                }
//                ((TimelineActivity) getActivity()).updateStatus(composeTweet);
//                dismiss();
            }
        });

    }
}
