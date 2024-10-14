package com.example.indegenousmedicine2;

public class User {
    private String name;
    private String age;
    private String areaSince;
    private String aadhar;
    private String phoneNumber;
    private String address;
    private String pan;
    private String user;

    // Required default constructor for Firebase
    public User() {
    }

    public User(String user, String name, String age, String areaSince, String aadhar, String phoneNumber, String address, String pan) {
        this.user = user;
        this.name = name;
        this.age = age;
        this.areaSince = areaSince;
        this.aadhar = aadhar;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.pan = pan;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getAreaSince() {
        return areaSince;
    }

    public void setAreaSince(String areaSince) {
        this.areaSince = areaSince;
    }

    public String getAadhar() {
        return aadhar;
    }

    public void setAadhar(String aadhar) {
        this.aadhar = aadhar;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }
}
