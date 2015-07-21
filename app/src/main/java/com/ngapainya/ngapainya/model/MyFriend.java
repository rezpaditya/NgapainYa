package com.ngapainya.ngapainya.model;

import java.io.Serializable;

/**
 * Created by Ari Anggraeni on 7/14/2015.
 */
public class MyFriend implements Serializable {
    public int id;
    public String propic;

    public MyFriend(int id, String propic) {
        this.id = id;
        this.propic = propic;
    }
}
