package com.yirmio.lockaway.UI.ListsItems;

import android.widget.ImageView;

import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.yirmio.lockaway.BL.RestaurantMenuObject;

/**
 * Created by yirmio on 23/07/2015.
 */
public class MenuListRowLayoutItem {

    //region Properties
    private ParseFile photoParseFile;
    private String info;
    private String lable;
    private Float price;
    private int timeToMake;
    private boolean isVeg;
    private boolean isGlotenFree;
    private String id;


//endregion


    //region CTor
    public MenuListRowLayoutItem(String itemID, ParseFile photoParseFileToUse, String info, boolean isGlotenFree, boolean isVeg, String lable, Float price, int timeToMake) {
        this.photoParseFile = photoParseFileToUse;
        this.info = info;
        this.isGlotenFree = isGlotenFree;
        this.isVeg = isVeg;
        this.lable = lable;
        this.price = price;
        this.timeToMake = timeToMake;
        this.id = itemID;
    }

    public MenuListRowLayoutItem(RestaurantMenuObject item) {
        this.photoParseFile = item.getPic();
        this.info = item.getDescription();
        this.isGlotenFree = item.isVeg();
        this.isVeg = item.isGlootenFree();
        this.lable = item.getTitle();
        this.price = item.getPrice();
        this.timeToMake = item.getTimeToMake();
        this.id = item.getId();
    }
    //endregion

    //region Getters&Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public ParseFile getPhotoParseFile() {
        return photoParseFile;
    }

    public void setPhotoParseFile(ParseFile photoParseFile) {
        this.photoParseFile = photoParseFile;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public boolean isGlotenFree() {
        return isGlotenFree;
    }

    public void setIsGlotenFree(boolean isGlotenFree) {
        this.isGlotenFree = isGlotenFree;
    }

    public boolean isVeg() {
        return isVeg;
    }

    public void setIsVeg(boolean isVeg) {
        this.isVeg = isVeg;
    }

    public String getLable() {
        return lable;
    }

    public void setLable(String lable) {
        this.lable = lable;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public int getTimeToMake() {
        return timeToMake;
    }

    public void setTimeToMake(int timeToMake) {
        this.timeToMake = timeToMake;
    }
    //endregion


}
