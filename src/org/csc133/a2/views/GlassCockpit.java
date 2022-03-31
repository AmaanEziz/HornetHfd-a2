package org.csc133.a2.views;

import com.codename1.charts.util.ColorUtil;
import org.csc133.a2.GameWorld;
import com.codename1.ui.layouts.GridLayout;
import com.codename1.ui.Container;
import com.codename1.ui.Label;


public class GlassCockpit extends Container{

    Label labelHeading;
    Label labelSpeed;
    Label labelFuel;
    Label labelFires;
    Label labelFireSize;
    Label labelDamage;
    Label labelLoss;

    Label labelHeadingValue;
    Label labelSpeedValue;
    Label labelFuelValue;
    Label labelFiresValue;
    Label labelFireSizeValue;
    Label labelDamageValue;
    Label labelLossValue;

    int heading;
    int speed;
    int fuel;
    int fires;
    int fireSize;
    int damage;
    int loss;

    private GameWorld gw;

    public GlassCockpit(GameWorld gw){
        this.gw = gw;

        this.setLayout(new GridLayout(2, 7));
        this.add(labelHeading = new Label(" HEADING "));
        this.add(labelSpeed = new Label(" SPEED "));
        this.add(labelFuel = new Label(" FUEL "));
        this.add(labelFires = new Label(" FIRES "));
        this.add(labelFireSize = new Label(" FIRE SIZE "));
        this.add(labelDamage = new Label ( "DAMAGE "));
        this.add(labelLoss = new Label (" LOSS "));

        this.add(labelHeadingValue = new Label ("" + heading));
        this.add(labelSpeedValue = new Label ("" + speed));
        this.add(labelFuelValue = new Label ("" + fuel));
        this.add(labelFiresValue = new Label ("" + fires));
        this.add(labelFireSizeValue = new Label ("" + fireSize));
        this.add(labelDamageValue = new Label ("" + damage));
        this.add(labelLossValue = new Label ("" + loss));
        this.getUnselectedStyle().setBgTransparency(255);
        this.getUnselectedStyle().setBgColor(ColorUtil.WHITE);

    }

    public void update(){
        labelHeadingValue.setText(gw.getHeading());
        labelSpeedValue.setText(gw.getSpeed());
        labelFuelValue.setText(gw.getFuel());
        labelFiresValue.setText(gw.getFires());
        labelFireSizeValue.setText(gw.getFireSize());
        labelDamageValue.setText(gw.getDamage());
        labelLossValue.setText(gw.getLoss());
    }

}