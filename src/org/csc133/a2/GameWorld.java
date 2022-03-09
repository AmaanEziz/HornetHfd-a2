package org.csc133.a2;

import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.Transform;
import com.codename1.ui.geom.Dimension;
import org.csc133.a2.gameobjects.*;
import org.csc133.a2.gameobjects.*;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class GameWorld {
    private static GameWorld instance;
    private int NUMBER_OF_BUILDINGS;
    private int fireAreaBudget;
    private int numberOfFires;
    private int defaultFireSize;
    private int initFuel;
    private long startOfElapsedTime;
    private Random rand;
    private Building building;
    private Dimension mapSize;
    private Dimension controlSize;
    private ArrayList<Integer> fireSizes;
    private GameObjectCollection<GameObject> GameObjects;
    private FireDispatch fireDispatch;
    private FlightPath flightPath;


    private GameWorld() {}

    public static GameWorld getInstance() {
        if(instance == null) {
            instance = new GameWorld();
        }
        return instance;
    }

    public void init() {
        initFuel            = 25000;
        fireAreaBudget      = 1000;
        NUMBER_OF_BUILDINGS = 3;
        numberOfFires       = 0;
        defaultFireSize     = 5;
        startOfElapsedTime  = TimeUnit.NANOSECONDS.toMillis(System.nanoTime());
        rand                 = new Random();
        fireDispatch         = new FireDispatch();
        GameObjects = new GameObjectCollection<>();
        populateGameObjectCollection();
    }

    private void populateGameObjectCollection() {
        GameObjects.add(new River(mapSize));
        GameObjects.add(new Helipad(mapSize));

        fireSizes = new ArrayList<>();
        setNumberOfFires();
        createBuildingAndFires();
        setFireInBuilding();

        flightPath = new FlightPath(getTakeOffPoint(), mapSize);
        GameObjects.add(Helicopter.getInstance());
    }

    private void setNumberOfFires() {
        while(fireAreaBudget > 0) {
            fireSizes.add(expendFireBudget());
            numberOfFires++;
        }
    }

    private int expendFireBudget() {
        int area = rand.nextInt(180 - 80) + 80;

        if(area > fireAreaBudget) {
            area = fireAreaBudget;
            fireAreaBudget = 0;
        }
        else {
            fireAreaBudget -= area;
        }
        return findRadiusFromArea(area);
    }

    private int findRadiusFromArea(int area) {
        return (int) Math.ceil(Math.sqrt(area / Math.PI));
    }

    private void createBuildingAndFires() {
        int firesToCreate = numberOfFires;
        for (int i = NUMBER_OF_BUILDINGS; i > 0; i--) {
            GameObjects.add(new Building(mapSize, i - 1));
            for(int j = firesToCreate / i; j > 0; j--) {
                GameObjects.add(new Fire(mapSize,
                                                  fireSizes.get(0),
                                                  fireDispatch));
                fireSizes.remove(0);
                firesToCreate--;
            }
        }
    }

    private void setFireInBuilding() {
        for(GameObject go : GameObjects) {
            if(go instanceof Building) {
                building = (Building)go;
            }
            if(go instanceof Fire) {
                Fire fire = (Fire)go;
                building.setFireInBuilding(fire);
            }
        }
    }

    private void spawnNewFires() {
        int index = 0;
        for (Building building : GameObjects.getBuildings()) {
            if (willFireSpawn(building)) {
                Fire fire = new Fire(mapSize, defaultFireSize, fireDispatch);
                building.setFireInBuilding(fire);
                GameObjects.add(index + 1, fire);
                updateNumOfFires(1);
            }
            index++;
        }
    }

    private boolean willFireSpawn(Building building) {
        if(building.allFiresPutOut()) {
            return false;
        }
        return rand.nextInt((int) (700 - building.getDamagePercentage())) < 1;
    }

    public GameObjectCollection<GameObject> getGameObjectCollection() {
        return GameObjects;
    }

    public int getInitialFuel() {
        return initFuel;
    }

    public Transform getTakeOffPoint() {
        return GameObjects.getHelipad().get(0).takeoffSpot();
    }

    public void layGameMap(Dimension mapSize) {
        this.mapSize = mapSize;
    }

    public void layButtons(int controlWidth, int controlHeight) {
        this.controlSize = new Dimension(controlWidth, controlHeight);
    }

    public Dimension getMapSize() {
        return mapSize;
    }

    public int getControlClusterHeight() {
        return controlSize.getHeight();
    }

    public FlightPath getFlightPath() {
        return flightPath;
    }

    public Transform getRiverOrigin() {
        return GameObjects.getRiver().get(0).getTranslation();
    }

    public Dimension getRiverDimension() {
        return GameObjects.getRiver().get(0).getDimension();
    }

    public void updateSelectedFire(Transform selectedFire) {
        flightPath.updateSelectedFire(selectedFire);
    }

    public int getNumOfBuildings() {
        return NUMBER_OF_BUILDINGS;
    }

    public int getNumOfFires() {
        return numberOfFires;
    }

    private Helicopter getHelicopter() {
        return Helicopter.getInstance();
    }



    public void turnLeft() {
        getHelicopter().steerLeft();
    }

    public void turnRight() {
        getHelicopter().steerRight();
    }

    public void accelerate() {
        System.out.println("Heli flying");
        getHelicopter().accelerate();
    }

    public void brake() {
        getHelicopter().brake();
    }

    private void depleteFuel() {
        Helicopter.getInstance().depleteFuel();
    }

    private void move(long elapsedTimeInMillis) {
        Helicopter.getInstance().move(elapsedTimeInMillis);
    }

    private long getElapsedTimeInMillis() {
        long endElapsedTime = TimeUnit.NANOSECONDS.toMillis(System.nanoTime());
        long elapsedTimeInMillis = endElapsedTime - startOfElapsedTime;
        startOfElapsedTime = TimeUnit.NANOSECONDS.toMillis(System.nanoTime());
        return elapsedTimeInMillis;
    }

    public void drink() {
        for(River river : GameObjects.getRiver()) {
                int w = river.getWidth();
                int h = river.getHeight();
                getHelicopter().drink(river.getTranslation(), w, h);
        }
    }

    public void attemptFightFire(Helicopter helicopter) {
        for (Fire fire : GameObjects.getFires()) {
            if (helicopterHasWater(helicopter)
                    && isHelicopterOverFire(helicopter, fire)
                    && !isExtinguished(fire)) {
                fire.shrink(helicopter.getWater());
                if (fire.getSize() <= 0) {
                    updateNumOfFires(-1);
                }
            }
        }
        helicopter.dumpWater();
    }

    private boolean helicopterHasWater(Helicopter helicopter) {
        return helicopter.getWater() > 0;
    }

    private boolean isHelicopterOverFire(Helicopter helicopter, Fire fire) {
        int w = fire.getSize();
        int h = fire.getSize();
        return helicopter.onTopOfObject(fire.getTranslation(), w, h);
    }

    private boolean isExtinguished(Fire fire) {
        return fire.currentState().equals("Extinguished");
    }

    private void updateNumOfFires(int updateValue) {
        numberOfFires += updateValue;
    }

    private void grow() {
        for(Fire fire : GameObjects.getFires()) {
            if(fire.getSize() <= 0 && !isExtinguished(fire)) {
                updateNumOfFires(-1);
            }
            else if(rand.nextInt(8) < 1) {
                fire.grow();
            }
        }
    }

    private void burnBuilding() {
        for(Building building : GameObjects.getBuildings()) {
                building.accumulateFireAreas();
                building.setDamagePercentage();
        }
    }

    private void checkIfWon() {
        if(!areThereFires() && hasLandedOnHelipad() && isThereFuel()) {
            displayWinDialog();
        }
    }

    private boolean areThereFires() {
        for(Fire fire : GameObjects.getFires()) {
            if(!fire.currentState().equals("Extinguished")) {
                return true;
            }
        }
        return false;
    }

    private boolean hasLandedOnHelipad() {
        Helipad helipad = getGameObjectCollection().getHelipad().get(0);
        Helicopter ph = Helicopter.getInstance();

        int w = helipad.getWidth();
        int h = helipad.getHeight();
        return ph.onTopOfObject(helipad.getTranslation(), w, h) &&
                ph.currentState().equals("Off");
    }

    private boolean isThereFuel() {
        return  Helicopter.getInstance().getFuel() > 0;
    }

    public void restartGame() {
        if(Dialog.show("Game Paused", "Are you sure you want to restart " +
                       "the game?", "Yes, restart the game", "No")) {
        }
    }

    private void displayWinDialog() {
        if(Dialog.show("Game Over", "You Won!" + "\nScore: " + getScore() +
                       "\nPlay again?", "Heck Yeah!", "Some other time")) {
        }
        else {
            exit();
        }
    }

    private int getScore() {
        return (int) (100 - getDamagePercentage());
    }

    private void checkIfLost() {
        String lossReason = "";

        if(!isThereFuel()) {
            lossReason = "Helicopter ran out of fuel!";
        }
        else if(allBuildingsBurned()) {
            lossReason = "All buildings burned out!";
        }
        if(!lossReason.isEmpty()) {
            displayLossDialog(lossReason);
        }
    }

    private boolean allBuildingsBurned() {
        return getDamagePercentage() >= 100;
    }

    private double getDamagePercentage() {
        double totalDamagePercentage = 0;
        for(Building building : GameObjects.getBuildings()) {
            totalDamagePercentage += building.getDamagePercentage();
        }
        return totalDamagePercentage / NUMBER_OF_BUILDINGS;
    }

    private void displayLossDialog(String lossReason) {
        if(Dialog.show("Game Over", lossReason +
                       "\nScore: 0"  +
                       "\nPlay again?", "Heck Yeah!", "Some other time")) {
        }
        else {
            exit();
        }
    }

    public void exit() {
        if(Dialog.show("Game Paused", "Are you sure you want to quit?",
                       "Yes", "I want to keep playing")) {
            Display.getInstance().exitApplication();
        }
    }

    public void updateLocalTransforms() {
        Helicopter.getInstance().updateLocalTransforms();
    }


    public String getHelicopterState() {
        return getHelicopter().currentState();
    }

    private boolean isHeliFlying() {
        return  getHelicopter().currentState().equals("Ready") ||
                getHelicopter().currentState().equals("Can land");
    }


    public void tick() {
        move(getElapsedTimeInMillis());
        spawnNewFires();
        grow();
        burnBuilding();
        depleteFuel();
        checkIfWon();
        checkIfLost();
    }
}