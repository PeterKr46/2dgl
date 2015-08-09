package net.jgl2d.math.area;


import net.jgl2d.math.Ray;
import net.jgl2d.math.Vector;
import net.jgl2d.util.Pair;
import sun.org.mozilla.javascript.tools.shell.Global;

import javax.media.opengl.GL2;

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
    public Pair<Vector, Float> cast(Ray ray, GL2 debug) {
        Ray counter = new Ray(center, ray.direction.getOrth());
        /*counter.debugColor = new float[]{1,1,1};
        counter.visualize(debug);*/
        Vector s;
        try {
            s = ray.intersect(counter).a; // Intersection ray x orth
        } catch(NullPointerException e) { // The Ray is pointed the opposite direction, NO overlap.
            return null;
        }
        Vector b = s.clone().subtract(center); // Difference center -> s
        if(b.sqrMagnitude() > radius * radius) { // Ray completely misses the circle.
            return null;
        }
        // Reverse Direction of the ray, magnitude = sqrt(r² - |b|²)
        Vector a = ray.direction.clone().multiply(-1).normalize();
        a.multiply(Math.sqrt(radius * radius - b.sqrMagnitude()));
        Vector pos = center.clone().add(b).add(a);
        float scalar = ray.reverseEval(pos); //pos.clone().subtract(ray.origin).magnitude() / ray.direction.magnitude();
        if(scalar < 0) {
            return null;
        }
        return new Pair<>(pos, scalar);
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
        if(area instanceof GlobalFloorArea) {
            return ((GlobalFloorArea) area).yPos >= center.y - radius;
        }
        return false;
    }
}
