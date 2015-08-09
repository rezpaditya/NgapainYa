package com.ngapainya.ngapainya.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ari Anggraeni on 8/6/2015.
 */
public class Comment implements Parcelable {
    private String comment_id;
    private String user_id;
    private String user_name;
    private String user_ava;
    private String user_comment;
    private String time;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_ava() {
        return user_ava;
    }

    public void setUser_ava(String user_ava) {
        this.user_ava = user_ava;
    }

    public String getUser_comment() {
        return user_comment;
    }

    public void setUser_comment(String user_comment) {
        this.user_comment = user_comment;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }
}
