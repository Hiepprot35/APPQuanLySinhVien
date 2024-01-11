package com.example.quanlysinhvien.model;

public class Student {

    private String id;
    private String fullname;
    private String email;
    private String country;
    private String gender;
    private String phone_number;
    private String date_of_birth;
    private String class_id;
    private String major_id;

    public Student(String id, String fullname, String email, String country, String gender, String phone_number,
                   String date_of_birth, String class_id, String major_id, String subject_id, byte[] avatar) {
        this.id = id;
        this.fullname = fullname;
        this.email = email;
        this.country = country;
        this.gender = gender;
        this.phone_number = phone_number;
        this.date_of_birth = date_of_birth;
        this.class_id = class_id;
        this.major_id = major_id;
        this.subject_id = subject_id;
        this.avatar = avatar;
    }

    public String getClass_id() {
        return class_id;
    }

    public void setClass_id(String class_id) {
        this.class_id = class_id;
    }

    public String getMajor_id() {
        return major_id;
    }

    public void setMajor_id(String major_id) {
        this.major_id = major_id;
    }

    public String getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(String subject_id) {
        this.subject_id = subject_id;
    }

    private String subject_id;
    private byte[] avatar;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }




    public byte[] getAvatar() {
        return avatar;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }

    public Student()
    {

    }
    public Student(String fullname, byte[] avatar)
    {
        this.avatar=avatar;
        this.fullname=fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getFullname() {
        return fullname;
    }

    public String getEmail() {
        return email;
    }

    public String getCountry() {
        return country;
    }

    public String getPhone_number() {
        return phone_number;
    }



}
