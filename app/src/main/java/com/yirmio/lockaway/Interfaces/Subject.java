package com.yirmio.lockaway.Interfaces;

/**
 * Created by oppenhime on 01/02/2016.
 */
public interface Subject {
    public void registerObserver(Observer o);

    public void removeObserver(Observer o);

    public void notifyObservers();

    public void notifyObservers(String msg);
}
