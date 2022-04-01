package org.csc133.a2.gameObjects;

public abstract class Movable extends GameObject{

    private int speed;
    private int heading;

    public Movable(double x, double y, int size) {
        super(x, y, size);
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setHeading(int heading)
    {
        this.heading = Math.floorMod(heading, 360);
    }

    public int getSpeed(){
        return speed;
    }

    public int getHeading(){ return heading; }

    public void headLeft(int addHeading){
        setHeading(heading += addHeading);
    }

    public void headRight(int addHeading){
        setHeading(heading += addHeading);
    }

    public void addSpeed(int addSpeed){
        speed += addSpeed;
    }

    public void addLocation(double deltaX, double deltaY) {
        super.updateLocation(deltaX, deltaY);
    }

    public void move(){
        double angle;
        double deltaX;
        double deltaY;

        angle = 90 + heading;
        deltaX = Math.cos(Math.toRadians(angle)) * speed;
        deltaY = Math.sin(Math.toRadians(angle)) * speed;
        addLocation(-deltaX, -deltaY);
    }

}
