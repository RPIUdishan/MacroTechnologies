package com.example.macro.Model;

public class Customer {
    String userName, email, password, image, address;

    public Customer() {

    }

    public Customer(String userName, String email, String password, String image, String address) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.image = image;
        this.address = address;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
