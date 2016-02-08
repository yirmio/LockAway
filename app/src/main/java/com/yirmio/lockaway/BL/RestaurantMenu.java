package com.yirmio.lockaway.BL;

import java.util.ArrayList;
import java.util.List;

public class RestaurantMenu {
    private List<RestaurantMenuObject> drinks;
    private List<RestaurantMenuObject> cakes;
    private List<RestaurantMenuObject> extras;
    private List<RestaurantMenuObject> itemsOnSale;
    private List<RestaurantMenuObject> specials;
    private List<RestaurantMenuObject> allItems;
    private List<RestaurantMenuObject> sanwiches;
    private List<RestaurantMenuObject> salads;
    private List<RestaurantMenuObject> breakFasts;


    public RestaurantMenu() {
        this.drinks = new ArrayList<RestaurantMenuObject>();
        this.cakes = new ArrayList<RestaurantMenuObject>();
        this.extras = new ArrayList<RestaurantMenuObject>();
        this.itemsOnSale = new ArrayList<RestaurantMenuObject>();
        this.specials = new ArrayList<RestaurantMenuObject>();
        this.allItems = new ArrayList<RestaurantMenuObject>();
        this.sanwiches = new ArrayList<RestaurantMenuObject>();
        this.salads = new ArrayList<RestaurantMenuObject>();
        this.breakFasts = new ArrayList<RestaurantMenuObject>();

    }

    /**
     * Insert item to menu.
     * This method will insert the item to the menu based on it's type.
     * @param mnuItm
     * @return True - if succeeded
     */
public boolean addItemToMenu(RestaurantMenuObject mnuItm){
    if (mnuItm.getType() != null) {
        try {
            switch (mnuItm.getType()) {
                case Drinks:
                    this.drinks.add(mnuItm);
                    break;
                case Cakes:
                    this.cakes.add(mnuItm);
                    break;
                case Extras:
                    this.extras.add(mnuItm);
                    break;
                case Specials:
                    this.specials.add(mnuItm);
                    break;
                case OnSale:
                    this.itemsOnSale.add(mnuItm);
                    break;
                case Sandwiches:
                    this.sanwiches.add(mnuItm);
                    break;
                case Salads:
                    this.salads.add(mnuItm);
                    break;
                case BreakFasts:
                    this.breakFasts.add(mnuItm);
                    break;

                default:
                    this.allItems.add(mnuItm);
                    break;
            }


        }
        catch (Exception e){
            return  false;
        }
    } else {
        this.allItems.add(mnuItm);
    }

    return true;
}
    public List<RestaurantMenuObject> getDrinks() {
        return drinks;
    }

    public List<RestaurantMenuObject> getCakes() {
        return cakes;
    }

    public List<RestaurantMenuObject> getExtras() {
        return extras;
    }

    public List<RestaurantMenuObject> getSpecials() {
        return specials;
    }

    public List<RestaurantMenuObject> getItemsOnSale() {
        return itemsOnSale;
    }
    public int getTotalItemsOnMenu(){
        //TODO handle sections
        //return this.drinks.size() + this.cakes.size() + this.specials.size() + this.extras.size() + this.itemsOnSale.size();
        return this.allItems.size();
    }

    /**
     * get All Items In OneList
     * @return List<MazeMenuItem> allItems
     */
    public List<RestaurantMenuObject> getAllItems(){
// TODO       handle sections
// List<RestaurantMenuObject> allItems = new ArrayList<RestaurantMenuObject>();
//        allItems.addAll(drinks);
//        allItems.addAll(cakes);
//        allItems.addAll(extras);
//        allItems.addAll(specials);
//        allItems.addAll(itemsOnSale);
//        return allItems;
        return this.allItems;
    }

    public RestaurantMenuObject getItemById(String objectId) {
        //TODO try make it better

        RestaurantMenuObject tmpRes = null;
        for (RestaurantMenuObject tmpObj : allItems) {
            if (tmpObj.getId().equals(objectId)) {
                tmpRes = tmpObj;
                return tmpRes;
            }
        }


        return tmpRes;
    }

    public List<RestaurantMenuObject> getMenuByType(MenuItemTypesEnum tmpType) {
        switch (tmpType) {
            case Drinks:
                return this.drinks;
            case Cakes:
                return this.cakes;
            case Extras:
                return this.extras;
            case Specials:
                return this.specials;
            case OnSale:
                return this.itemsOnSale;
            case Sandwiches:
                return this.sanwiches;
            case Salads:
                return this.salads;
            case BreakFasts:
                return this.breakFasts;
            default:
                return null;
        }
    }
}
