package org.csc133.a2.gameobjects.shapes;

import com.codename1.ui.Graphics;
import com.codename1.ui.geom.Point;
import org.csc133.a2.gameobjects.GameObject;

public class Arc extends GameObject {
    private final int Origin_Angle;
    private final int arcAngle;

    public Arc(int color, int w, int h,float translate_x, float translate_y,float scale_x, float scale_y,float Rotate_degree, int Origin_Angle, int arcAngle) {

        setColor(color);
        setDimensions(w, h);
        this.Origin_Angle = Origin_Angle;
        this.arcAngle = arcAngle;

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
    public void localDraw(Graphics g, Point containerOrigin, Point Origin_Screen)
    {
        g.fillArc(0, 0, getWidth(), getHeight(), Origin_Angle, arcAngle);
    }
}
