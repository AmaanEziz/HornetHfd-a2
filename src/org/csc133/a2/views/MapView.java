package org.csc133.a2.views;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Container;
import com.codename1.ui.Graphics;
import com.codename1.ui.Transform;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.geom.Point;
import com.codename1.ui.layouts.BorderLayout;
import org.csc133.a2.GameWorld;
import org.csc133.a2.gameobjects.*;
import org.csc133.a2.gameobjects.Fire;
import org.csc133.a2.gameobjects.GameObject;

public class MapView extends Container {
    private final GameWorld gw;
    private final float[] coordinates_left   = {0, 0, 0};
    private final float[] coordinates_right  = {0, 0, 0};
    private final float[] coordinates_top    = {0, 0, 0};
    private final float[] coordinates_bottom = {0, 0, 0};
    private int mapIndex  = 0;

    public MapView() {
        gw = GameWorld.getInstance();
        setLayout(new BorderLayout());
        this.getAllStyles().setBgTransparency(255);
        this.getAllStyles().setBgColor(ColorUtil.BLACK);
    }

    private void InitMap() {
        coordinates_left[0]  = coordinates_bottom[0] = 0;
        coordinates_right[0] = this.getWidth();
        coordinates_top[0]   = this.getHeight();
    }



    private Transform World_To_normalized_device(float windows_width, float windows_height,
                                          float coordinates_left, float coordinates_bottom) {
        Transform temporary_form = Transform.makeIdentity();
        temporary_form.scale(1 / windows_width, 1 / windows_height);
        temporary_form.translate(-coordinates_left, -coordinates_bottom);
        return temporary_form;
    }

    private Transform Normalized_device_To_Screen(float displayWidth,
                                            float displayHeight) {
        Transform temporary_form = Transform.makeIdentity();
        temporary_form.translate(0, displayHeight);
        temporary_form.scale(displayWidth, -displayHeight);
        return temporary_form;
    }

    private void Make_Viewing_Transformation_Matrix(Graphics g) {
        Transform worldToND, ndToDisplay, theVTM;

        float winH = coordinates_top[mapIndex] - coordinates_bottom[mapIndex];
        float winW = coordinates_right[mapIndex] - coordinates_left[mapIndex];

        worldToND = World_To_normalized_device(winW, winH,
                                        coordinates_left[mapIndex],
                                        coordinates_bottom[mapIndex]);
        ndToDisplay = Normalized_device_To_Screen(getWidth(), getHeight());

        theVTM = ndToDisplay.copy();
        theVTM.concatenate(worldToND);

        Transform gXform = Transform.makeIdentity();
        g.getTransform(gXform);
        gXform.translate(getAbsoluteX(), getAbsoluteY());
        gXform.concatenate(theVTM);
        gXform.translate(-getAbsoluteX(), -getAbsoluteY());
        g.setTransform(gXform);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        Make_Viewing_Transformation_Matrix(g);

        Point containerOrigin = new Point(this.getX(), this.getY());
        Point Origin_Screen = new Point(getAbsoluteX(), getAbsoluteY());

        for(GameObject go : gw.getGameObjectCollection()) {
            go.draw(g, containerOrigin, Origin_Screen);
        }

        g.resetAffine();
    }

    @Override
    public void pointerPressed(int x, int y) {

        x += this.getX();
        y  = this.getY() + this.getHeight() + gw.getControlClusterHeight() - y;

        for(Fire fire : gw.getGameObjectCollection().getFires()) {
            fire.checkIfSelected(x, y);
        }
    }

    @Override
    public void laidOut() {
        super.laidOut();
        gw.layGameMap(new Dimension(this.getWidth(), this.getHeight()));
        InitMap();
    }
}