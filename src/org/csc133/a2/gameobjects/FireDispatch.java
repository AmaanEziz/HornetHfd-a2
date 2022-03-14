package org.csc133.a2.gameobjects;

import com.codename1.ui.Transform;
import org.csc133.a2.GameWorld;
import org.csc133.a2.interfaces.Observer;
import org.csc133.a2.interfaces.Subject;

import java.util.ArrayList;

public class FireDispatch implements Subject {
    private final ArrayList<Observer> observers;
    private Fire selected;

    public FireDispatch() {
        observers = new ArrayList<>();
    }

    @Override
    public void attach(Observer p) {
        observers.add(p);
    }

    @Override
    public void detach(Observer p) {
        observers.remove(p);
    }

    void setSelectedFire(Fire selected) {
        this.selected = selected;
        alertObservers();
        FireUpdate_SEL();
    }

    @Override
    public void alertObservers() {
        for(Observer p : observers) {
            p.update(selected);
        }
    }

    private void FireUpdate_SEL() {
        Transform fire = Transform.makeIdentity();
        fire.translate(selected.getX(), selected.getY());
        GameWorld.getInstance().FireUpdate_SEL(fire);
    }
}
