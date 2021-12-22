package com.example.sportbuddiesapp.ObjectClass;

public class Review {

    private String location;
    private Integer rating;
    private User user;
    public Review(){}

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Review(String location, Integer rating, User user) {
        this.user = user;
        this.location = location;
        this.rating = rating;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }
}
