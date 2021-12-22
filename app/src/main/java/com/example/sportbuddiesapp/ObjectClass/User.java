package com.example.sportbuddiesapp.ObjectClass;

public class User {

    private String useremail;
    private String username;
    private String userpassword;
    private String telegramhandle;
    public User(){}

    public String getUseremail() {
        return useremail;
    }

    public void setUseremail(String useremail) {
        this.useremail = useremail;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserpassword() {
        return userpassword;
    }

    public void setUserpassword(String userpassword) {
        this.userpassword = userpassword;
    }

    public String getTelegramhandle() {
        return telegramhandle;
    }

    public void setTelegramhandle(String telegramhandle) {
        this.telegramhandle = telegramhandle;
    }

    public User(String useremail, String username, String userpassword, String telegramhandle) {
        this.useremail = useremail;
        this.username = username;
        this.userpassword = userpassword;
        this.telegramhandle = telegramhandle;
    }
}
