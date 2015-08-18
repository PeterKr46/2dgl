package net.jgl2d.behaviour;

import com.jogamp.opengl.GLAutoDrawable;
import net.jgl2d.behaviour.collider.Collider;
import net.jgl2d.transform.Transform;

/**
 * Created by peter on 7/19/15.
 */
public abstract class Behaviour {

    protected Transform transform;

    public Behaviour(Transform transform) {
        this.transform = transform;
    }

    public final Transform getTransform() {
        return transform;
    }

    public abstract void update(GLAutoDrawable drawable);

    public void start() {

    }

    public void onCollide(Collider collider) {

    }

}
