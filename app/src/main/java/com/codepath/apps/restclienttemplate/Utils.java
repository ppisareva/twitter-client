package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.format.DateUtils;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by polina on 9/26/17.
 */

public class Utils {
    public static final String APP_SHARED_VALUE = "ololo";
    public static final String TOCKEN = "TOKEN";
    public static final String USER = "user";
    public static final String TWEET = "tweet";
    public static final int VIEW_TWEET_REQUEST = 1234;
    public static final String POSITION = "pos";
    public static final String RETWEET = "retweet";
    public static final String FAVORITE = "fav";
    public static final String REPLY = "reply";
    public static final String ID_REPLY = "id" ;
    public static final String PHOTO = "photo";
    public static final CharSequence HOME = "HOME";
    public static final CharSequence MENTIONS = "MENTIONS";
    public static final String IS_ME = "isMe";
    public static final String USER_ID = "user_id";
    public static final CharSequence TWEETS = "tweets";
    public static final CharSequence LIKES = "likes";
    public static final String IS_FOLLOWERS = "is_followers";
    public static int MAX_TWEET_LENGHT = 140;
    public static String SEARCH ="search";

    public static Boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public static boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (InterruptedException | IOException e) { e.printStackTrace(); }
        return false;
    }

    public static String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.err.println("Time " + relativeDate);
        return relativeDate;
    }


    public static String shortFormat(String input) {

        String[] parts = input.split(" ");

        String numericPart ="";
        String timePart ="";


        if (parts[0].equalsIgnoreCase("in") && StringUtils.isNumeric(parts[1])) {
            numericPart = parts[1];
            if (parts[2].contains("minute")) {
                timePart = "m";
            } else if (parts[2].contains("hour")) {
                timePart = "h";
            } else if (parts[2].contains("second")) {
                timePart = "s";
            }
            return "in " + numericPart + timePart;
        }

        if (StringUtils.isNumeric(parts[0])) {
            numericPart = parts[0];
        }

        if (parts[1].contains("minute")) {
            timePart = "m";
        } else if (parts[1].contains("hour")) {
            timePart = "h";
        } else if (parts[1].contains("second")) {
            timePart = "s";
        }

        if (!StringUtils.isEmpty(numericPart) && !StringUtils.isEmpty(timePart)) {
            return numericPart + timePart;
        }
        return input;
    }

    public static String longFormat(String created_at) {
        try {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        DateFormat df = new SimpleDateFormat(twitterFormat);
            Date date = df.parse(created_at);
            df = new SimpleDateFormat("HH:mm - dd MMM EEE");
            return df.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return created_at;
        }
    }
}
