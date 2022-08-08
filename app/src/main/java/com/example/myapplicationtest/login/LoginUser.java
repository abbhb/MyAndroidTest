package com.example.myapplicationtest.login;

public class LoginUser {
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public LoginUser(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
