package org.csc133.a2.gameobjects;

import com.codename1.ui.Transform;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.geom.Point2D;
import org.csc133.a2.GameWorld;
import java.util.ArrayList;

public class FlightPath {
    private final Dimension mapSize;
    private final Transform startPoint;
    private final Transform river;
    private Transform selectedFire;
    private final Point2D bottomCoordinates;
    private final Point2D topCoordinates;
    private final Point2D bottomrivercoordinates;

    public FlightPath(Transform startPoint, Dimension mapSize) {
        Transform defaultLocation = Transform.makeIdentity();
        defaultLocation.translate(mapSize.getWidth() * 0.85f,
                mapSize.getHeight() * 0.15f);

        this.selectedFire = defaultLocation;
        this.startPoint = startPoint;
        this.mapSize = mapSize;
        this.river = GameWorld.getInstance().getRiverOrigin();

        bottomCoordinates = new Point2D(mapSize.getWidth() * 0.01,
                mapSize.getHeight() * 0.02);
        topCoordinates = new Point2D(-mapSize.getWidth() * 0.10,
                mapSize.getHeight() * 0.98);
        bottomrivercoordinates = new Point2D(-mapSize.getWidth() * 0.03,
                mapSize.getHeight() / 4f);
    }

    public void FireUpdate_SEL(Transform selectedFire) {
        this.selectedFire = selectedFire;

    }
}
