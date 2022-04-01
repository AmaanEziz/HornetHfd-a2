package org.csc133.a2.gameObjects;
import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Font;
import com.codename1.ui.Graphics;
import com.codename1.ui.geom.Point;

import org.csc133.a2.GameWorld;
import org.csc133.a2.inerfaces.Steerable;

import java.util.ArrayList;

import static com.codename1.ui.CN.*;


public class Helicopter extends Movable implements Steerable {

    private final static int MAX_SPEED = 10;
    private final static int MAX_WATER = 1000;
    private final static int SIZE = 30;

    private int color;

    public int fuel;
    public int water;

    public Helicopter(int locationX, int locationY) {
        super(locationX, locationY, SIZE);
        super.setSpeed(0);
        super.setHeading(0);
        fuel = 30000;
        water = 0;
        setColor();
    }

    private void setColor(){ this.color = ColorUtil.rgb(249, 249, 0); }

    @Override
    public void draw(Graphics g, Point containerOrigin){

        int radius = 55;
        int centerX = (int)super.getX() + SIZE / 2;
        int centerY = (int)super.getY() + SIZE / 2;
        double angle = Math.toRadians(-this.getHeading() - 90);
        double eX = centerX + radius * Math.cos(angle);
        double eY = centerY + radius * Math.sin(angle);

        g.setColor(this.color);
        g.fillRoundRect(containerOrigin.getX(),
                        containerOrigin.getY(),
                        SIZE,
                        SIZE,
                        SIZE,
                        SIZE);
        g.drawLine(centerX,  centerY, (int) eX, (int) eY);
        g.drawString("F  : " + fuel,
                     containerOrigin.getX(),
                  containerOrigin.getY() + 55);
        g.drawString("W : " + water,
                     containerOrigin.getX(),
                  containerOrigin.getY() + 80);
        g.setFont(Font.createSystemFont(FACE_SYSTEM, STYLE_PLAIN, SIZE_MEDIUM));
    }

    public void move() {
        super.move();
    }

    public void updateLocation(double deltaX, double deltaY) {
        super.addLocation(deltaX,deltaY);
    }

    public void addSpeed(int addSpeed) {
        if(addSpeed == -1)
        {
            System.out.println("Decreasing Speed: "+this.getSpeed());
            if (this.getSpeed() >= 1 ) {
                super.addSpeed(addSpeed);
            }
        }
        else if(addSpeed == 1)
        {
            if (this.getSpeed() < MAX_SPEED ) {
                super.addSpeed(addSpeed);
            }
        }
    }

    public void headLeft(int addHeading) {
        super.headLeft(addHeading);
    }

    public void headRight(int addHeading) {
        super.headRight(addHeading);
    }

    public void drink(River river) {
        if (river.canDrink(this)) {
            willDrink();
        }
    }

    public void willDrink() {
        if ((this.getSpeed() <= 0
                && this.getSpeed() > -2)
                && (water < MAX_WATER)) {
                    water += 50;
                    water += 50;
        }
    }

    public void fightFire(ArrayList<Fire> fires) {
        for (Fire fire : fires) {
            if ((this.getX() > fire.getX()
                    && this.getX() < fire.getX() + fire.size)
                    && (this.getY() > fire.getY()
                    && this.getY() < fire.getY() + fire.size)){
                if (water > 0)
                {
                    int totalWater = water;
                    water -= totalWater;
                    if(fire.size>= totalWater)
                        fire.size -= totalWater;
                    else
                        fire.size = 0 ;
                }
            }
        }
    }

    public void consumeFuel() {
        double tempFuel;
        tempFuel = Math.pow(this.getSpeed(), 2) + 5;
        fuel -= tempFuel;
    }

    public void receiveFuel(Helipad helipad) {
        if (fuel < GameWorld.FUEL) {
            if (this.getX() > helipad.getX()
                    && this.getX() < helipad.getX() + helipad.getSize()
                    && this.getY() > helipad.getY()
                    && this.getY() < helipad.getY() + helipad.getSize()) {
                    fuel += 100;
            }
        }
    }

    @Override
    public void steer(int steer) {
        if(steer < 0){
            this.headRight(steer);
        } else {
            this.headLeft(steer);
        }
    }
}