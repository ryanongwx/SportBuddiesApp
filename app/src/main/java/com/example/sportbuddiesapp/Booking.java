package com.example.sportbuddiesapp;

import android.widget.RatingBar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Booking {

    private User user;
    private String timeslot;
    private String date;
    private String location;
    private Integer playernumber;
    private String bookingstatus;
    public Booking(){}

    public Booking(User user, String timeslot, String date, String location, Integer playernumber, String bookingstatus) {
        this.user = user;
        this.timeslot = timeslot;
        this.date = date;
        this.location = location;
        this.playernumber = playernumber;
        this.bookingstatus = bookingstatus;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTimeslot() {
        return timeslot;
    }

    public void setTimeslot(String timeslot) {
        this.timeslot = timeslot;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getPlayernumber() {
        return playernumber;
    }

    public void setPlayernumber(Integer playernumber) {
        this.playernumber = playernumber;
    }

    public String getBookingstatus() {
        return bookingstatus;
    }

    public void setBookingstatus(String bookingstatus) {
        this.bookingstatus = bookingstatus;
    }

}
