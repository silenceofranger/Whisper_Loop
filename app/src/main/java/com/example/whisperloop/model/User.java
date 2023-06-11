package com.example.whisperloop.model;

public class User {
    private String profilePic;
    private String emailId;
    private String userName;
    private String password;
    private String userId;
    private String lastMessage;
    private String status;

    public User() {
    }

    public User(String userId, String userName, String email, String password, String profilePic, String status) {
        this.userId = userId;
        this.userName = userName;
        this.emailId = email;
        this.password = password;
        this.profilePic = profilePic;
        this.status = status;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}