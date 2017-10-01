package com.codepath.apps.restclienttemplate.data;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;
import com.raizlabs.android.dbflow.annotation.Database;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Database(name = TweetDatabase.NAME, version = TweetDatabase.VERSION)
public class TweetDatabase {

    public static final String NAME = "RestClientDatabase";

    public static final int VERSION = 2;

    public static void saveTweets(List<Tweet> tweets) {
        for (int i = 0; i < tweets.size(); i++) {
            User user = tweets.get(i).getUser();
            user.save();
            Tweet tweet = tweets.get(i);
            tweet.save();
        }
    }


    public static void clearDB() {
        new Delete().from(User.class).execute();
        new Delete().from(Tweet.class).execute();
    }

    public static ArrayList<Tweet> readFromDB() {
        List<User> existingUsers = new SQLite().select().from(User.class).queryList();
        List<Tweet> existingTweets = new SQLite().select().from(Tweet.class).queryList();
        ArrayList<Tweet> res = new ArrayList<>();


        Map<Long, User> userMap = new HashMap<>();

        if (existingUsers != null) {
            for (User user : existingUsers) {
                userMap.put(user.getUser_id(), user);
            }

        }

        if (existingTweets != null) {
            for (Tweet tweet : existingTweets) {
                if (userMap.containsKey(tweet.getUser_id())) {
                    tweet.setUser(userMap.get(tweet.getUser_id()));
                }
                res.add(tweet);
            }

        }
        return res;
    }

}
