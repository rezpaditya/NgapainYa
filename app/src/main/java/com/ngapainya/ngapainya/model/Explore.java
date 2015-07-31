package com.ngapainya.ngapainya.model;

import java.io.Serializable;

/**
 * Created by Ari Anggraeni on 7/7/2015.
 */
public class Explore implements Serializable {
    private String program_id;
    private String program_name;
    private String user_id;
    private String program_desc;
    private String program_date_start;
    private String program_date_end;
    private String user_pic;

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
}
