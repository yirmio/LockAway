package com.yirmio.lockaway.BL;

import com.parse.ParseFile;
import com.yirmio.lockaway.UI.ListsItems.MenuListRowLayoutItem;
import com.yirmio.lockaway.UI.ListsItems.OrderBuilderRowLayout;

/**
 * Created by yirmio on 1/9/2015.
 */
public class RestaurantMenuObject implements Comparable<RestaurantMenuObject> {
    //region Properties
    private float price;
    private String title;
    private int timeToMake;
    private ParseFile pic;
    private MenuItemTypesEnum type;
    private boolean isReady;
    private String description;
    private boolean isVeg;
    private boolean isGlootenFree;
    private String id;


//endregion

    //region Ctor
    public RestaurantMenuObject() {
    }
    //Convert MenuListRowLayoutItem to RestaurantMenuObject
    public RestaurantMenuObject(MenuListRowLayoutItem item){
        this.price = item.getPrice();
        this.title = item.getLable();
        this.timeToMake = item.getTimeToMake();
        this.pic = item.getPhotoParseFile();
        //TODO handle type
        //this.type = item.get;
        this.isReady = false;
        this.description = item.getInfo();
        this.isVeg = item.isVeg();
        this.isGlootenFree = item.isGlotenFree();
        this.id = item.getId();
    }
    public RestaurantMenuObject(OrderBuilderRowLayout item){
        this.price = item.getPrice();
        this.title = item.getLable();
        this.timeToMake = item.getTimeToMake();
        this.pic = item.getPhotoParseFile();
        //TODO handle type
        //this.type = item.get;
        this.isReady = false;
        //this.description = item.getInfo();
//        this.isVeg = item.isVeg();
//        this.isGlootenFree = item.isGlotenFree();
        this.id = item.getId();
    }

    public RestaurantMenuObject(String itemID, String desc, float price, String title, int timeToMake, ParseFile pic, MenuItemTypesEnum type, boolean isVeg, boolean isGlootenFree) {

        this.price = price;
        this.title = title;
        this.timeToMake = timeToMake;
        this.pic = pic;
        this.type = type;
        this.isReady = false;
        this.description = desc;
        this.isVeg = isVeg;
        this.isGlootenFree = isGlootenFree;
        this.id = itemID;

    }
    //endregion

    //region Getters & Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public boolean isVeg() {
        return isVeg;
    }

    public void setIsVeg(boolean isVeg) {
        this.isVeg = isVeg;
    }

    public boolean isGlootenFree() {
        return isGlootenFree;
    }

    public void setIsGlootenFree(boolean isGlootenFree) {
        this.isGlootenFree = isGlootenFree;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIsReady(boolean isReady) {
        this.isReady = isReady;
    }
    public MenuItemTypesEnum getType() {
        return type;
    }
    public float getPrice() {
        return price;
    }
    public String getTitle() {
        return title;
    }
    public int getTimeToMake() {
        return timeToMake;
    }
    public ParseFile getPic() {
        return pic;
    }
    public void setPrice(float price) {
        this.price = price;
    }

    public boolean isReady() {
        return isReady;
    }

    public void setReady(boolean isReady) {
        this.isReady = isReady;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTimeToMake(int timeToMake) {
        this.timeToMake = timeToMake;
    }

    public void setPic(ParseFile pic) {
        this.pic = pic;
    }

    public void setType(MenuItemTypesEnum type) {
        this.type = type;
    }
    public void setType(String type){this.type = MenuItemTypesEnum.valueOf(type);}
    //endregion

    //region Overrides
    @Override
    //Will Compare MenuItems by TimeToMake
    public int compareTo(RestaurantMenuObject another) {
        if (this.timeToMake < another.timeToMake) {
            return -1;
        } else return 1;
    }
    //endregion
}
