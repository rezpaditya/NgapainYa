package com.ngapainya.ngapainya.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ari Anggraeni on 7/2/2015.
 */
public class Notification implements Parcelable {
    private String text_notif;
    private String propic;

    public String getPropic() {
        return propic;
    }

    public void setPropic(String propic) {
        this.propic = propic;
    }

    public String getText_notif() {
        return text_notif;
    }

    public void setText_notif(String text_notif) {
        this.text_notif = text_notif;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }
}
