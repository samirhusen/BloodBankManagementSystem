package com.example.bloodbankmanagementsystem.HelperClass;

import com.google.firebase.database.Exclude;

public class UploadHelperClass {

    private String message;
    private String imageUri;
    private String number;

    private String mKey;

    public UploadHelperClass() {
        //firebase needs this empty constructor always
    }

    public UploadHelperClass(String message, String imageUri) {
        this.message = message;
        this.imageUri = imageUri;
    }

    public UploadHelperClass(String number) {
        this.number = number;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Exclude
    public String getKey() {
        return mKey;
    }

    @Exclude
    public void setKey(String Key) {
        mKey = Key;
    }
}
