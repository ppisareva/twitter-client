package com.codepath.apps.restclienttemplate.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.codepath.apps.restclienttemplate.Utils;
import com.codepath.apps.restclienttemplate.fragments.TweetListFragment;

/**
 * Created by polina on 10/4/17.
 */

public class SectionsUserContentAdapter extends FragmentPagerAdapter {
    boolean isUserTweets= true;
    String userId;

    public SectionsUserContentAdapter(FragmentManager fm, String userId) {
        super(fm);
        this.userId = userId;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return TweetListFragment.newInstance(position, isUserTweets , userId);
            case 1:
                return TweetListFragment.newInstance(position, isUserTweets, userId);
        }
        return null;
    }


    @Override
    public int getCount() {

        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return Utils.TWEETS;
            case 1:
                return Utils.LIKES;
        }
        return null;
    }
}