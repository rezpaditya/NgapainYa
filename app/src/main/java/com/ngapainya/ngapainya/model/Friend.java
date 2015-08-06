package com.ngapainya.ngapainya.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ari Anggraeni on 8/6/2015.
 */
public class Friend implements Parcelable {
    private String friend_id;
    private String friend_name;
    private String friend_ava;
    private String apply_status;

    public String getApply_status() {
        return apply_status;
    }

    public void setApply_status(String apply_status) {
        this.apply_status = apply_status;
    }

    public String getFriend_id() {
        return friend_id;
    }

    public void setFriend_id(String friend_id) {
        this.friend_id = friend_id;
    }

    public String getFriend_name() {
        return friend_name;
    }

    public void setFriend_name(String friend_name) {
        this.friend_name = friend_name;
    }

    public String getFriend_ava() {
        return friend_ava;
    }

    public void setFriend_ava(String friend_ava) {
        this.friend_ava = friend_ava;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }
}
