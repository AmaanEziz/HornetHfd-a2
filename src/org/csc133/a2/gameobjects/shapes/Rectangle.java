package org.csc133.a2.gameobjects.shapes;

import com.codename1.ui.Graphics;
import com.codename1.ui.geom.Point;
import org.csc133.a2.gameobjects.GameObject;

public class Rectangle extends GameObject {

    public Rectangle (int color, int w, int h, float translate_x, float translate_y, float scale_x, float scale_y, float Rotate_degree) {

        setColor(color);
        setDimensions(w, h);
        translate(translate_x, translate_y);
        scale(scale_x, scale_y);
        rotate(Rotate_degree);
    }


    @Override
    public void draw (Graphics g, Point containerOrigin)
    {
        ;
    }

    @Override
    public void localDraw(Graphics g, Point containerOrigin,
                                      Point Origin_Screen) {
        g.fillRect(0, 0, getWidth(), getHeight());
    }
}
