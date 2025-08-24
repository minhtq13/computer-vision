package com.example.multiplechoiceexam.Models;

public class User {
    private int id;
    private String username;
    private String fullName;
    private String email;
    private String birthday;
    private String phoneNumber;
    private String code;
    private String gender;
    public User() {
    }

    public User(int id, String username, String fullName, String email, String birthday, String phoneNumber, String code, String gender) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.email = email;
        this.birthday = birthday;
        this.phoneNumber = phoneNumber;
        this.code = code;
        this.gender = gender;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
