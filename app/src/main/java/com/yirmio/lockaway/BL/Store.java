package com.yirmio.lockaway.BL;

import android.graphics.Bitmap;
import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Yirmi on 1/5/2015.
 */
public class Store implements Parcelable {
    private String name;
    private String location;
    private Location loc;
    private Bitmap logo;
    private String telephone;
    private String email;
    private String openDays;//In 1234567 format when 1=Sunday, 2 = monday...
    //TODO - handle location
  /* private ParseGeoPoint parseLocation; */
    private String displayName;//In Hebrew
    private String webStirUrl;
    private String address;
    private String phoneNumber;
    private boolean isStoreOpen;
    private boolean isOpen;

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOpenDays() {
        return openDays;
    }

    public void setOpenDays(String openDays) {
        this.openDays = openDays;
    }
//TODO - handle open days
    /*public ParseGeoPoint getParseLocation() {
        return parseLocation;
    }

    public void setParseLocation(ParseGeoPoint parseLocation) {
        this.parseLocation = parseLocation;
    }*/

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getWebStirUrl() {
        return webStirUrl;
    }

    public void setWebStirUrl(String webStirUrl) {
        this.webStirUrl = webStirUrl;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Bitmap getLogo() {
        return logo;
    }

    public void setLogo(Bitmap logo) {
        this.logo = logo;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.location);
        dest.writeParcelable(this.loc, 0);
        dest.writeParcelable(this.logo, 0);
        dest.writeString(this.telephone);
        dest.writeString(this.email);
        dest.writeString(this.openDays);
        //TODO - handle location
        //dest.writeParcelable(this.parseLocation, flags);
        dest.writeString(this.displayName);
        dest.writeString(this.webStirUrl);
        dest.writeString(this.address);
    }

    public Store() {
    }

    private Store(Parcel in) {
        this.name = in.readString();
        this.location = in.readString();
        this.loc = in.readParcelable(Location.class.getClassLoader());
        this.logo = in.readParcelable(Bitmap.class.getClassLoader());
        this.telephone = in.readString();
        this.email = in.readString();
        this.openDays = in.readString();
        //TODO - handle location
        //  this.parseLocation = in.readParcelable(ParseGeoPoint.class.getClassLoader());
        this.displayName = in.readString();
        this.webStirUrl = in.readString();
        this.address = in.readString();
    }

    public static final Parcelable.Creator<Store> CREATOR = new Parcelable.Creator<Store>() {
        public Store createFromParcel(Parcel source) {
            return new Store(source);
        }

        public Store[] newArray(int size) {
            return new Store[size];
        }
    };

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setIsOpen(boolean isOpenNew) {

        this.isOpen = isOpenNew;
    }

    public boolean getIsStoreOpen() {
        return isOpen;
    }
}
