package com.example.order;

import java.util.HashMap;

public class ModelItem {
    public String name, totalAmount, date, state, phone, address, uid;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public ModelItem(String name, String totalAmount, String date, String state, String phone, String address, String uid, HashMap stars, int starCount) {
        this.name = name;
        this.totalAmount = totalAmount;
        this.date = date;
        this.state = state;
        this.phone = phone;
        this.address = address;
        this.uid = uid;
        this.stars = stars;
        this.starCount = starCount;
    }

    public HashMap stars;
    public int starCount;



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return totalAmount;
    }

    public void setPrice(String price) {
        this.totalAmount = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return state;
    }

    public void setStatus(String status) {
        this.state = status;
    }

    public String getContactNo() {
        return phone;
    }

    public void setContactNo(String contactNo) {
        this.phone = contactNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public ModelItem() {
    }

}
