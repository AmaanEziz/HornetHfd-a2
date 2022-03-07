package org.csc133.a2.views;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Button;
import com.codename1.ui.Command;
import com.codename1.ui.Container;
import com.codename1.ui.Font;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.GridLayout;
import com.codename1.ui.plaf.Style;
import org.csc133.a2.GameWorld;
import org.csc133.a2.commands.*;
import org.csc133.a2.commands.*;

public class ControlCluster extends Container {
    private final Container NavBarLeft  = new Container(new GridLayout(1, 3));
    private final Container NavBarMid   = new Container(new GridLayout(1, 5));
    private final Container NavBarRight = new Container(new GridLayout(1, 3));
    private final Button bLeft;
    private final Button bRight;
    private final Button bFight;
    private final Button bExit;
    private final Button bDrink;
    private final Button bBrake;
    private final Button bAccel;

    public ControlCluster(MapView mv) {
        setLayout(new BorderLayout());


        this.getAllStyles().setBgColor(ColorUtil.WHITE);
        this.getAllStyles().setBgTransparency(255);

        bLeft    = buttonMaker(new TurnLeftCommand(), "Left");
        bRight   = buttonMaker(new TurnRightCommand(), "Right");
        bFight   = buttonMaker(new FightCommand(), "Fight");
        bExit    = buttonMaker(new ExitCommand(), "Exit");
        bDrink   = buttonMaker(new DrinkCommand(), "Drink");
        bBrake   = buttonMaker(new BrakeCommand(), "Brake");
        bAccel   = buttonMaker(new AccelerateCommand(), "Accel");
        Button.setSameWidth(bLeft, bRight, bFight, bDrink, bAccel, bBrake);
        addButtons();
    }

    private Button buttonMaker(Command cmd, String btnText) {
        Button button = new Button(btnText);
        button.setCommand(cmd);

        Style settingsStyle = button.getAllStyles();
        settingsStyle.setFont(Font.createSystemFont(Font.FACE_SYSTEM,
                                                    Font.STYLE_BOLD,
                                                    Font.SIZE_MEDIUM));
        settingsStyle.setFgColor(ColorUtil.BLUE);
        settingsStyle.setBgColor(ColorUtil.LTGRAY);
        settingsStyle.setBgTransparency(255);

        // Gives feedback to user by changing pressed buttons to white.
        //
        button.getPressedStyle().setBgColor(ColorUtil.WHITE, true);

        return button;
    }

    private void addButtons() {
        // Group buttons in a NavBar to make them easier to place.
        //
        NavBarLeft.add(bLeft);
        NavBarLeft.add(bRight);
        NavBarLeft.add(bFight);
        NavBarMid.add(bExit);
        NavBarRight.add(bDrink);
        NavBarRight.add(bBrake);
        NavBarRight.add(bAccel);

        NavBarLeft.getAllStyles().setBgColor(ColorUtil.LTGRAY);
        NavBarLeft.getAllStyles().setBgTransparency(255);
        NavBarRight.getAllStyles().setBgColor(ColorUtil.LTGRAY);
        NavBarRight.getAllStyles().setBgTransparency(255);


        ((BorderLayout)this.getLayout()).setCenterBehavior(
                                        BorderLayout.CENTER_BEHAVIOR_CENTER);
        add(BorderLayout.WEST, NavBarLeft);
        add(BorderLayout.CENTER, NavBarMid);
        add(BorderLayout.EAST, NavBarRight);
    }



    @Override
    public void laidOut() {
        super.laidOut();
        GameWorld.getInstance().layButtons(this.getWidth(), this.getHeight());
    }
}
