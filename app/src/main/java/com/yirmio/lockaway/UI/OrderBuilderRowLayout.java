package com.yirmio.lockaway.UI;

import com.parse.ParseFile;
import com.yirmio.lockaway.BL.RestaurantMenuObject;

/**
 * Created by yirmio on 27/07/2015.
 */
public class OrderBuilderRowLayout {

    //region Properties
    private ParseFile photoParseFile;
    private String lable;
    private Float price;
    private int timeToMake;
    private String id;

    public OrderBuilderRowLayout(RestaurantMenuObject obj) {
        this.id = obj.getId();
        this.lable = obj.getTitle();
        this.price = obj.getPrice();
        this.timeToMake = obj.getTimeToMake();
        this.photoParseFile = obj.getPic();
    }


//endregion



    //region Properties
    public ParseFile getPhotoParseFile() {
        return photoParseFile;
    }

    public void setPhotoParseFile(ParseFile photoParseFile) {
        this.photoParseFile = photoParseFile;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    //endregion
}
