package com.codepath.apps.restclienttemplate.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.JsonObject;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by polina on 9/28/17.
 */



public class User extends BaseModel implements Parcelable  {
    public static final String ID_STR = "id_str";
    public static final String NAME = "name";
    public static final String SCREEN_NAME = "screen_name";
    public static final String PROFILE_IMAGE_URL = "profile_image_url";

    @Column(name = "UserId" )
    long user_id;

    @Column(name = "IdStr")
    private String idStr;

    @Column(name = "UserName")
    private String userName;

    @Column(name = "ScreenName")
    private String screenName;

    @Column(name = "ProfileImageUrl")
    private String profileImageUrl;

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

        return user;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.idStr);
        dest.writeString(this.userName);
        dest.writeString(this.screenName);
        dest.writeString(this.profileImageUrl);
    }

    protected User(Parcel in) {
        this.idStr = in.readString();
        this.userName = in.readString();
        this.screenName = in.readString();
        this.profileImageUrl = in.readString();
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
