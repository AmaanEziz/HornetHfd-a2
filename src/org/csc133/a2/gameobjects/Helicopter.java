package org.csc133.a2.gameobjects;

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

    // Change COCKPIT RADIUS to scale helicopter's size.
    //
    private final int   BACK_RADIUS             = 40;
    private final float BACK_SHIFT_Y            = BACK_RADIUS * 0.2f;
    private final float FRONT_WIDTH             = BACK_RADIUS * 2.85f;
    private final float FRONT_HEIGHT            = BACK_RADIUS * 0.48f;
    private final float FRONT_SHIFT_Y           = BACK_RADIUS * 0.13f;

    private class HELI_BACK extends Arc {
        public HELI_BACK(int partColor) {
            super(  partColor,
                    2 * BACK_RADIUS,
                    2 * BACK_RADIUS,
                    0, BACK_SHIFT_Y,
                    1, 1,
                    0, 0, 360);
        }
        private void updateLocalTransforms(double rotationSpeed) {
//            this.rotate(rotationSpeed);;
        ;
        }
    }

    private class HELI_FRONT extends Rectangle {
        public HELI_FRONT(int partColor) {
            super(  partColor,
                    (int) FRONT_WIDTH,
                    (int) FRONT_HEIGHT,
                    0, - FRONT_SHIFT_Y,
                    1, 1, 0);
        }
        private void updateLocalTransforms(double rotationSpeed) {
//            this.rotate(rotationSpeed);
            ;
        }
    }

    //````````````````````````````````````````````````````````````````````````

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

    //````````````````````````````````````````````````````````````````````````
    // Helicopter State Pattern
    //
    private abstract class HelicopterState {
        Helicopter getHelicopter() {
            return Helicopter.this;
        }

        abstract void startOrStopEngine();

        void steerLeft() {}

        void steerRight() {}

        void accelerate() {}

        void brake() {}

        void drink(Transform river, int w, int h) {}

        void dumpWater() {}

        void depleteFuel() {}
        void updateLocalTransforms() {}
    }

    //````````````````````````````````````````````````````````````````````````
    private class Off extends HelicopterState {
        @Override
        void startOrStopEngine() {
            getHelicopter().changeState(new Starting());
        }
    }

    //````````````````````````````````````````````````````````````````````````
    private class Starting extends HelicopterState {
        @Override
        void startOrStopEngine() {
            getHelicopter().changeState(new Stopping());
        }

        @Override
        void depleteFuel() {
            fuel -= partialFuelConsumption;
        }

        private void takeOff() {
            System.out.println("Taking off");
            getHelicopter().changeState(new Ready());
            GameWorld.getInstance().initiateChopper();
        }

        @Override
        void updateLocalTransforms() {
            HeliBack.updateLocalTransforms(80);
            System.out.println("Taking OFf");
            takeOff();
//            if(true) {
//                takeOff();
//            }
        }
    }

    //````````````````````````````````````````````````````````````````````````
    private class Stopping extends HelicopterState {
        @Override
        void startOrStopEngine() {
            getHelicopter().changeState(new Starting());
        }

        private void turnOffEngine() {
            getHelicopter().changeState(new Off());
        }

        @Override
        void updateLocalTransforms() {
            HeliBack.updateLocalTransforms(10);

//            if(rotationalSpeed <= 0) {
//                // Prevents a negative speed restarting speed.
//                //
//                rotationalSpeed = 0;
//                turnOffEngine();
//            }
        }

    }

    //````````````````````````````````````````````````````````````````````````
    private class Ready extends HelicopterState {
        @Override
        void startOrStopEngine() {
            if(getSpeed() == 0) {
                getHelicopter().changeState(new Stopping());
                GameWorld.getInstance().stopChopper();
            }
        }

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
            System.out.println("Accelerating");

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

        @Override
        void updateLocalTransforms() {
            HeliBack.updateLocalTransforms(80);
        }
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private final ArrayList<GameObject> helicopterParts;
    private HELI_BACK HeliBack;
    private int fuel;
    private int water;
    private final static int size = 70;
    private final int[] partColors;

    public Helicopter(Dimension mapSize, int initFuel, int[] partColors,
                                                       Transform startPoint) {
        super(partColors[1], mapSize, size, size);
        water = 0;
        fuel = initFuel;
        this.partColors = partColors;
        setFont(Font.createSystemFont(FACE_SYSTEM, STYLE_BOLD, SIZE_MEDIUM));

        this.translate(startPoint.getTranslateX(), startPoint.getTranslateY());

        helicopterState = new Ready();
        helicopterParts = new ArrayList<>();
        buildHelicopter();
    }

    private void buildHelicopter() {
        helicopterParts.add(new HELI_FRONT(partColors[1]));
        HeliBack = new HELI_BACK(partColors[0]);
        helicopterParts.add(HeliBack);

    }
    public void updateLocalTransforms() {
        helicopterState.updateLocalTransforms();
    }


    public void startOrStopEngine() {
        helicopterState.startOrStopEngine();
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
//        g.drawString("W : " + water,
//                     getWidth() + textGap + xOffset,
//                     getHeight() + textGap * 2);
        g.drawString("Speed : " + getSpeed(),
                getWidth() + textGap + xOffset,
                getHeight() + textGap * 2);
    }
}