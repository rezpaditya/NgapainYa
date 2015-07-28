package com.ngapainya.ngapainya.model;

import java.io.Serializable;

/**
 * Created by Ari Anggraeni on 7/8/2015.
 */
public class Home implements Serializable {
    /**
     * Type
     * 0 = post status
     * 1 = post image
     * 2 = post location
     * 3 = post url
     * 4 = become friend with
     */
    private String act_id;
    private String act_content;
    private String act_type;
    private String act_url;
    private String act_lat;
    private String act_lng;
    private String act_address;
    private String username;
    private String user_pic;
    private String created_at;

    public String getAct_id() {
        return act_id;
    }

    public void setAct_id(String act_id) {
        this.act_id = act_id;
    }

    public String getAct_content() {
        return act_content;
    }

    public void setAct_content(String act_content) {
        this.act_content = act_content;
    }

    public String getAct_type() {
        return act_type;
    }

    public void setAct_type(String act_type) {
        this.act_type = act_type;
    }

    public String getAct_url() {
        return act_url;
    }

    public void setAct_url(String act_url) {
        this.act_url = act_url;
    }

    public String getAct_lat() {
        return act_lat;
    }

    public void setAct_lat(String act_lat) {
        this.act_lat = act_lat;
    }

    public String getAct_lng() {
        return act_lng;
    }

    public void setAct_lng(String act_lng) {
        this.act_lng = act_lng;
    }

    public String getAct_address() {
        return act_address;
    }

    public void setAct_address(String act_address) {
        this.act_address = act_address;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUser_pic() {
        return user_pic;
    }

    public void setUser_pic(String user_pic) {
        this.user_pic = user_pic;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}

