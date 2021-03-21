package com.project.modernastrology;

import com.google.firebase.auth.FirebaseUser;

public class User {
    private FirebaseUser userinfo;
    private String name;
    private String date;
    private String time;
    private String booking;
    private String appointment;

    public String getPrediction() {
        return prediction;
    }

    public String getAppointment() {
        return appointment;
    }

    public void setAppointment(String appointment) {
        this.appointment = appointment;
    }

    private String prediction;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    private String phone;

    public FirebaseUser getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(FirebaseUser userinfo) {
        this.userinfo = userinfo;
        setUid(userinfo.getUid());
    }

    public String getBooking() {
        return booking;
    }

    public void setBooking(String booking) {
        this.booking = booking;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getB_place() {
        return b_place;
    }

    public void setB_place(String b_place) {
        this.b_place = b_place;
    }

    public String getC_place() {
        return c_place;
    }

    public void setC_place(String c_place) {
        this.c_place = c_place;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public User(FirebaseUser userinfo, String name, String date, String time, String b_place, String c_place, String question, String request, String phone) {
        this.userinfo = userinfo;
        this.name = name;
        this.date = date;
        this.time = time;
        this.b_place = b_place;
        this.c_place = c_place;
        this.question = question;
        this.request = request;
        this.phone = phone;
    }

    private String b_place;
    private String c_place;
    private String question;
    private String request;
    private String uid;

    public User() {
    }

}
