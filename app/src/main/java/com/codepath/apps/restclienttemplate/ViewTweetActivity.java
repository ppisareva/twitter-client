package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.databinding.ActivityViewTweetBinding;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.util.TextUtils;

public class ViewTweetActivity extends AppCompatActivity {

    ActivityViewTweetBinding binding;
    TwitterClient twitterClient;
    Tweet tweet;
    Intent data = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        twitterClient = Application.getRestClient();
        tweet = getIntent().getParcelableExtra(Utils.TWEET);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_tweet);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (tweet.getUser() != null && !TextUtils.isEmpty(tweet.getUser().getProfileImageUrl())) {
            Glide.with(this).load(tweet.getUser().getProfileImageUrl())
                    .placeholder(R.drawable.tw__ic_logo_default)
                    .fitCenter()
                    .into(binding.ivProfile);
        }
        binding.tvName.setText(tweet.getUser().getUserName());
        binding.tvScreenName.setText("@"+tweet.getUser().getScreenName());
        binding.tvText.setText(tweet.getText().toString());
        if(tweet.getMedia_type().equals("photo")){
            binding.ivImage.setVisibility(View.VISIBLE);
            Glide.with(this).load(tweet.getMedia_url())
//                    .placeholder(R.mipmap.ic_wifi)
                    .centerCrop()
                    .into(binding.ivImage);
        }
        else {
            binding.ivImage.setVisibility(View.GONE);
        }

        binding.tvTime.setText(Utils.longFormat(tweet.getCreated_at()));
        binding.tvReplyCount.setText(tweet.getRetweet_count() + " " +  getString(R.string.retweet));
        binding.tvLikeCount.setText(tweet.getFavourite_count()+ " " + getString(R.string.liked));
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        binding.etDetailsReplyToText.setText("@"+tweet.getUser().getScreenName());
        binding.etDetailsReplyToText.setSelection(binding.etDetailsReplyToText.length());
        binding.tvCount.setText(140-binding.etDetailsReplyToText.length()+"");

        binding.etDetailsReplyToText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int length = 0;
                if (s.length() > 140) {
                    binding.tvCount.setTextColor(Color.RED);
                    binding.btnPostTweet.setEnabled(false);
                } else {
                    binding.tvCount.setTextColor(Color.BLACK);
                    binding.btnPostTweet.setEnabled(true);
                }
                length = 140 - s.length();
                binding.tvCount.setText(length+"");

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.btnRetweet.setImageDrawable(tweet.isRetweeted()?getResources().
                getDrawable(R.drawable.ic_retweet_vector_true):
        getResources().getDrawable(R.drawable.ic_retweet_vector));
        binding.btnRetweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Boolean isRetweet = tweet.isRetweeted()? false: true;
                tweet.setRetweeted(isRetweet);
                twitterClient.retweet(tweet.getIdStr(), isRetweet, new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        System.err.println( " tweet was retweeted");
                        data.putExtra(Utils.RETWEET, isRetweet);
                        if(isRetweet) {
                            binding.btnRetweet.setImageDrawable(getResources().getDrawable(R.drawable.ic_retweet_vector_true));
                        } else {
                            binding.btnRetweet.setImageDrawable(getResources().getDrawable(R.drawable.ic_retweet_vector));
                        }
                    }
                });

            }
        });

        binding.btnLike.setImageDrawable(tweet.isFavorited()?
                getResources().getDrawable(R.drawable.ic_like_vector_true):
                getResources().getDrawable(R.drawable.ic_like_vector));
        binding.btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final boolean isLiked = tweet.isFavorited()?false:true;
                tweet.setFavorited(isLiked);
                twitterClient.isLike(tweet.getIdStr(), isLiked, new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        data.putExtra(Utils.FAVORITE, isLiked);
                        System.err.println( " tweet was liked");
                        if(isLiked) {
                            binding.btnLike.setImageDrawable(getResources().getDrawable(R.drawable.ic_like_vector_true));
                        } else {
                            binding.btnLike.setImageDrawable(getResources().getDrawable(R.drawable.ic_like_vector));
                        }
                    }
                });
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void finish() {
        data.putExtra(Utils.POSITION, getIntent().getIntExtra((Utils.POSITION), -1));
        setResult(data.getExtras().isEmpty() ? RESULT_CANCELED : RESULT_OK, data);
        super.finish();
    }

    public void onTweetSend(View view) {
        twitterClient.sendTweet(binding.etDetailsReplyToText.getText().toString(), tweet.getIdStr(), new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                throwable.getMessage();
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                Tweet newTweet = new Tweet();
                if (responseString != null) {
                    try {
                        Gson gson = new GsonBuilder().create();
                        JsonObject jsonObject = gson.fromJson(responseString, JsonObject.class);
                        if (jsonObject != null) {
                            newTweet = Tweet.fromJsonObjectToTweet(jsonObject);
                            data.putExtra(Utils.REPLY, newTweet);
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
