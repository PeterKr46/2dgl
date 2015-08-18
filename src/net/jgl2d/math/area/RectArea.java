package net.jgl2d.math.area;

import com.jogamp.opengl.GL2;

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
    public Pair<Vector, Float> cast(Ray ray, GL2 debug) {
        Ray lRay = new Ray(ray.origin.clone().subtract(position).rotate(-rotation), ray.direction.clone().rotate(-rotation));
        lRay.visualize(debug);
        Vector right = new Vector(size.x,0);
        Vector up = new Vector(0,size.y);
        Vector topLeft = right.clone().add(up);
        Vector botRight = right.clone();
        Vector botLeft = Vector.zero;

        Ray sRight = new Ray(botRight, up);
        Ray sLeft = new Ray(botLeft, up);
        Ray sBot = new Ray(botLeft, right);
        Ray sTop = new Ray(topLeft, right);
        sRight.visualize(debug);
        sLeft.visualize(debug);
        sBot.visualize(debug);
        sTop.visualize(debug);

        Triplet<Vector, Float, Float> iRight = sRight.intersect(lRay);
        Triplet<Vector, Float, Float> iLeft = sLeft.intersect(lRay);
        Triplet<Vector, Float, Float> iBottom = sBot.intersect(lRay);
        Triplet<Vector, Float, Float> iTop = sTop.intersect(lRay);
        Triplet<Vector, Float, Float> hit = null;
        if(iRight != null && iRight.b >= 0 && iRight.b <= 1) {
            hit = iRight;
        }
        if(iLeft != null && iLeft.b >= 0 && iLeft.b <= 1 && (hit == null || iLeft.c < hit.c)) {
            hit = iLeft;
        }
        if(iBottom != null && iBottom.b >= 0 && iBottom.b <= 1 && (hit == null || iBottom.c < hit.c)) {
            hit = iBottom;
        }
        if(iTop != null && iTop.b >= 0 && iTop.b <= 1 && (hit == null || iTop.c < hit.c)) {
            hit = iTop;
        }
        if(hit != null) {
            Vector pos = hit.a.rotate(rotation).add(position);
            return new Pair<>(pos, hit.c);
        }
        return null;
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
        if(area instanceof GlobalFloorArea) {
            return overlapsCorners(area);
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
