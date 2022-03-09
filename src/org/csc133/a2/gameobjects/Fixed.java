package org.csc133.a2.gameobjects;

import com.codename1.ui.geom.Dimension;

abstract class Fixed extends GameObject {
    private boolean objectPlaced = false;

    public Fixed(int color, Dimension mapSize, int width, int height) {
        super(color, mapSize, width, height);
    }

    @Override
    public void translate(double xOrigin, double yOrigin) {
        if(!objectPlaced) {
            super.translate(xOrigin, yOrigin);
            objectPlaced = true;
        }
    }
}
