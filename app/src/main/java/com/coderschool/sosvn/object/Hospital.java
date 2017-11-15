package com.coderschool.sosvn.object;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Admin on 7/26/2017.
 */

public class Hospital implements Parcelable {

    private String address;
    private String email;
    private float latitude;
    private float longitude;
    private String name;
    private String phone;
    private float rating;
    private String url;

    public Hospital() {
    }

    public Hospital(String address, String email, float latitude, float longitude, String name, String phone, float rating, String url) {
        this.address = address;
        this.email = email;
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.phone = phone;
        this.rating = rating;
        this.url = url;
    }

    protected Hospital(Parcel in) {
        address = in.readString();
        email = in.readString();
        latitude = in.readFloat();
        longitude = in.readFloat();
        name = in.readString();
        phone = in.readString();
        rating = in.readFloat();
        url = in.readString();
    }

    public static final Creator<Hospital> CREATOR = new Creator<Hospital>() {
        @Override
        public Hospital createFromParcel(Parcel in) {
            return new Hospital(in);
        }

        @Override
        public Hospital[] newArray(int size) {
            return new Hospital[size];
        }
    };

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(address);
        parcel.writeString(email);
        parcel.writeFloat(latitude);
        parcel.writeFloat(longitude);
        parcel.writeString(name);
        parcel.writeString(phone);
        parcel.writeFloat(rating);
        parcel.writeString(url);
    }
}
