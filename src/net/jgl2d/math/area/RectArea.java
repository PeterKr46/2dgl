package net.jgl2d.math.area;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

import net.jgl2d.Camera;
import net.jgl2d.math.Ray;
import net.jgl2d.math.Vector;
import net.jgl2d.util.Pair;
import net.jgl2d.util.QuickDraw;
import net.jgl2d.util.Triplet;

/**
 * Created by Peter on 22.07.2015.
 */
public class RectArea implements Area {
    private Vector size;
    private float rotation;
    private Vector position;

    public RectArea(Vector anchor, float rotation, float width, float height) {
        this.position = anchor.clone();
        this.rotation = rotation;
        this.size = new Vector(width, height);
    }

    public boolean contains(Vector position) {
        Vector diff = Vector.difference(this.position, position);
        diff.rotate(-rotation);
        return diff.x >= 0 && diff.x <= size.x && diff.y >= 0 && diff.y <= size.y;
    }

    @Override
    public Pair<Vector, Float> cast(Ray ray, GLAutoDrawable debug) {
        Vector right = new Vector(size.x,0).rotate(-rotation);
        Vector up = new Vector(0,size.y).rotate(-rotation);
        Vector topLeft = position.clone().add(up);
        Vector botRight = position.clone().add(right);
        Vector botLeft = position.clone();
        Ray rRight = new Ray(botRight, up);
        Ray rLeft = new Ray(botLeft, up);
        Ray rTop = new Ray(topLeft, right);
        Ray rBot = new Ray(botLeft, right);
        Triplet<Vector, Float, Float> iBot = rBot.intersect(ray);
        Triplet<Vector, Float, Float> iTop = rTop.intersect(ray);
        Triplet<Vector, Float, Float> iLeft = rLeft.intersect(ray);
        Triplet<Vector, Float, Float> iRight = rRight.intersect(ray);
        Triplet<Vector, Float, Float> closest = null;
        if(iBot != null && iBot.b >= 0 && iBot.b <= 1) {
            closest = iBot;
        }
        if(iTop != null && iTop.b >= 0 && iTop.b <= 1 && (closest == null || closest.c > iTop.c)) {
            closest = iTop;
        }
        if(iLeft != null && iLeft.b >= 0 && iLeft.b <= 1 && (closest == null || closest.c > iLeft.c)) {
            closest = iLeft;
        }
        if(iRight != null && iRight.b >= 0 && iRight.b <= 1 && (closest == null || closest.c > iRight.c)) {
            closest = iRight;
        }
        return closest != null ? new Pair<>(closest.a, closest.c) : null;
    }

    protected boolean overlapsCorners(Area other) {
        Vector rotX = new Vector(size.x,0).rotate(rotation);
        Vector rotY = new Vector(0,size.y).rotate(rotation);
        Vector topRight = position.clone().add(rotX).add(rotY);
        Vector topLeft = position.clone().add(rotY);
        Vector botRight = position.clone().add(rotX);
        Vector botLeft = position.clone();
        return other.contains(botLeft) || other.contains(botRight) || other.contains(topLeft) || other.contains(topRight);
    }

    public void debug(GL2 gl, float[] color) {
        Vector rotX = new Vector(size.x,0).rotate(rotation);
        Vector rotY = new Vector(0,size.y).rotate(rotation);
        Vector topRight = Camera.main().localize(position.clone().add(rotX).add(rotY));
        Vector topLeft = Camera.main().localize(position.clone().add(rotY));
        Vector botRight = Camera.main().localize(position.clone().add(rotX));
        Vector botLeft = Camera.main().localize(position.clone());
        QuickDraw.line(gl, topLeft, topRight, color);
        QuickDraw.line(gl, topRight, botRight, color);
        QuickDraw.line(gl, botLeft, botRight, color);
        QuickDraw.line(gl, botLeft, topLeft, color);
    }

    public boolean overlaps(Area area) {
        if(area instanceof RectArea) {
            RectArea other = (RectArea) area;
            if (Vector.difference(other.getCenter(), getCenter()).sqrMagnitude() > size.clone().divide(2).add(other.size.clone().divide(2)).sqrMagnitude()) {
                return false;
            }
            return overlapsCorners(other) || other.overlapsCorners(this);
        }

        if(area instanceof CircleArea) {
            return area.overlaps(this);
        }

        return false;
    }

    public Vector getCenter() {
        return position.clone().add(size.clone().divide(2).rotate(rotation));
    }

    public Vector getMin() {
        return position.clone();
    }

    public float getRotation() {
        return rotation;
    }

    public float getWidth() {
        return size.x;
    }

    public float getHeight() {
        return size.y;
    }
}
