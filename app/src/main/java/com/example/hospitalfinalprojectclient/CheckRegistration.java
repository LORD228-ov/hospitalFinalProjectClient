package com.example.hospitalfinalprojectclient;

public class CheckRegistration {
    private String login_db;
    private String password_db;

    public CheckRegistration(String login_db, String password_db) {
        this.login_db = login_db;
        this.password_db = password_db;
    }

    public String getLogin_db() {
        return login_db;
    }

    public void setLogin_db(String login_db) {
        this.login_db = login_db;
    }

    public String getPassword_db() {
        return password_db;
    }

    public void setPassword_db(String password_db) {
        this.password_db = password_db;
    }

    @Override
    public String toString() {
        return "CheckRegistration{" +
                "login_db='" + login_db + '\'' +
                ", password_db='" + password_db + '\'' +
                '}';
    }
}
