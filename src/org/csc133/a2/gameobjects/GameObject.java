package org.csc133.a2.gameobjects;

import com.codename1.ui.Font;
import com.codename1.ui.Graphics;
import com.codename1.ui.Transform;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.geom.Point;
import org.csc133.a2.interfaces.Drawable;

public abstract class GameObject implements Drawable {
    private int color;
    private Dimension mapSize;
    private Dimension dimensions;
    private Font font;
    private Transform OrgXFormation;
    private final Transform translation;
    private final Transform rotation;
    private final Transform scale;

    public GameObject() {
        translation = Transform.makeIdentity();
        rotation = Transform.makeIdentity();
        scale = Transform.makeIdentity();
    }

    public GameObject(int color, Dimension mapSize, int width, int height) {
        this.color = color;
        this.mapSize = mapSize;
        dimensions = new Dimension(width, height);
        translation = Transform.makeIdentity();
        rotation = Transform.makeIdentity();
        scale = Transform.makeIdentity();
    }

    protected void setColor(int color) {
        this.color = color;
    }

    protected void setDimensions(int width, int height) {
        dimensions = new Dimension(width, height);
    }

    protected void setFont(Font font) {
        this.font = font;
    }

    Font getFont() {
        return font;
    }

    Dimension getMapSize() {
        return mapSize;
    }

    public Dimension getDimension() {
        return dimensions;
    }

    public int getWidth() {
        return dimensions.getWidth();
    }

    public int getHeight() {
        return dimensions.getHeight();
    }

    float getY() {
        return translation.getTranslateY();
    }

    Transform PreLeftTrans(Graphics g, Point Origin_Screen) {
        Transform gXform = Transform.makeIdentity();
        g.getTransform(gXform);
        OrgXFormation = gXform.copy();
        gXform.translate(Origin_Screen.getX(),Origin_Screen.getY());
        return gXform;
    }

    void localTransforms(Transform gXform) {
        gXform.translate(getX(), getY());
        gXform.concatenate(rotation);
        gXform.scale(getScaleX(), getScaleY());
    }

    void PostLeftTrans(Graphics g, Point Origin_Screen, Transform gXform) {
        gXform.translate(-Origin_Screen.getX(), -Origin_Screen.getY());
        g.setTransform(gXform);
    }

    void forwardPrimitiveTranslate(Graphics g, Dimension dimension) {
        Transform gXform = Transform.makeIdentity();
        g.getTransform(gXform);
        gXform.translate(-dimension.getWidth() / 2f,
                         -dimension.getHeight() / 2f);
        g.setTransform(gXform);
    }

    protected void rotate(double degrees) {
        rotation.rotate((float) Math.toRadians(degrees), 0, 0);
    }

    protected void scale(double sX, double sY) {
        scale.scale((float) sX, (float) sY);
    }

    protected float getScaleX() {
        return scale.getScaleX();
    }

    protected float getScaleY() {
        return scale.getScaleY();
    }

    protected void translate(double tX, double tY) {
        translation.translate((float) tX, (float) tY);
    }

    public Transform getTranslation() {
        return translation;
    }

    float getX() {
        return translation.getTranslateX();
    }


    void reversePrimitiveTranslate(Graphics g, Dimension dimension) {
        Transform gXform = Transform.makeIdentity();
        g.getTransform(gXform);
        gXform.translate(dimension.getWidth() / 2f, dimension.getHeight() / 2f);
        g.setTransform(gXform);
    }

    void containerTranslate(Graphics g, Point containerOrigin) {
        Transform gXform = Transform.makeIdentity();
        g.getTransform(gXform);
        gXform.translate(containerOrigin.getX(), containerOrigin.getY());
        g.setTransform(gXform);
    }

    void reverseContainerTranslate(Graphics g, Point containerOrigin) {
        Transform gXform = Transform.makeIdentity();
        g.getTransform(gXform);
        gXform.translate(-containerOrigin.getX(), -containerOrigin.getY());
        g.setTransform(gXform);
    }

    void restoreOriginalTransforms(Graphics g) {
        g.setTransform(OrgXFormation);
    }

    void applyTextTransforms(Graphics g, Point containerOrigin, Point Origin_Screen) {
        g.setColor(color);
        g.setFont(font);

        restoreOriginalTransforms(g);
        g.setTransform(OrgXFormation);
        Transform gXform = PreLeftTrans(g, Origin_Screen);
        gXform.translate(getX(), getY());
        gXform.scale(1, -1);
        PostLeftTrans(g, Origin_Screen, gXform);
        forwardPrimitiveTranslate(g, dimensions);
        containerTranslate(g, containerOrigin);
    }

    abstract public void localDraw(Graphics g, Point containerOrigin, Point Origin_Screen);

    public void draw(Graphics g, Point containerOrigin, Point Origin_Screen) {
        g.setColor(color);

        Transform gXform = PreLeftTrans(g, Origin_Screen);
        localTransforms(gXform);
        PostLeftTrans(g, Origin_Screen, gXform);
        forwardPrimitiveTranslate(g, dimensions);
        containerTranslate(g, containerOrigin);

        localDraw(g, containerOrigin, Origin_Screen);
        restoreOriginalTransforms(g);
    }
}