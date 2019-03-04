package com.erez8.chatpic;

public class Users {

    private String email;
    private String userImage;
    private String uid;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String password;

    public String getUid() {

        return uid;
    }

    public Users(String userName, String userImg, String userID) {
        this.email = userName;
        this.userImage = userImg;
        this.uid = userID;
    }

    public Users() {
    }

    public String getEmail() {
        return email;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
