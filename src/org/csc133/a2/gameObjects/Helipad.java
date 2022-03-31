package org.csc133.a2.gameObjects;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Graphics;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.geom.Point;


public class Helipad extends Fixed {

    private int color;
    private int rectSize;
    private Point center;
    private Dimension mapSize;

    public Helipad(Dimension mapSize) {
        super((double) mapSize.getWidth() / 2,
                (double) mapSize.getHeight() - 100,
                150);
        center = new Point(super.getSize() / 2, super.getSize() /2);
        this.mapSize = mapSize;
        setColor();
    }

    private void setColor(){
        this.color = ColorUtil.rgb(153,153,153);
    }

    public int getRectSize(){
        return this.rectSize;
    }

    @Override
    public void draw(Graphics g, Point containerOrigin) {
        g.setColor(this.color);
        g.drawRect(containerOrigin.getX(),
                containerOrigin.getY(),
                this.getSize(),
                this.getSize(),
                5);
        g.drawRoundRect(containerOrigin.getX() + 10,
                containerOrigin.getY() + 10,
                this.getSize() - 25,
                this.getSize() - 25,
                this.getSize() - 25,
                this.getSize() - 25);
    }

}
