package net.jgl2d.math.area;


import net.jgl2d.math.Vector;

/**
 * Created by Peter on 26.07.2015.
 */
public class CircleArea implements Area {

    private float radius;
    private Vector center;

    public CircleArea(Vector center, float radius) {
        this.radius = Math.abs(radius);
        this.center = center.clone();
    }

    @Override
    public boolean contains(Vector point) {
        return Vector.difference(point, center).sqrMagnitude() <= Math.pow(radius, 2);
    }

    @Override
    public boolean overlaps(Area area) {
        if(area instanceof CircleArea) {
            CircleArea other = (CircleArea) area;
            return Vector.difference(other.center, center).magnitude() <= other.radius + radius;
        }
        if(area instanceof RectArea) {
            RectArea other = (RectArea) area;
            if(other.overlapsCorners(this)) {
                return true;
            }
            Vector diff = Vector.difference(other.getMin(), center);
            diff.rotate(-other.getRotation());
            if(diff.x >= -radius && diff.x <= other.getWidth() + radius && diff.y >= 0 && diff.y <= other.getHeight()) {
                return true;
            }
            if(diff.y >= -radius && diff.y <= other.getHeight() + radius && diff.x >= 0 && diff.x <= other.getWidth()) {
                return true;
            }
            return false;
        }
        return false;
    }
}
