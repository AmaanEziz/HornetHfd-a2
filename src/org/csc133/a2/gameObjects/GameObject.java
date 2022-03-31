package org.csc133.a2.gameObjects;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.geom.Point;
import com.codename1.ui.geom.Point2D;
import org.csc133.a2.inerfaces.Drawable;


public abstract class GameObject implements Drawable{

    public Point2D location;
    public Dimension dimension;
    public Dimension mapSize;
    private double x;
    private double y;
    private int size;


    public GameObject(double x, double y, int size){
        this.x = x;
        this.y = y;
        this.setSize(size);
        this.location = new Point2D(x,y);
    }

    private void setX(double x){
        this.x = x;
    }

    private void setY(double y){
        this.y = y;
    }

    private void setSize(int size){
        this.size = size;
    }

    public void setLocation(double setX, double setY){
        this.location.setX(setX);
        this.location.setY(setY);
        this.setX(setX);
        this.setY(setY);
    }

    public double getX(){
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public int getSize(){
        return this.size;
    }

    public Point getLocation(){
        return new Point ((int) location.getX(), (int) location.getY());
    }

    public void updateLocation(double deltaX, double deltaY){
        setLocation(this.getX() + deltaX, this.getY() + deltaY);

    }

}
