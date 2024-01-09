package com.example.quanlysinhvien.model;

public class Major {
    String id;
    String major_name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMajor_name() {
        return major_name;
    }

    public void setMajor_name(String major_name) {
        this.major_name = major_name;
    }

    public Major(String id, String major_name) {
        this.id = id;
        this.major_name = major_name;
    }

}
