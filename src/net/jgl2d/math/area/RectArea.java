package net.jgl2d.math.area;

import com.jogamp.opengl.GL2;
import net.jgl2d.Camera;
import net.jgl2d.math.Vector;
import net.jgl2d.util.QuickDraw;

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
