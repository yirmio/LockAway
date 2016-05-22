package com.yirmio.lockaway.BL;

/**
 * Created by yirmio on 3/24/2015.
 */
public enum OrderStatusEnum {
    Active,
    OnHold,
    OnDelay,
    OnMake,
    Ready,
    WaitingToStart,
    Finish,
    Canceled;


    public static String[] getAllValues(){
        String[] res = new String[OrderStatusEnum.values().length];
        OrderStatusEnum[] r = OrderStatusEnum.values();
        for (int i = 0; i < OrderStatusEnum.values().length; i++) {
            res[i] = r[i].name();
        }
        return res;
    }

    public static OrderStatusEnum fromInt(int i) {
        return values()[i];

    }
}