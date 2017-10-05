package com.codepath.apps.restclienttemplate.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.codepath.apps.restclienttemplate.Utils;
import com.codepath.apps.restclienttemplate.fragments.TweetListFragment;

/**
 * Created by polina on 10/3/17.
 */

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    boolean isUserTweets= false;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return TweetListFragment.newInstance(position, isUserTweets, null);
                case 1:
                    return TweetListFragment.newInstance(position, isUserTweets, null);
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
                    return Utils.HOME;
                case 1:
                    return Utils.MENTIONS;
            }
            return null;
        }
    }

