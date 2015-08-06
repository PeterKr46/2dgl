package net.jgl2d.math;

/**
 * Created by Peter on 05.08.2015.
 */
public class Ray {
    public Vector origin, direction;

    public Ray(Vector origin, Vector direction) {
        this.origin = origin;
        this.direction = direction;
        if(this.direction.sqrMagnitude() == 0) {
            this.direction = new Vector(0,1);
        }
    }

    public Vector eval(float scalar) {
        return origin.clone().add(direction.clone().multiply(scalar, scalar));
    }

}
