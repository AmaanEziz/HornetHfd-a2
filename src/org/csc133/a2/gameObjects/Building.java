package org.csc133.a2.gameObjects;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Graphics;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.geom.Point;

import java.util.ArrayList;
import java.util.Random;

public class Building extends Fixed{

    private Dimension dim;

    private Random r;
    private int color;
    private int value;
    private int thisTotalFireArea;
    private int buildingArea;
    private int maxDamage;
    private int height;
    private int width;
    private int largetValue;

    private double damage;

    public Building(int xPosition, int yPosition, int width, int height){
        super(xPosition,yPosition, 0);
        this.height = height;
        this.width = width;
        this.value = 1000;
        setBuildingArea();
        setBuildingSize();
        setColor();
    }

    private void setColor(){
        this.color = ColorUtil.rgb(255,0,0);
    }
    public void setFireInBuilding(Fire fire){
        double x = getRandomX();
        double y = getRandomY();
        fire.setLocation(x,y);
    }

    public void setBuildingDamage(ArrayList<GameObject> gameObjects){
        int temp = 0;
        for(GameObject go : gameObjects){
            if(go instanceof Fire){
                Fire fire = (Fire)go;
                if(fireInBuilding(fire)){
                    temp += fire.getFireArea();
                }
            }
        }
        if(thisTotalFireArea == 0){
            thisTotalFireArea = temp;
        }
        else if (temp > thisTotalFireArea){
            thisTotalFireArea = temp;
        }
        damage =  100 * thisTotalFireArea / buildingArea;
        if(maxDamage == 0){
            maxDamage = (int) damage;
        } else if(damage > maxDamage){
            maxDamage = (int) damage;
        }
    }

    private boolean fireInBuilding(Fire fire){
        boolean b = fire.getX() > super.getX()
                && fire.getX() < (super.getX() + this.width)
                && fire.getY() > super.getY()
                && fire.getY() < (super.getY() + this.height);
        return b;
    }

    private void setBuildingSize(){
        dim = new Dimension(this.width, this.height);
        setDimension(dim);
    }

    private void setBuildingArea(){
        this.buildingArea = this.width * this.height;
    }

    private void setDimension(Dimension dim){
        this.dim = dim;
    }
    private double getRandomX(){
        r = new Random();
        return (super.getX() + ((super.getX() + this.width)
                - super.getX()) * r.nextDouble());
    }
    private double getRandomY(){
        r = new Random();
        return (super.getY() + ((super.getY() + this.height)
                - super.getY()) * r.nextDouble());
    }

    public void updateBuildingValue(ArrayList<GameObject> gameObjects){
        for(GameObject go : gameObjects){
            if(go instanceof Fire){
                Fire fire = (Fire)go;
                if(fireInBuilding(fire))
                {
                    value = (int) (value - (this.damage / 100000));
                    if(largetValue == 0){
                        largetValue = value;
                    } else if (value < largetValue){
                        largetValue = value;
                    }
                }
            }
        }
    }
    public int getBuildingArea(){
        return this.buildingArea;
    }

    public int getBuildingDamage(){
        return (int) damage;
    }

    public int getBuildingValue(){
        return this.largetValue;
    }

    public int getHeight(){
        return this.height;
    }

    public int getWidth(){
        return this.width;
    }

    @Override
    public void draw(Graphics g, Point containerOrigin) {
        g.setColor(this.color);
        g.drawRect(containerOrigin.getX(),
                   containerOrigin.getY(),
                   dim.getWidth(),
                   dim.getHeight());
        g.drawString("V : " + this.getBuildingValue(),
                (containerOrigin.getX() + dim.getWidth() + 20),
                (containerOrigin.getY() + dim.getHeight() - 65));
        g.drawString("D : " + maxDamage + "%",
                (containerOrigin.getX() + dim.getWidth() + 20),
                (containerOrigin.getY() + dim.getHeight() - 30));
    }
}
