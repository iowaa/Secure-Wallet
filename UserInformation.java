package com.example.imas.login_firebase;

/**
 * Created by IMAS on 10/19/2016.
 */


//this class will help us to store data in the firebase

public class UserInformation {

    public String name,mobile,address;

    public UserInformation(String name, String mobile, String address) {
        this.name = name;
        this.mobile = mobile;
        this.address = address;
    }
}
