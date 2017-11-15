package com.coderschool.sosvn.object;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;

/**
 * Created by Admin on 7/26/2017.
 */

public class Country extends RealmObject implements Parcelable  {

    private String id;

    private String name;

    private String phoneCode;

    private int idFlag;

    public Country() {}


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhoneCode() {
        return phoneCode;
    }

    public void setPhoneCode(String phoneCode) {
        this.phoneCode = phoneCode;
    }

    public int getIdFlag() {
        return idFlag;
    }

    public void setIdFlag(int idFlag) {
        this.idFlag = idFlag;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public Country(Parcel in) {
        id = in.readString();
        name = in.readString();
        phoneCode = in.readString();
        idFlag = in.readInt();
    }

    static final Creator<Country> CREATOR
            = new Creator<Country>() {
        @Override
        public Country createFromParcel(Parcel parcel) {
            return new Country(parcel);
        }

        @Override
        public Country[] newArray(int i) {
            return new Country[i];
        }
    };

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeInt(idFlag);
        parcel.writeString(id);
        parcel.writeString(phoneCode);
    }
}
