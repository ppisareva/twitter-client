package com.codepath.apps.restclienttemplate;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by polina on 9/28/17.
 */

public class TweetAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private List<Tweet> tweetList;
    private Activity context;

    private final int TWEET_NO_IMAGE = 0;
    private final int TWEET_IMAGE = 1;

    public static final int REQUEST_CODE =1111;
    TwitterClient client;

    public TweetAdapter(Activity context, List<Tweet> tweets) {
        tweetList = tweets;
        this.context = context;
    }


    @Override
    public int getItemCount() {
        return tweetList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Tweet tweet = tweetList.get(position);

        if (tweet.getMedia_type().equals("photo")) {
            return TWEET_IMAGE;
        }

        return TWEET_NO_IMAGE;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        switch (viewType) {
            case TWEET_IMAGE:
                View v1 = inflater.inflate(R.layout.tweet_image,
                        viewGroup, false);
                viewHolder = new TweetImageViewHolder( v1);
                break;
            case TWEET_NO_IMAGE:
                View v2 = inflater.inflate(R.layout.tweet_no_image,
                        viewGroup, false);
                viewHolder = new TweetNoImageViewHolder(v2);
                break;
            default:
                v2 = inflater.inflate(R.layout.tweet_no_image,
                        viewGroup, false);
                viewHolder = new TweetNoImageViewHolder(v2);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType()) {
            case TWEET_IMAGE:
                TweetImageViewHolder vh1 = (TweetImageViewHolder) viewHolder;
                bindViewWithImage(vh1, position);
                break;
            case TWEET_NO_IMAGE:
                TweetNoImageViewHolder vh2 = (TweetNoImageViewHolder) viewHolder;
                bindViewTextOnly(vh2, position);
                break;
            default:
                TweetNoImageViewHolder vh = (TweetNoImageViewHolder) viewHolder;
                bindViewTextOnly(vh, position);
                break;
        }
    }

    private void bindViewWithImage(TweetImageViewHolder viewHolder, int position) {

        Tweet tweet = tweetList.get(position);
        viewHolder.setTweet(tweet);
        viewHolder.tvTweet.setText(tweet.getText());
        viewHolder.twName.setText(tweet.getUser().getUserName());
        viewHolder.twScreenName.setText("@" + tweet.getUser().getScreenName());
        if(tweet.isFavorited()){
            viewHolder.ibLike.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_like_vector_true));
        } else {
            viewHolder.ibLike.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_like_vector));
        }

        if(tweet.isRetweeted()){
            viewHolder.ibRetweet.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_retweet_vector_true));
        } else {
            viewHolder.ibRetweet.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_retweet_vector));
        }
        if (!TextUtils.isEmpty(tweet.getUser().getProfileImageUrl())) {
            Glide.with(context).load(tweet.getUser().getProfileImageUrl())
//                    .placeholder(R.mipmap.ic_wifi)
                    .fitCenter()
                    .into(viewHolder.ivProfileImage);
        }

        viewHolder.timeStamp.setText(Utils.getRelativeTimeAgo(tweet.getCreated_at()));
        Glide.with(context).load(tweet.getMedia_url())
//                    .placeholder(R.mipmap.ic_wifi)
                .centerCrop()
                .into(viewHolder.ivImage);
//        viewHolder.countLike.setText(tweet.getFavourite_count()==null ? "0" : tweet.getFavourite_count());
//        viewHolder.countRetweet.setText(tweet.getRetweet_count());

    }


    private void bindViewTextOnly(TweetNoImageViewHolder viewHolder, int position) {
        Tweet tweet = tweetList.get(position);
        viewHolder.setTweet(tweet);
        if (!TextUtils.isEmpty(tweet.getUser().getProfileImageUrl())) {
            Glide.with(context).load(tweet.getUser().getProfileImageUrl())
//                    .placeholder(R.mipmap.ic_wifi)
                    .fitCenter()
                    .into(viewHolder.ivProfileImage);
        }

        viewHolder.tvTweet.setText(tweet.getText());
        viewHolder.twName.setText(tweet.getUser().getUserName());
        viewHolder.twScreenName.setText("@" + tweet.getUser().getScreenName());
        viewHolder.timeStamp.setText(Utils.getRelativeTimeAgo(tweet.getCreated_at()));
        if(tweet.isFavorited()){
            viewHolder.ibLike.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_like_vector_true));
        } else {
            viewHolder.ibLike.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_like_vector));
        }

        if(tweet.isRetweeted()){
            viewHolder.ibRetweet.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_retweet_vector_true));
        } else {
            viewHolder.ibRetweet.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_retweet_vector));
        }

