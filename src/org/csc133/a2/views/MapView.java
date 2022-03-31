package org.csc133.a2.views;

import com.codename1.ui.geom.Dimension;
import com.codename1.ui.geom.Point;
import org.csc133.a2.GameWorld;
import org.csc133.a2.gameObjects.GameObject;

import com.codename1.ui.Container;
import com.codename1.ui.Graphics;


public class MapView extends Container {
    private GameWorld gw;

    public MapView(GameWorld gw){
        this.gw = gw;
        gw.setDimension(new Dimension (this.getWidth(), this.getHeight()));
    }

    @Override
    public void laidOut(){
        gw.setDimension(new Dimension(this.getWidth(), this.getHeight()));
        gw.init();
    }

    @Override
    public void paint(Graphics g){
        super.paint(g);
        for(GameObject go : gw.getGameObjects()){
            go.draw(g,
                    new Point((int)go.location.getX(), (int)go.location.getY())
            );
        }
    }

}
