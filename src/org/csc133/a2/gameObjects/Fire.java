package org.csc133.a2.gameObjects;
import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Font;
import com.codename1.ui.Graphics;
import com.codename1.ui.geom.Point;
import java.util.Random;

import static com.codename1.ui.CN.*;

public class Fire extends Fixed{

    public int size;
    private int area;
    private int color;
    private Random r = new Random();

    public Fire(){
        super(0,0,0);
        getRandomSize();
        setFireArea();
        setColor();
    }

    public Fire(int xPosition, int yPosition) {
        super(xPosition, yPosition, 300);
        getRandomSize();
    }

    private void setFireArea(){
        area = (int) (Math.PI * Math.pow(size /2, 2 ));
    }

    private void setColor(){
        this.color = ColorUtil.rgb(238, 1, 255);
    }

    private void updateFireArea(){
        area = (int) (Math.PI * Math.pow(size /2, 2 ));
    }

    public int getFireArea(){
        updateFireArea();
        return this.area;
    }

    void getRandomSize() {
        size = r.nextInt((100 - 50) + 50);
    }

    @Override
    public void draw(Graphics g, Point containerOrigin) {
        g.setColor(this.color);
        g.fillRoundRect(containerOrigin.getX(),
                containerOrigin.getY(),
                this.size,
                this.size,
                this.size,
                this.size);
        g.drawString(String.valueOf(this.size),
                containerOrigin.getX() + this.size,
                containerOrigin.getY() + this.size);

        g.setFont(Font.createSystemFont(FACE_SYSTEM, STYLE_PLAIN, SIZE_MEDIUM));
    }

    private void updateSize(int increaseSize){
        this.size += increaseSize;
    }

    public void randomFireGrowth() {
        int increaseSize;
        increaseSize = r.nextInt(10);
        this.updateSize(increaseSize);
    }
}
