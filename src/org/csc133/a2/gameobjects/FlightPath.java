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
    private final Point2D lowerScreenCP;
    private final Point2D upperScreenCP;
    private final Point2D belowRiverCP;

    public FlightPath(Transform startPoint, Dimension mapSize) {
        Transform defaultLocation = Transform.makeIdentity();
        defaultLocation.translate(mapSize.getWidth() * 0.85f,
                                  mapSize.getHeight() * 0.15f);

        this.selectedFire = defaultLocation;
        this.startPoint   = startPoint;
        this.mapSize      = mapSize;
        this.river        = GameWorld.getInstance().getRiverOrigin();

        lowerScreenCP = new Point2D( mapSize.getWidth() * 0.01,
                                     mapSize.getHeight() * 0.02);
        upperScreenCP = new Point2D(-mapSize.getWidth() * 0.10,
                                     mapSize.getHeight() * 0.98);
        belowRiverCP  = new Point2D(-mapSize.getWidth() * 0.03,
                                     mapSize.getHeight() / 4f);
}

    private ArrayList<Point2D> initPathToRiver() {
        ArrayList<Point2D> temp = new ArrayList<>();
        int directionStabilizer = 1;

        // Helipad endpoint.
        temp.add(new Point2D(startPoint.getTranslateX(),
                             startPoint.getTranslateY() + directionStabilizer));

        // Center of screen control point.
        temp.add(new Point2D(mapSize.getWidth() / 2f,
                             mapSize.getHeight() / 2f));

        // River's leftmost side control point.
        temp.add(new Point2D(river.getTranslateX() * 0.03,
                             river.getTranslateY()));

        // River's origin endpoint.
        temp.add(new Point2D(river.getTranslateX(),
                             river.getTranslateY()));
        return temp;
    }

    private ArrayList<Point2D> initPathToFire() {
        ArrayList<Point2D> temp = new ArrayList<>();

        // River's origin endpoint.
        temp.add(new Point2D(river.getTranslateX(),
                             river.getTranslateY()));

        // River's rightmost side control point.
        temp.add(new Point2D(river.getTranslateX() * 1.97,
                             river.getTranslateY()));

        // Endpoint dependent by selected fire's location.
        temp.add(new Point2D(selectedFire.getTranslateX(),
                             selectedFire.getTranslateY()));
        return temp;
    }

    private ArrayList<Point2D> initPathFromFire(Point2D controlPoint) {
        ArrayList<Point2D> temp = new ArrayList<>();

        // Starting endpoint dependent by selected fire's location.
        temp.add(new Point2D(selectedFire.getTranslateX(),
                             selectedFire.getTranslateY()));

        // Control point dependent of fire's quadrant.
        temp.add(controlPoint);

        // Control point located in the far left side of the river.
        temp.add(new Point2D(-river.getTranslateX() * 0.03,
                              river.getTranslateY()));

        // River's origin endpoint.
        temp.add(new Point2D(river.getTranslateX(),
                             river.getTranslateY()));
        return temp;
    }



    private void recreatePath() {
        updateSelectedFire(selectedFire);
    }

    public void updateSelectedFire(Transform selectedFire) {
        this.selectedFire = selectedFire;

    }

    private boolean inPositiveYQuadrant() {
        return selectedFire.getTranslateY() > mapSize.getHeight() * 0.5;
    }

    private boolean inNegativeXQuadrant() {
        return selectedFire.getTranslateX() < mapSize.getWidth() * 0.5;
    }}


