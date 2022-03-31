package org.csc133.a2.gameObjects;



public abstract class Fixed extends GameObject {

    public Fixed(double x, double y, int size) {
        super(x, y, size);
    }

    @Override
    public void updateLocation(double deltaX, double deltaY){}



}
