package org.csc133.a2.gameobjects;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Transform;
import com.codename1.ui.geom.Dimension;
import org.csc133.a2.GameWorld;

public class PlayerHelicopter extends Helicopter {
    private static PlayerHelicopter instance;
    private static final int[] color = {
            ColorUtil.rgb(71, 66, 98),     // RightSkids
            ColorUtil.rgb(71, 66, 98)};  // HUD

    private PlayerHelicopter(Dimension map, int initFuel, Transform startP) {
        super(map, initFuel, color, startP);
    }

    public static PlayerHelicopter getInstance() {
        if(instance == null) {
            Dimension mapSize    = GameWorld.getInstance().getMapSize();
            int initFuel         = GameWorld.getInstance().getInitialFuel();
            Transform startPoint = GameWorld.getInstance().getTakeOffPoint();

            instance = new PlayerHelicopter(mapSize, initFuel, startPoint);
        }
        return instance;
    }

    public void reset() {
        instance = null;
    }
}