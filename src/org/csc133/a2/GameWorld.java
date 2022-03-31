package org.csc133.a2;

import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.geom.Dimension;
import org.csc133.a2.gameObjects.*;

import java.util.ArrayList;
import java.util.Random;



public class GameWorld {

    public static final int FUEL = 30000;
    private final int numberOfFires = 2;

    private int numberOfBuildings = 3;
    private int totalFireSize = 0;
    private int xPosition;
    private int yPosition;

    private Dimension mapSize;

    private Object GameObject;
    private River river;
    private Helipad helipad;
    private Helicopter helicopter;
    private Building building;

    private ArrayList<GameObject> gameObjects;
    private ArrayList<Fire> fires;
    private ArrayList<Building> buildings;

    private Random r = new Random();

    public GameWorld() {
    }

    public void init() {

        buildings = new ArrayList<>();
        fires = new ArrayList<>();
        gameObjects = new ArrayList<GameObject>();

        gameObjects.add(createRiver());
        createBuilding();
        addBuildingToGO();
        createFire();
        addFireToGO();
        gameObjects.add(createHelipad());
        gameObjects.add(createHelicopter((int)helipad.getX() + 55,
                (int)helipad.getY() + 60));
    }

    private void addFireToGO(){
        for(Fire fire : fires){
            gameObjects.add(fire);
        }
    }

    /**
     * Since the fire objects are added to Building object collection
     * this method takes those objects and adds each object in the collection
     * to a GameObject collection.
     */
    private void addBuildingToGO(){
        for(Building building : buildings){
            gameObjects.add(building);
        }
    }

    private void createBuilding(){
        for(int i = 0;i<numberOfBuildings;i++){
            switch (i){
                    case 0:
                        xPosition = mapSize.getWidth() - 400;
                        yPosition = mapSize.getHeight() - 500;
                        buildings.add(new Building(
                            xPosition,
                            yPosition,
                            mapSize.getWidth() / 8,
                            mapSize.getHeight() / 3));
                        break;

                    case 1:
                        xPosition = mapSize.getWidth() -
                                (mapSize.getWidth() - 200);
                        yPosition = mapSize.getHeight() - 600;
                        buildings.add(new Building(
                                xPosition,
                                yPosition,
                                mapSize.getWidth() / 8,
                                mapSize.getHeight() / 2));
                        break;

                    case 2:
                        xPosition = mapSize.getWidth() -
                                (mapSize.getWidth() - 400);
                        yPosition = mapSize.getHeight() -
                                (mapSize.getHeight() - 150);
                        buildings.add(new Building(
                                xPosition,
                                yPosition,
                                mapSize.getWidth() -
                                        (mapSize.getWidth() /2 - 200),
                                mapSize.getHeight() / 10));
                        break;
            }
        }
    }
    private River createRiver() {
        river = new River(mapSize);
        return river;
    }

    private void createFire() {
        for(Building building : buildings){
            for(int i = 0 ; i < numberOfFires;i++){
                Fire fire = new Fire();
                building.setFireInBuilding(fire);
                fires.add(fire);
            }
        }
    }

    private Helipad createHelipad() {
        helipad = new Helipad(mapSize);
        return helipad;
    }

    private Helicopter createHelicopter(int xPosition, int yPosition) {
        helicopter = new Helicopter(xPosition, yPosition);
        return helicopter;
    }

    private void checkSizeOfFire() {
        fires.removeIf(fire -> fire.size < 0);
    }

    private int getRandomX(int leftBound, int rightBound) {
        return r.nextInt((rightBound - leftBound) + 1) + leftBound;
    }

    private int getRandomY(int lowerBound, int upperBound) {
        return r.nextInt((upperBound - lowerBound) + 1) + lowerBound;
    }

    public void tick() {
        fireGrow();
        helicopter.move();
        drinkWater();
        checkSizeOfFire();
        helicopter.consumeFuel();
        helicopter.receiveFuel(helipad);
        updateBuildingDamage();
        updateBuildingValue();
        checkGameConditions();
    }

    public void updateBuildingValue(){
        for(Building build : buildings){
            build.updateBuildingValue(gameObjects);
        }
    }

    public void updateBuildingDamage(){
        for(Building build : buildings){
            build.setBuildingDamage(gameObjects);
        }
    }

