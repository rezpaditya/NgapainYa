package com.ngapainya.ngapainya.model;

import java.io.Serializable;

/**
 * Created by Ari Anggraeni on 7/8/2015.
 */
public class Home implements Serializable {
    public String title;
    public int type;

    public Home(String title, int type) {
        this.title = title;
        this.type = type;
    }
}

/**
 * Type
 * 0 = post status
 * 1 = post image
 * 2 = post location
 * 3 = post url
 * 4 = become friend with
 */
