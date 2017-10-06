package com.codepath.apps.restclienttemplate.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.Utils;
import com.codepath.apps.restclienttemplate.activities.UserProfileActivity;
import com.codepath.apps.restclienttemplate.application.Application;
import com.codepath.apps.restclienttemplate.data.TwitterClient;
import com.codepath.apps.restclienttemplate.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by polina on 10/5/17.
 */

public class FollowersAdapter extends RecyclerView.Adapter<FollowersAdapter.ViewHolder> {
    List<User> followers = new ArrayList<>();
    Context context;
    private TwitterClient client;

    public FollowersAdapter(List<User> followers, Context context) {
        this.followers = followers;
        this.context = context;
        client = Application.getRestClient();

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_follower,
                parent, false);
        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        User user = followers.get(position);
        holder.user = user;
        initImageAdd(user.getFollowing(), holder.addUser);
        Glide.with(context).load(user.getProfileImageUrl())
                .placeholder(R.drawable.tw__ic_logo_default)
                .fitCenter()
                .into(holder.profile);
        holder.name.setText(user.getUserName());
        holder.screenName.setText("@"+user.getScreenName());
        holder.description.setText(user.getDescription());
    }

    @Override
    public int getItemCount() {
        return followers.size();
    }

    private void initImageAdd(boolean isFriends, ImageButton addUser) {
        if(isFriends) {
            addUser.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_added_user_vector));
        } else {
            addUser.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_add_user_vector));
        }
    }

    public class ViewHolder  extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView profile;
        TextView name;
        TextView screenName;
        TextView description;
        ImageButton addUser;
        User user;

        public ViewHolder(View itemView) {
            super(itemView);
            profile = (ImageView) itemView.findViewById(R.id.userImageProfile);
            name = (TextView) itemView.findViewById(R.id.userProfileName);
            screenName = (TextView) itemView.findViewById(R.id.userProfileScreenName);
            description = (TextView) itemView.findViewById(R.id.userDescription);
            addUser = (ImageButton)itemView.findViewById(R.id.userAddButton);
            addUser.setOnClickListener(this);
        }



        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.userAddButton:
                    final boolean isFriends = !user.getFollowing();

                    client.isFriends(user.getUser_id()+"", isFriends,  new JsonHttpResponseHandler(){
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            System.err.println( " tweet was liked");
                            initImageAdd(isFriends, addUser);
                        }
                    });
                    break;
                case R.id.userImageProfile:
                    Intent intentUser = new Intent(context, UserProfileActivity.class);
                    intentUser.putExtra(Utils.IS_ME, false);
                    intentUser.putExtra(Utils.USER_ID, user.getUser_id()+"");
                    context.startActivity(intentUser);
                    break;
            }
        }
    }
}
