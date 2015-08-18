package net.jgl2d.math;

import com.jogamp.opengl.GL2;
import net.jgl2d.Camera;
import net.jgl2d.util.Mathf;
import net.jgl2d.util.QuickDraw;
import net.jgl2d.util.Triplet;

//import com.jogamp.opengl.GL;
//import com.jogamp.opengl.GL2;

/**
 * Created by Peter on 05.08.2015.
 */
public class Ray {
    public Vector origin, direction;
    public float[] debugColor = new float[] {1, 0.3f, 0.3f};

    public Ray(Vector origin, Vector direction) {
        this.origin = origin.clone();
        this.direction = direction.clone().normalize();
        if(this.direction.sqrMagnitude() == 0) {
            this.direction = new Vector(0,1);
        }
    }

    public Vector eval(float scalar) {
        return origin.clone().add(direction.clone().multiply(scalar, scalar));
    }

    public float xOf(float y) {
        float s = (y - origin.y) / direction.y;
        return origin.x + s * direction.x;
    }

    public float yOf(float x) {
        float s = (x - origin.x) / direction.x;
        return origin.y + s * direction.y;
    }

    public float reverseEval(Vector position) {
        if(position == null) {
            return Float.NaN;
        }
        Vector delta = position.clone().subtract(origin);
        delta.x = Mathf.round(delta.x);
        delta.y = Mathf.round(delta.y);
        float s1 = Mathf.roundDigits(delta.x / direction.x, 3);
        float s2 = Mathf.roundDigits(delta.y / direction.y, 3);
        if(s1 == s2) {
            return s1;
        }
        if(delta.x == 0 && direction.x == 0) {
            return s2;
        }
        if(delta.y == 0 && direction.y == 0) {
            return s1;
        }
        return Float.NaN;
    }

    public float calcRotation() {
        return direction.getAngle();
    }

    /**
     * Calculates the intersection of this Ray and another.
     * @param other The second Ray.
     * @return A Triplet containing the intersection, the own scalar value of it and the scalar value of the other ray.
     */
    public Triplet<Vector, Float, Float> intersect(Ray other) {
        //TODO: Some bs is hiding here.
        float rot = calcRotation();
        Vector localOrigin = other.origin.clone().subtract(origin); // TOWARDS OTHER
        localOrigin.rotate(rot);
        Vector localDir = other.direction.clone().rotate(rot);
        Vector relative;
        if(localDir.x == 0) { // Parallel to y-Axis -> Parallel to this.
            return null;
        }

        if(localDir.y == 0) { // Orthogonal to y-Axis -> Orth to this.
            relative = new Vector(0, localOrigin.y);
        } else {
            localDir.divide(localDir.x); // localDir.x = 1
            relative = new Vector(0, localOrigin.y + (-localOrigin.x * localDir.y));
        }
        float ownScal = relative.magnitude() / direction.magnitude();
        if(relative.y < 0) ownScal *= -1;
        float otherScal = Vector.difference(relative, localOrigin).magnitude() / other.direction.magnitude();
        if((localOrigin.x > 0 && localDir.x > 0) || (localOrigin.x < 0 && localDir.x < 0)) {
            otherScal *= -1;
        }
        relative.rotate(-rot); // Delocalize
        Vector hit = relative.add(origin);
        /*if(ownScal >= 0 && ownScal <= 1 && otherScal > 0) {
            QuickDraw.cross(Camera.getGL(), hit, new float[]{0,1,0});
        } else if(otherScal < 0) {
            QuickDraw.cross(Camera.getGL(), hit, new float[]{1,0,0});
        }*/
        return new Triplet<>(hit, ownScal, otherScal);
    }

    public void visualize(GL2 gl) {
        if(gl == null) {
            return;
        }
        float[] soft = new float[] {debugColor[0], debugColor[1], debugColor[2], 0.2f};
        Vector dispOrigin = Camera.main().localize(origin);
        Vector dispEnd = Camera.main().localize(eval(1));
        Vector dispL = Camera.main().localize(eval(100));
        Vector dispR = Camera.main().localize(eval(-100));
        Vector tipL = Camera.main().localize(direction.clone().multiply(-0.15f).rotate(20).add(origin).add(direction));
        Vector tipR = Camera.main().localize(direction.clone().multiply(-0.15f).rotate(-20).add(origin).add(direction));

        QuickDraw.line(gl, dispOrigin, dispEnd, debugColor);
        QuickDraw.line(gl, dispEnd, tipL, debugColor);
        QuickDraw.line(gl, dispEnd, tipR, debugColor);
        QuickDraw.line(gl, dispEnd, dispL, soft);
        QuickDraw.line(gl, dispR, dispOrigin, soft);
    }
}
