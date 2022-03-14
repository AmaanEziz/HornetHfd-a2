package org.csc133.a2.gameobjects;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Font;
import com.codename1.ui.Graphics;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.geom.Point;
import com.codename1.ui.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Random;
import static com.codename1.ui.CN.*;

public class Building extends Fixed {
    private final ArrayList<Fire> fires;
    private double damagePercentage;
    private int value;
    private double fireAreas;
    private final Random rand;
    private final double[] x_change = {0.30, 0.10, 0.80};
    private final double[] y_change = {0.80, 0.10, 0.10};
    private final double[] WIDTHS  = {0.40, 0.10, 0.10};
    private final double[] HEIGHTS = {0.15, 0.40, 0.40};

    public Building(Dimension mapSize, int map) {
        super(ColorUtil.rgb(255, 0, 0), mapSize, 0, 0);
        fireAreas = 0;
        damagePercentage = 0;
        rand = new Random();
        fires = new ArrayList<>();
        setFont(Font.createSystemFont(FACE_SYSTEM, STYLE_BOLD, SIZE_MEDIUM));

        build(map);
    }

    private void build(int map) {
        setDimensions((int) (WIDTHS[map] * getMapSize().getWidth()),
                      (int) (HEIGHTS[map] * getMapSize().getHeight()));

        double trans_X = getMapSize().getWidth() * x_change[map]
                            + getWidth() / 2f;
        double trans_Y = getMapSize().getHeight() * y_change[map]
                            + getHeight() / 2f;

        this.translate(trans_X, trans_Y);
        this.scale(1, -1);

        value = (map + 1) * 400;
    }


    private double getArea() {
        return getWidth() * getHeight();
    }

    public void setFireInBuilding(Fire fire) {
        double x = getX() - getWidth() / 2f;
        double y = getY() - getHeight() / 2f;
        double randomX = rand.nextInt((getWidth()));
        double randomY = rand.nextInt((getHeight()));

        fire.spawn(x + randomX, y + randomY);
        fire.start();
        fires.add(fire);
    }

    public void setDamagePercentage() {
        double damagePercentage = (int) ((fireAreas / this.getArea()) * 100);

        if(damagePercentage > 100) {
            this.damagePercentage = 100;
        }
        else if(this.damagePercentage < damagePercentage) {
            this.damagePercentage = damagePercentage;
        }
        fireAreas = 0;
    }

    public double getDamagePercentage() {
        return damagePercentage;
    }

    public double getLoss() {
        return value * damagePercentage / 100f;
    }

    public boolean checkfirestatus() {
        for(Fire fire : fires) {
            if(fire.getSize() > 0) {
                return false;
            }
        }
        return true;
    }

    public void gatherfireareas() {
        if(fires.isEmpty()) {
            fireAreas = 0;
            return;
        }
        int[][] index = new int[fires.size()][fires.size()];
        int q = 0, t = 0;

        for(Fire fireA : fires) {
            fireAreas += fireA.getArea();
            for(Fire fireB : fires) {
                if(fireA == fireB || index[q][t] == 1) {
                    t++;
                }
                else if(is_Intersecting(fireA, fireB))
                {
                    fireAreas -= Get_Intersection_Area(fireA, fireB);
                    index[q][t] = index[t][q] = 1;
                    t++;
                }
            }
            q++;
            t = 0;
        }
    }

    private boolean is_Intersecting(Fire fireA, Fire fireB) {
        return Rectangle2D.intersects(fireA.getX(),    fireA.getY(),
                                      fireA.getSize(), fireA.getSize(),
                                      fireB.getX(),    fireB.getY(),
                                      fireB.getSize(), fireB.getSize());
    }

    private double Get_Intersection_Area(Fire fireA, Fire fireB)
    {
        double distance = Math.hypot(fireB.getX() - fireA.getX(),
                                     fireB.getY() - fireA.getY());

        if(distance < fireA.getRadius() + fireB.getRadius()) {
            double a = fireA.getRadius() * fireA.getRadius();
            double b = fireB.getRadius() * fireB.getRadius();

            double x = (a - b + distance * distance) / (2 * distance);
            double z = x * x;
            double y = Math.sqrt(a - z);

            if(distance <= Math.abs(fireB.getRadius() - fireA.getRadius())) {
                return Math.PI * Math.min(a, b);
            }
            return  a * Math.asin(y / fireA.getRadius()) +
                    b * Math.asin(y / fireB.getRadius()) -
                    y * (x + Math.sqrt(z + b - a));
        }
        return 0;
    }


    @Override
    public void draw (Graphics g, Point containerOrigin)
    {
        ;
    }




    @Override
    public void localDraw(Graphics g, Point containerOrigin,
                                      Point Origin_Screen) {
        int separator = 35;
        g.setFont(getFont());
        g.drawRect(0, 0, getWidth(), getHeight());
        g.drawString("V  : " + value,
                     getWidth() + separator,
                     getHeight() - separator * 2);
        g.drawString("D  : " + (int) damagePercentage + "%",
                     getWidth() + separator,
                     getHeight() - separator);
    }
}
