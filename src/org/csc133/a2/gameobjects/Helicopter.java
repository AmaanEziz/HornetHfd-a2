package org.csc133.a2.gameobjects;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Font;
import com.codename1.ui.Graphics;
import com.codename1.ui.Transform;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.geom.Point;
import org.csc133.a2.GameWorld;
import org.csc133.a2.gameobjects.shapes.Arc;
import org.csc133.a2.gameobjects.shapes.Rectangle;
import org.csc133.a2.interfaces.Steerable;

import java.util.ArrayList;

import static com.codename1.ui.CN.*;

public class Helicopter extends Movable implements Steerable {

    private final int   Helicopter_Radius             = 40;
    private final float Helicopter_Y_Pos            = Helicopter_Radius * 0.2f;
    private final float FRONT_WIDTH             = Helicopter_Radius * 2.85f;
    private final float FRONT_HEIGHT            = Helicopter_Radius * 0.48f;
    private final float FRONT_SHIFT_Y           = Helicopter_Radius * 0.13f;

    private class Helicopter_Body_Circle_Part extends Arc {
        public Helicopter_Body_Circle_Part(int partColor) {
            super(  partColor,
                    2 * Helicopter_Radius,
                    2 * Helicopter_Radius,
                    0, Helicopter_Y_Pos,
                    1, 1,
                    0, 0, 360);
        }
    }

    private class Helicopter_Body_Pointy_Part extends Rectangle {
        public Helicopter_Body_Pointy_Part(int partColor) {
            super(  partColor,
                    (int) FRONT_WIDTH,
                    (int) FRONT_HEIGHT,
                    0, - FRONT_SHIFT_Y,
                    1, 1, 0);
        }
    }

    private HelicopterState helicopterState;
    private final int partialFuelConsumption = 3;

    private void changeState(HelicopterState helicopterState) {
        this.helicopterState = helicopterState;
    }

    public String currentState() {
        String currentState = helicopterState.getClass().getSimpleName();

        if(currentState.equals("Ready") && getSpeed() == 0) {
            return "Can land";
        }
        else {
            return currentState;
        }
    }
    private abstract class HelicopterState {
        Helicopter getHelicopter() {
            return Helicopter.this;
        }
        void steerLeft() {}
        void steerRight() {}
        void accelerate() {}
        void brake() {}
        void drink(Transform river, int w, int h) {}
        void dumpWater() {}
        void depleteFuel() {}
        void updateLocalTransforms() {}
    }

    private class Ready extends HelicopterState {
        @Override
        void steerLeft() {
            setHeading(getHeading() + 15);
            getHelicopter().rotate(15);
        }

        @Override
        void steerRight() {
            setHeading(getHeading() - 15);
            getHelicopter().rotate(-15);
        }
        @Override
        void accelerate() {
            if(getSpeed() < 10) {
                setSpeed(getSpeed() + 1);
            }
        }
        @Override
        void brake() {
            if(getSpeed() > 0) {
                setSpeed(getSpeed() - 1);
            }
        }
        @Override
        void drink(Transform river, int w, int h) {
            if(water < 1000 && getSpeed() <= 2 && onTopOfObject(river, w, h)) {
                water += 100;
            }
        }
        @Override
        void dumpWater() {
            water = 0;
        }
        @Override
        void depleteFuel() {
            fuel -= Math.pow(getSpeed(), 2) + 5;
        }
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private final ArrayList<GameObject> helicopterParts;
    private Helicopter_Body_Circle_Part HeliBack;
    private int fuel;
    private int water;
    private final static int size = 70;
    private final int Color;

    public Helicopter(Dimension mapSize, int initFuel, Transform startPoint) {
        super(ColorUtil.GREEN, mapSize, size, size);
        water = 0;
        fuel = initFuel;
        this.Color = ColorUtil.GREEN;
        setFont(Font.createSystemFont(FACE_SYSTEM, STYLE_BOLD, SIZE_MEDIUM));

        this.translate(startPoint.getTranslateX(), startPoint.getTranslateY());

        helicopterState = new Ready();
        helicopterParts = new ArrayList<>();
        buildHelicopter();
    }

    private void buildHelicopter() {
        helicopterParts.add(new Helicopter_Body_Pointy_Part(Color));
        HeliBack = new Helicopter_Body_Circle_Part(Color);
        helicopterParts.add(HeliBack);

    }
    public void updateLocalTransforms() {
        helicopterState.updateLocalTransforms();
    }
    @Override
    public void steerLeft() {
        helicopterState.steerLeft();
    }

    @Override
    public void steerRight() {
        helicopterState.steerRight();
    }

    public void accelerate() {
        helicopterState.accelerate();
    }

    public void brake() {
        helicopterState.brake();
    }

    public void drink(Transform river, int width, int height) {
        helicopterState.drink(river, width, height);
    }

    public void dumpWater() {
        helicopterState.dumpWater();
    }

    public int getWater() {
        return water;
    }

    public void depleteFuel() {
        helicopterState.depleteFuel();
    }

    public int getFuel() {
        return fuel;
    }

    public boolean onTopOfObject(Transform object, int w, int h) {
        int offset = getDimension().getWidth() / 2;

        return getY() + offset >= object.getTranslateY() - h / 2f &&
               getY() - offset <= object.getTranslateY() + h / 2f &&
               getX() + offset >= object.getTranslateX() - w / 2f &&
               getX() - offset <= object.getTranslateX() + w / 2f;
    }
    private static Helicopter instance;
    public static Helicopter getInstance() {
        if(instance == null) {
            Dimension mapSize    = GameWorld.getInstance().getMapSize();
            int initFuel         = GameWorld.getInstance().getInitialFuel();
            Transform startPoint = GameWorld.getInstance().getTakeOffPoint();

            instance = new Helicopter(mapSize, initFuel, startPoint);
        }
        return instance;
    }






    @Override
    public void localDraw(Graphics g, Point containerOrigin,
                                      Point screenOrigin) {
        reversePrimitiveTranslate(g, getDimension());
        reverseContainerTranslate(g, containerOrigin);

        for(GameObject go : helicopterParts) {
            go.draw(g, containerOrigin, screenOrigin);
        }

        int textGap = 35;
        int xOffset = 60;

        applyTextTransforms(g, containerOrigin, screenOrigin);

        g.drawString("F   : " + fuel,
                     getWidth() + textGap + xOffset,
                     getHeight() + textGap);
        g.drawString("W : " + water,
                     getWidth() + textGap + xOffset,
                     getHeight() + textGap * 2);
    }
}