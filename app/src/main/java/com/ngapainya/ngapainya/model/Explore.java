package com.ngapainya.ngapainya.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ari Anggraeni on 7/7/2015.
 */
public class Explore implements Parcelable {
    private String program_id;
    private String program_name;
    private String user_id;
    private String program_desc;
    private String program_date_start;
    private String program_date_end;
    private String user_pic;
    private String user_fullname;
    private String program_age_min;
    private String program_age_max;
    private String program_fee;
    private String program_url;
    private String program_quota;

    public String getProgram_quota() {
        return program_quota;
    }

    public void setProgram_quota(String program_quota) {
        this.program_quota = program_quota;
    }

    public String getUser_fullname() {
        return user_fullname;
    }

    public void setUser_fullname(String user_fullname) {
        this.user_fullname = user_fullname;
    }

    public String getProgram_age_min() {
        return program_age_min;
    }

    public void setProgram_age_min(String program_age_min) {
        this.program_age_min = program_age_min;
    }

    public String getProgram_age_max() {
        return program_age_max;
    }

    public void setProgram_age_max(String program_age_max) {
        this.program_age_max = program_age_max;
    }

    public String getProgram_fee() {
        return program_fee;
    }

    public void setProgram_fee(String program_fee) {
        this.program_fee = program_fee;
    }

    public String getProgram_url() {
        return program_url;
    }

    public void setProgram_url(String program_url) {
        this.program_url = program_url;
    }

    public String getProgram_id() {
        return program_id;
    }

    public void setProgram_id(String program_id) {
        this.program_id = program_id;
    }

    public String getProgram_name() {
        return program_name;
    }

    public void setProgram_name(String program_name) {
        this.program_name = program_name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getProgram_desc() {
        return program_desc;
    }

    public void setProgram_desc(String program_desc) {
        this.program_desc = program_desc;
    }

    public String getProgram_date_start() {
        return program_date_start;
    }

    public void setProgram_date_start(String program_date_start) {
        this.program_date_start = program_date_start;
    }

    public String getProgram_date_end() {
        return program_date_end;
    }

    public void setProgram_date_end(String program_date_end) {
        this.program_date_end = program_date_end;
    }

    public String getUser_pic() {
        return user_pic;
    }

    public void setUser_pic(String user_pic) {
        this.user_pic = user_pic;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }
}
