package com.ngapainya.ngapainya.model;

import java.io.Serializable;

/**
 * Created by Ari Anggraeni on 7/2/2015.
 */
public class Notification implements Serializable {
    public String text_notif;

    public Notification(String text_notif){
        this.text_notif = text_notif;
    }
}
