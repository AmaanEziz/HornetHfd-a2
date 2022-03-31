package org.csc133.a2;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Form;
import com.codename1.ui.Graphics;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.util.UITimer;
import org.csc133.a2.commands.*;
import org.csc133.a2.views.ControlCluster;
import org.csc133.a2.views.GlassCockpit;
import org.csc133.a2.views.MapView;



public class Game extends Form implements Runnable {

    protected GameWorld gw;
    protected MapView mapView;
    protected GlassCockpit cockPit;
    protected ControlCluster controlCluster;

    public Game() {
        gw = new GameWorld();
        mapView = new MapView(gw);
        cockPit = new GlassCockpit(gw);
        controlCluster = new ControlCluster(gw);

        this.setLayout(new BorderLayout());
        this.add(BorderLayout.NORTH, cockPit);
        this.add(BorderLayout.CENTER, mapView);
        this.add(BorderLayout.SOUTH, controlCluster);

        addKeyListener('Q', new Exit(gw));
        addKeyListener(-93, new TurnLeft(gw));
        addKeyListener(-94, new TurnRight(gw));
        addKeyListener(-91, new Accelerate(gw));
        addKeyListener(-92, new Break(gw));
        addKeyListener('f', new Fight(gw));
        addKeyListener('d', new Drink(gw));

        UITimer timer = new UITimer(this);
        timer.schedule(100, true, this);
        this.getAllStyles().setBgColor(ColorUtil.BLACK);
        this.show();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        //mapView.paint(g);
    }

    @Override
    public void run() {
        gw.tick();
        cockPit.update();
        repaint();
    }
}
