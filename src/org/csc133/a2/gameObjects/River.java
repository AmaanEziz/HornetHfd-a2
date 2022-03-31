package org.csc133.a2.gameObjects;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Graphics;
import com.codename1.ui.geom.Point;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.geom.Point2D;


public class River extends Fixed {

    private int color;

    public River(Dimension mapSize){
        super(  0,
                mapSize.getHeight() / 3,
                125);
        this.mapSize = mapSize;
        this.location = new Point2D(0,mapSize.getHeight());
        this.dimension = new Dimension(mapSize.getWidth(), mapSize.getHeight());
        setColor();
    }

    private void setColor(){
        this.color = ColorUtil.rgb(0,0,255);
    }

    public boolean canDrink(Helicopter helicopter) {
        return (helicopter.getX() > this.getX()
                && helicopter.getX() < mapSize.getWidth()
                && helicopter.getY() > this.getY()
                && helicopter.getY() < this.getY() + this.getSize());
    }

    @Override
    public void draw(Graphics g, Point containerOrigin) {

        int x = containerOrigin.getX() + (int)location.getX();
        int y = mapSize.getHeight() / 3;
        int h = dimension.getHeight() / 8;
        int w = dimension.getWidth();
        g.setColor(this.color);
        g.drawRect(x,y,w,h);;
    }

}
