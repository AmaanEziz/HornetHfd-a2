package org.csc133.a2.interfaces;

public interface Subject {
    void attach(Observer p);
    void detach(Observer p);
    void alertObservers();
}
