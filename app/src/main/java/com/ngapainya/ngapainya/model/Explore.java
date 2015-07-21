package com.ngapainya.ngapainya.model;

import java.io.Serializable;

/**
 * Created by Ari Anggraeni on 7/7/2015.
 */
public class Explore implements Serializable {
    public String img;
    public String title;
    public String text;
    public String strDate;
    public String endDate;
    public String type;

    public Explore(String img, String title, String text, String strDate, String endDate, String type) {
        this.img = img;
        this.title = title;
        this.text = text;
        this.strDate = strDate;
        this.endDate = endDate;
        this.type = type;
    }
}
