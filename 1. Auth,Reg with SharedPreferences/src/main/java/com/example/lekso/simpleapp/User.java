package com.example.lekso.simpleapp;

import java.io.Serializable;

public class User  implements Serializable{
    private String Login;
    private String Password;
    private String photoUri;
    private boolean hasSuccessLogin;

    public String getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
    }

    public boolean hasSuccessLogin() {
        return hasSuccessLogin;
    }

    public void setHasSuccessLogin(boolean hasSuccessLogin) {
        this.hasSuccessLogin = hasSuccessLogin;
    }

    public User(String Login, String Password) {
        this.Login = Login;
        this.Password = Password;
    }

    public String getLogin() {
        return Login;
    }

    public void setLogin(String login) {
        Login = login;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
