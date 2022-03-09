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

    float getY() {
        return translation.getTranslateY();
    }

    Transform preLTransform(Graphics g, Point screenOrigin) {
        Transform gXform = Transform.makeIdentity();
        g.getTransform(gXform);
        OrgXFormation = gXform.copy();
        gXform.translate(screenOrigin.getX(),screenOrigin.getY());
        return gXform;
    }

    void localTransforms(Transform gXform) {
        gXform.translate(getX(), getY());
        gXform.concatenate(rotation);
        gXform.scale(getScaleX(), getScaleY());
    }

    void postLTransform(Graphics g, Point screenOrigin, Transform gXform) {
        gXform.translate(-screenOrigin.getX(), -screenOrigin.getY());
        g.setTransform(gXform);
    }

    void forwardPrimitiveTranslate(Graphics g, Dimension dimension) {
        Transform gXform = Transform.makeIdentity();
        g.getTransform(gXform);
        gXform.translate(-dimension.getWidth() / 2f,
                         -dimension.getHeight() / 2f);
        g.setTransform(gXform);
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

    void applyTextTransforms(Graphics g, Point containerOrigin, Point screenOrigin) {
        g.setColor(color);
        g.setFont(font);

        restoreOriginalTransforms(g);
        g.setTransform(OrgXFormation);
        Transform gXform = preLTransform(g, screenOrigin);
        gXform.translate(getX(), getY());
        gXform.scale(1, -1);
        postLTransform(g, screenOrigin, gXform);
        forwardPrimitiveTranslate(g, dimensions);
        containerTranslate(g, containerOrigin);
    }

    abstract public void localDraw(Graphics g, Point containerOrigin, Point screenOrigin);

    public void draw(Graphics g, Point containerOrigin, Point screenOrigin) {
        g.setColor(color);

        Transform gXform = preLTransform(g, screenOrigin);
        localTransforms(gXform);
        postLTransform(g, screenOrigin, gXform);
        forwardPrimitiveTranslate(g, dimensions);
        containerTranslate(g, containerOrigin);

        localDraw(g, containerOrigin, screenOrigin);
        restoreOriginalTransforms(g);
    }
}