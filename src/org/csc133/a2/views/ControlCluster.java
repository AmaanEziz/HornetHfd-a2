package org.csc133.a2.views;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Command;
import org.csc133.a2.GameWorld;
import org.csc133.a2.commands.*;
import com.codename1.ui.Container;
import com.codename1.ui.Button;
import com.codename1.ui.layouts.BorderLayout;

public class ControlCluster extends Container {
    /**
     * Figure out a way to place buttons in the center and right hand
     * of this layout
     */

    private GameWorld gw;

    private Button button;
    private Button myTurnRight;
    private Button myTurnLeft;
    private Button fightFire;
    private Button accelerate;
    private Button decelerate;
    private Button drink;
    private Button exit;

    private Container bottomLeftContainer;
    private Container bottomRightContainer;

    public ControlCluster(GameWorld gw){

        this.gw = gw;

        this.setLayout(new BorderLayout());

        myTurnRight = new Button();
        myTurnLeft = new Button();
        fightFire = new Button();
        accelerate = new Button();
        decelerate = new Button();
        drink = new Button();
        exit = new Button();

        bottomLeftContainer = new Container();
        bottomRightContainer = new Container();

        //Getting commands from GameWorld
        myTurnRight = buttonMaker(new TurnRight(gw), "Right");
        myTurnLeft = buttonMaker(new TurnLeft(gw), "Left");
        fightFire = buttonMaker(new Fight(gw), "Fight");
        accelerate = buttonMaker(new Accelerate(gw), "Accel");
        decelerate = buttonMaker(new Break(gw), "Break");
        drink = buttonMaker(new Drink(gw), "Drink");
        exit = buttonMaker(new Exit(gw), "Exit");

        //Adding buttons to container
        bottomLeftContainer.add(myTurnRight);
        bottomLeftContainer.add(myTurnLeft);
        bottomLeftContainer.add(fightFire);

        //Adding Buttons to right container
        bottomRightContainer.add(drink);
        bottomRightContainer.add(decelerate);
        bottomRightContainer.add(accelerate);

        //Adding container to control cluster container
        this.addComponent(BorderLayout.WEST,bottomLeftContainer);
        this.addComponent(BorderLayout.EAST,bottomRightContainer);
        this.addComponent(BorderLayout.CENTER, exit);

    }
    public Button buttonMaker(Command action, String actionName){
        button = new Button();
        this.getUnselectedStyle().setBgTransparency(255);
        this.getUnselectedStyle().setBgColor(ColorUtil.WHITE);
        this.getAllStyles().setPadding(RIGHT, 3);
        this.getAllStyles().setPadding(LEFT, 3);
        this.getAllStyles().setMargin(RIGHT, 3);
        this.getAllStyles().setMargin(LEFT, 3);
        button.setCommand(action);
        return button;
    }

}
