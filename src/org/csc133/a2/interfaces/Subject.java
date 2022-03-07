package org.csc133.a2.interfaces;

public interface Subject {
    void attach(Observer o);
    void detach(Observer o);
    void notifyObservers();
}
