package com.example.bloodbankmanagementsystem.HelperClass;

public class UserHelperClass {

    String name, city, email, password, number, blood_group;

    public UserHelperClass() {
    }

    public UserHelperClass(String name, String city, String email, String password, String number, String blood_group) {
        this.name = name;
        this.city = city;
        this.email = email;
        this.password = password;
        this.number = number;
        this.blood_group = blood_group;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getBlood_group() {
        return blood_group;
    }

    public void setBlood_group(String blood_group) {
        this.blood_group = blood_group;
    }
}
