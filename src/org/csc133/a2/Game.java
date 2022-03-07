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
    private final GameWorld gw;
    private final MapView mv;
    private final GlassCockpit gc;
    private final ControlCluster cc;

    public Game() {
        setLayout(new BorderLayout());
        this.getAllStyles().setBgColor(ColorUtil.BLACK);

        gw = GameWorld.getInstance();
        mv = new MapView();
        gc = new GlassCockpit();
        cc = new ControlCluster(mv);

        setUpCommands();
        setUpViews();
        setUpTimer();
        show();
        gw.init();
    }

    private void setUpCommands() {
        addKeyListener('Q', new ExitCommand());
        addKeyListener('f', new FightCommand());
        addKeyListener('d', new DrinkCommand());
        addKeyListener(-93, new TurnLeftCommand());
        addKeyListener(-94, new TurnRightCommand());
        addKeyListener(-91, new AccelerateCommand());
        addKeyListener(-92, new BrakeCommand());
    }

    private void setUpViews() {
        this.add(BorderLayout.NORTH,  gc);
        this.add(BorderLayout.CENTER, mv);
        this.add(BorderLayout.SOUTH,  cc);
    }

    private void setUpTimer() {
        UITimer timer = new UITimer(this);
        timer.schedule(100, true, this);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
    }

    @Override
    public void run() {
        gc.updateDisplay();
        gw.tick();
        repaint();
    }
}