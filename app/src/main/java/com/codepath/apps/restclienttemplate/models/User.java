package com.codepath.apps.restclienttemplate.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.codepath.apps.restclienttemplate.data.TweetDatabase;
import com.google.gson.JsonObject;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by polina on 9/28/17.
 */



@Table(database = TweetDatabase.class)
public class User extends BaseModel implements Parcelable  {
    public static final String ID_STR = "id_str";
    public static final String NAME = "name";
    public static final String SCREEN_NAME = "screen_name";
    public static final String PROFILE_IMAGE_URL = "profile_image_url";
    public static final String PROFILE_BACKGROUND_IMAGE_URL = "profile_banner_url";
    public static final String DESCRIPTION = "description";
    public static final String FOLLOWERS_COUNT = "followers_count";
    public static final String FRIENDS_COUNT = "friends_count";
    public static final String IS_FRIEND = "following";

    @Column(name = "UserId")
    @PrimaryKey
    long user_id;

    @Column(name = "IdStr")
    private String idStr;

    @Column(name = "UserName")
    private String userName;

    @Column(name = "ScreenName")
    private String screenName;

    @Column(name = "ProfileImageUrl")
    private String profileImageUrl;

    public String getBackgroundImageUrl() {
        return backgroundImageUrl;
    }

    public void setBackgroundImageUrl(String backgroundImageUrl) {
        this.backgroundImageUrl = backgroundImageUrl;
    }

    private String backgroundImageUrl;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    private String description;

    private String followersCount;

    private String followingsCount;

    private Boolean following;


    public String getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(String followersCount) {
        this.followersCount = followersCount;
    }

    public String getFollowingsCount() {
        return followingsCount;
    }

    public void setFollowingsCount(String followingsCount) {
        this.followingsCount = followingsCount;
    }

    public Boolean getFollowing() {
        return following;
    }

    public void setFollowing(Boolean following) {
        this.following = following;
    }





    public User() {
        super();
    }

    public String getIdStr() {
        return idStr;
    }

    public void setIdStr(String idStr) {
        this.idStr = idStr;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public static User fromJsonObjectToUser(JsonObject jsonUserObject) {
        User user = new User();
        if (jsonUserObject.has(ID_STR)) {
            user.setIdStr(jsonUserObject.get(ID_STR).getAsString());
            user.setUser_id(Long.parseLong(jsonUserObject.get(ID_STR).getAsString()));
        }
        if (jsonUserObject.has(NAME)) {
            user.setUserName(jsonUserObject.get(NAME).getAsString());
        }
        if (jsonUserObject.has(SCREEN_NAME)) {
            user.setScreenName(jsonUserObject.get(SCREEN_NAME).getAsString());
        }
        if (jsonUserObject.has(PROFILE_IMAGE_URL)) {
            user.setProfileImageUrl(jsonUserObject.get(PROFILE_IMAGE_URL).getAsString());
        }
        if (jsonUserObject.has(DESCRIPTION)) {
            user.setDescription(jsonUserObject.get(DESCRIPTION).getAsString());
        }
        if (jsonUserObject.has(FOLLOWERS_COUNT)) {
            user.setFollowersCount(jsonUserObject.get(FOLLOWERS_COUNT).getAsString());
        }
        if (jsonUserObject.has(FRIENDS_COUNT)) {
            user.setFollowingsCount(jsonUserObject.get(FRIENDS_COUNT).getAsString());
        }
        if (jsonUserObject.has(IS_FRIEND)) {
            user.setFollowing(jsonUserObject.get(IS_FRIEND).getAsBoolean());
        }
        if (jsonUserObject.has(PROFILE_BACKGROUND_IMAGE_URL)) {
            user.setBackgroundImageUrl(jsonUserObject.get(PROFILE_BACKGROUND_IMAGE_URL).getAsString());
        }
        return user;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.user_id);
        dest.writeString(this.idStr);
        dest.writeString(this.userName);
        dest.writeString(this.screenName);
        dest.writeString(this.profileImageUrl);
        dest.writeString(this.backgroundImageUrl);
        dest.writeString(this.description);
        dest.writeString(this.followersCount);
        dest.writeString(this.followingsCount);
        dest.writeValue(this.following);
    }

    protected User(Parcel in) {
        this.user_id = in.readLong();
        this.idStr = in.readString();
        this.userName = in.readString();
        this.screenName = in.readString();
        this.profileImageUrl = in.readString();
        this.backgroundImageUrl = in.readString();
        this.description = in.readString();
        this.followersCount = in.readString();
        this.followingsCount = in.readString();
        this.following = (Boolean) in.readValue(Boolean.class.getClassLoader());
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
