package com.example.quanlysinhvien.model;

public class Subject {
    String id;
    String major_id;
    String name_subject;

    public Subject(String id, String major_id, String name_subject) {
        this.id = id;
        this.major_id = major_id;
        this.name_subject = name_subject;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMajor_id() {
        return major_id;
    }

    public void setMajor_id(String major_id) {
        this.major_id = major_id;
    }

    public String getName_subject() {
        return name_subject;
    }

    public void setName_subject(String name_subject) {
        this.name_subject = name_subject;
    }
}
