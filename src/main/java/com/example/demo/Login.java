package com.example.demo;

public class Login {
    private String user_name;

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    private String pass;

    Login(){
        this.user_name = "";
        this.pass = "";
    }

    Login(String user_name, String pass){
        this.user_name = user_name;
        this.pass = pass;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
}