//        viewHolder.countLike.setText(tweet.getFavourite_count());
//        viewHolder.countRetweet.setText(tweet.getRetweet_count());
    }



    private class TweetImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView ivProfileImage;
        TextView twName;
        TextView twScreenName;
        TextView tvTweet;
        ImageView ivImage;
        ImageButton ibReply;
        ImageButton ibRetweet;
        ImageButton ibLike;
        TextView countRetweet;
        TextView countShare;
        TextView countLike;
        TextView timeStamp;
        Tweet tweet;

        public Tweet getTweet() {
            return tweet;
        }

        public void setTweet(Tweet tweet) {
            this.tweet = tweet;
        }


        public TweetImageViewHolder(View view) {
            super(view);
            ivProfileImage = (ImageView) view.findViewById(R.id.imgProfile);
            twName = (TextView)view.findViewById(R.id.tvName);
            twScreenName = (TextView)view.findViewById(R.id.tvScreenName);
            tvTweet = (TextView) view.findViewById(R.id.tvTweet);
            ivImage =(ImageView) view.findViewById(R.id.imgTweet);
            ibReply = (ImageButton) view.findViewById(R.id.btnReply);
            ibReply.setOnClickListener(this);
            ibRetweet = (ImageButton) view.findViewById(R.id.btnRetweet);
            ibRetweet.setOnClickListener(this);
            ibLike =(ImageButton) view.findViewById(R.id.btnLike);
            ibLike.setOnClickListener(this);
            countRetweet = (TextView) view.findViewById(R.id.tvReplyCount);
            countShare = (TextView) view.findViewById(R.id.tvRetweetCount);
            countLike = (TextView) view.findViewById(R.id.tvLikeCount);
            timeStamp = (TextView) view.findViewById(R.id.tvTimeStamp);
        }

        @Override
        public void onClick(View v) {
            client = Application.getRestClient();
            switch (v.getId()){
                case R.id.btnReply:
                    Intent intent = new Intent(context, NewTweet.class);
                    intent.putExtra(Utils.TWEET, tweet);
                    context.startActivityForResult(intent, REQUEST_CODE);
                    break;
                case R.id.btnRetweet:
                    final Boolean isRetweet = tweet.isRetweeted()? false: true;
                    tweet.setRetweeted(isRetweet);
                    client.retweet(tweet.getIdStr(), isRetweet, new JsonHttpResponseHandler(){
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            System.err.println( " tweet was retweeted");
                            if(isRetweet) {
                                ibRetweet.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_retweet_vector_true));
                            } else {
                                ibRetweet.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_retweet_vector));
                            }
                        }
                    });

                    break;
                case R.id.btnLike:
                   final boolean isLiked = tweet.isFavorited()?false:true;
                    tweet.setFavorited(isLiked);
                    client.isLike(tweet.getIdStr(), isLiked, new JsonHttpResponseHandler(){
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                            System.err.println( " tweet was liked");
                            if(isLiked) {
                                ibLike.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_like_vector_true));
                            } else {
                                ibLike.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_like_vector));
                            }
                        }
                    });
                    break;
                default:
            }
        }



}

    private class TweetNoImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView ivProfileImage;
        TextView twName;
        TextView twScreenName;
        TextView tvTweet;
        ImageButton ibReply;
        ImageButton ibRetweet;
        ImageButton ibLike;
        TextView countRetweet;
        TextView countShare;
        TextView countLike;
        TextView timeStamp;
        Tweet tweet;

        public Tweet getTweet() {
            return tweet;
        }

        public void setTweet(Tweet tweet) {
            this.tweet = tweet;
        }


        public TweetNoImageViewHolder(View view) {
            super(view);
            ivProfileImage = (ImageView) view.findViewById(R.id.imgProfile);
            twName = (TextView) view.findViewById(R.id.tvName);
            twScreenName = (TextView) view.findViewById(R.id.tvScreenName);
            tvTweet = (TextView) view.findViewById(R.id.tvTweet);
            ibReply = (ImageButton) view.findViewById(R.id.btnReply);
            ibReply.setOnClickListener(this);
            ibRetweet = (ImageButton) view.findViewById(R.id.btnRetweet);
            ibRetweet.setOnClickListener(this);
            ibLike = (ImageButton) view.findViewById(R.id.btnLike);
            ibLike.setOnClickListener(this);
            countRetweet = (TextView) view.findViewById(R.id.tvReplyCount);
            countShare = (TextView) view.findViewById(R.id.tvRetweetCount);
            countLike = (TextView) view.findViewById(R.id.tvLikeCount);
            timeStamp = (TextView) view.findViewById(R.id.tvTimeStamp);


        }

        @Override
        public void onClick(View v) {
            client = Application.getRestClient();
            switch (v.getId()){
                case R.id.btnReply:
                    Intent intent = new Intent(context, NewTweet.class);
                    intent.putExtra(Utils.TWEET, tweet);
                    context.startActivityForResult(intent, REQUEST_CODE);
                    break;
                case R.id.btnRetweet:
                    final Boolean isRetweet = tweet.isRetweeted()? false: true;
                    tweet.setRetweeted(isRetweet);
                    client.retweet(tweet.getIdStr(), isRetweet, new JsonHttpResponseHandler(){
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            System.err.println( " tweet was retweeted");
                            if(isRetweet) {
                                ibRetweet.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_retweet_vector_true));
                            } else {
                                ibRetweet.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_retweet_vector));
                            }
                        }
                    });

                    break;
                case R.id.btnLike:
                    final boolean isLiked = tweet.isFavorited()?false:true;
                    tweet.setFavorited(isLiked);
                    client.isLike(tweet.getIdStr(), isLiked, new JsonHttpResponseHandler(){
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                            System.err.println( " tweet was liked");
                            if(isLiked) {
                                ibLike.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_like_vector_true));
                            } else {
                                ibLike.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_like_vector));
                            }
                        }
                    });
                    break;
                default:
            }
        }
    }


}
