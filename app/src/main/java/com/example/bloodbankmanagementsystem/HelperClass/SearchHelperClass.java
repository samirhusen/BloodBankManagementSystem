package com.example.bloodbankmanagementsystem.HelperClass;

public class SearchHelperClass {

    public String name, city, blood_group, number ;

    public SearchHelperClass(){
        //complusory for firebase
    }

    public SearchHelperClass(String name, String city, String blood_group, String number) {
        this.name = name;
        this.city = city;
        this.blood_group = blood_group;
        this.number = number;
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

    public String getBlood_group() {
        return blood_group;
    }

    public void setBlood_group(String blood_group) {
        this.blood_group = blood_group;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