    /**
     * Checks to see if losing or winning
     * game conditions have been satisfied.
     */
    private void checkGameConditions() {
        if (helicopter.fuel <= 0) {
            if (showLosingDialog()) {
                // user clicked yes
                //
                init();
            } else {
                // user clicked no
                //
                quit();
            }
        }
        if ((winningGameConditions())) {
            // user clicked yes
            //
            if (showWinningDialog()) {
                init();
            } else {
                quit();
            }
        }
    }

    private boolean helicopterLand() {
        return (helicopter.getX() > helipad.getX()
                && helicopter.getX() < helipad.getX() + helipad.getSize()
                && helicopter.getY() > helipad.getY()
                && helicopter.getY() < helipad.getY() + helipad.getSize());
    }

    /**
     * There is two different dialogs; this is winning dialog
     *
     * @return true if yes, false if no
     */
    private boolean showWinningDialog() {
        return Dialog.show("You Won!", "Score: " +
                getTotalBuildingValue() +
                "\nWould you like to play again?", "yes", "No");
    }

    /**
     * There is two different dialogs; this is losing dialog
     *
     * @return true if yes, false if no
     */
    private boolean showLosingDialog() {
        return Dialog.show("Game Over!", "You ran out of fuel\n" +
                "Would you like to play again?", "yes", "No");
    }

    /**
     * This is all the conditions necessary to win
     *
     * @return true if game conditions have been satisfied, false otherwise
     */
    private boolean winningGameConditions() {
        if ((!fires.stream().iterator().hasNext())
                && (helicopter.getSpeed() == 0)
                && helicopterLand()) {
            return true;
        } else {
            return false;
        }
    }

    public void quit() {
        Display.getInstance().exitApplication();
    }

    public void headHelicopterLeft() {
        helicopter.headLeft(-15);
    }

    public void headHelicopterRight() {
        helicopter.headRight(+15);
    }

    public void increaseSpeed() {
        helicopter.addSpeed(-1);
    }

    public void decreaseSpeed() {
        helicopter.addSpeed(1);
    }

    /**
     * This method calls the method with the fire fighting
     * logic
     */
    public void fightFire() {
        helicopter.fightFire(fires);
    }

    /**
     * this method calls the method with the water drinking logic
     */
    public void drinkWater() {
        helicopter.drink(river);
    }

    private void fireGrow() {
        int randomInt;
        for (GameObject go : gameObjects) {
            if(go instanceof Fire){
                Fire fire = (Fire)go;
                randomInt = r.nextInt(((500 + 1) + 1) + 1);
                if (randomInt == 300 || randomInt == 500 || randomInt == 100) {
                    fire.randomFireGrowth();
                }
            }

        }
    }
    public ArrayList<GameObject> getGameObjects(){
        return gameObjects;
    }

    public String getHeading() {
        int heading = helicopter.getHeading();
        if(heading == 360 || heading == -360){
            heading = heading % 360;
            helicopter.setHeading(0);
        }
        return String.valueOf(heading);
    }

    public String getSpeed() {
        return String.valueOf(Math.abs(helicopter.getSpeed()));
    }

    public String getFuel() {
        return String.valueOf(helicopter.fuel);
    }

    public void setDimension(Dimension mapSize) {
        this.mapSize = mapSize;
    }

    public String getFires() {
        int count = 0;
        return String.valueOf(fires.size());
    }

    public String getFireSize() {
        int temp = 0;
        for(GameObject go: gameObjects){
            if(go instanceof Fire){
                Fire fire = (Fire)go;
                temp += fire.getFireArea();
            }
        }
        return String.valueOf(temp);
    }

    public String getDamage() {
        int fireSize;
        int temp = 0;
        int damage;
        for(GameObject go : gameObjects){
            if(go instanceof Building){
                Building build = (Building)go;
                temp += build.getBuildingDamage();
            }
        }
        return String.valueOf(temp);
    }

    public String getLoss() {
        int totalFinancialLoss = 0;
        int totalBuildingDamage = 0;
        int totalBuildingArea = 0;
        for(GameObject go : gameObjects){
            if(go instanceof Building){
                Building build = (Building)go;
                totalBuildingDamage += build.getBuildingDamage();
                totalBuildingArea += build.getBuildingArea();
            }
        }
        totalFinancialLoss = 100 / (100000 /
                (totalBuildingArea / totalBuildingDamage));
        return String.valueOf(totalFinancialLoss);

    }

    public String getTotalBuildingValue(){
        int totalValueSaved = 0;
        for(GameObject go : gameObjects){
            if(go instanceof Building){
                Building build = (Building)go;
                totalValueSaved += build.getBuildingValue();
            }
        }
        return String.valueOf(totalValueSaved);
    }
}
