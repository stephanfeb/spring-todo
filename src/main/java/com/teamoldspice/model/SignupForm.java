package com.teamoldspice.model;

import java.io.Serializable;

public class SignupForm implements Serializable{

    private String email;
    private String password;
    private String password_2;

    public void SignupForm(){}


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword_2() {
        return password_2;
    }

    public void setPassword_2(String password_2) {
        this.password_2 = password_2;
    }


}
