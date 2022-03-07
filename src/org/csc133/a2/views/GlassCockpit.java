package org.csc133.a2.views;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.*;
import com.codename1.ui.layouts.GridLayout;
import org.csc133.a2.GameWorld;
import org.csc133.a2.gameobjects.Building;
import org.csc133.a2.gameobjects.Fire;
import org.csc133.a2.gameobjects.Helicopter;

class myLabel extends Label
{
    public myLabel(String text)
    {
        super(text);
    }
    void setValue(int val)
    {
        setText(Integer.toString(val));
    }
}




public class GlassCockpit extends Container {
    private final GameWorld gw;
    private final myLabel heading;
    private final myLabel speed;
    private final myLabel fuel;
    private final myLabel numFires;
    private final myLabel totalFire;
    private final myLabel damagePercent;
    private final myLabel financialLoss;

    public GlassCockpit() {
        setLayout(new GridLayout(2, 7));
        this.gw = GameWorld.getInstance();
        this.getAllStyles().setBgTransparency(255);
        this.getAllStyles().setBgColor(ColorUtil.WHITE);

        heading       = LabelMaker( ColorUtil.rgb(255, 0, 0));
        speed         = LabelMaker( ColorUtil.rgb(252, 118, 41));
        fuel          = LabelMaker(ColorUtil.YELLOW);
        numFires      = LabelMaker(ColorUtil.GREEN);
        totalFire     = LabelMaker(ColorUtil.CYAN);
        damagePercent = LabelMaker(ColorUtil.BLUE);
        financialLoss = LabelMaker(ColorUtil.rgb(132, 41, 252));
        addNavBarLabels();
        addLabels();
    }

    private myLabel LabelMaker(int Color) {
        myLabel dc = new myLabel("");
        return dc;
    }

    private void addNavBarLabels() {
        add(labelMaker("Heading"));
        add(labelMaker("Speed"));
        add(labelMaker("Fuel"));
        add(labelMaker("Fires"));
        add(labelMaker("Total Fire Size"));
        add(labelMaker("Damage %"));
        add(labelMaker("Financial Loss $"));
    }

    private myLabel labelMaker(String text) {
        myLabel lbl = new myLabel(text);
        lbl.getAllStyles().setAlignment(CENTER);
        lbl.getAllStyles().setFont(Font.createSystemFont(Font.FACE_SYSTEM,
                                                         Font.STYLE_BOLD,
                                                         Font.SIZE_MEDIUM));
        return lbl;
    }

    private void addLabels() {
        add(heading);
        add(speed);
        add(fuel);
        add(numFires);
        add(totalFire);
        add(damagePercent);
        add(financialLoss);
    }

    public void updateDisplay() {
        double totalFireSize    = 0;
        double buildingDmg      = 0;
        double totalFinanceLoss = 0;

        for(Fire fire : gw.getGameObjectCollection().getFires()) {
            totalFireSize += fire.getSize();
        }

        for(Building building : gw.getGameObjectCollection().getBuildings()) {
            buildingDmg += building.getDamagePercentage();
            totalFinanceLoss += building.getFinancialLoss();
        }

        updateHelicopterLbl(Helicopter.getInstance());
        updateFireLbl((int) totalFireSize);
        updateBuildingLbl((int) buildingDmg, (int) totalFinanceLoss);
    }

    private void updateHelicopterLbl(Helicopter helicopter) {
        heading.setValue(helicopter.getHeading());
        speed.setValue(helicopter.getSpeed());
        fuel.setValue(helicopter.getFuel());
    }

    private void updateFireLbl(int totalFireSize) {
        numFires.setValue(gw.getNumOfFires());
        totalFire.setValue(totalFireSize);
    }

    private void updateBuildingLbl(int buildingDmg, int totalFinanceLoss) {
        int percentage = buildingDmg/gw.getNumOfBuildings();
        damagePercent.setValue(percentage);
        financialLoss.setValue(totalFinanceLoss);
    }
}
