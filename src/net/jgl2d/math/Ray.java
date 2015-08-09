package net.jgl2d.math;

import net.jgl2d.Camera;
import net.jgl2d.sys.Debug;
import net.jgl2d.util.Mathf;
import net.jgl2d.util.Triplet;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

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
            relative = new Vector(0, localOrigin.y + localOrigin.x * localDir.y);
        }

        relative.rotate(-rot); // Delocalize
        Vector hit = relative.add(origin);
        float ownScal = reverseEval(hit);//Vector.difference(origin, hit).magnitude() / direction.magnitude();
        float otherScal = other.reverseEval(hit);//Vector.difference(other.origin, hit).magnitude() / other.direction.magnitude();

        return new Triplet<>(hit, ownScal, otherScal);
    }

    public void visualize(GL2 gl) {
        if(gl == null) {
            return;
        }
        gl.glColor3f(debugColor[0], debugColor[1], debugColor[2]);
        gl.glBegin(GL.GL_LINES);

        Vector dispOrigin = Camera.main().localize(origin);
        Vector dispEnd = Camera.main().localize(eval(1));
        Vector dispL = Camera.main().localize(eval(100));
        Vector dispR = Camera.main().localize(eval(-100));
        gl.glVertex2f(dispOrigin.x, dispOrigin.y);
        gl.glColor4f(debugColor[1], debugColor[2], debugColor[0], 0.6f);
        gl.glVertex2f(dispEnd.x, dispEnd.y);

        gl.glColor4f(debugColor[0], debugColor[1], debugColor[2], 0.3f);
        gl.glVertex2f(dispOrigin.x, dispOrigin.y);
        gl.glVertex2f(dispR.x, dispR.y);

        gl.glVertex2f(dispEnd.x, dispEnd.y);
        gl.glVertex2f(dispL.x, dispL.y);
        gl.glEnd();
        gl.glColor3f(1,1,1);
    }
}
