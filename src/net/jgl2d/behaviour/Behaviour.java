package net.jgl2d.behaviour;

import net.jgl2d.transform.Transform;

import javax.media.opengl.GLAutoDrawable;

/**
 * Created by peter on 7/19/15.
 */
public abstract class Behaviour {

    protected Transform transform;

    public Behaviour(Transform transform) {
        this.transform = transform;
    }

    public abstract void update(GLAutoDrawable drawable);

    public void start() {

    }


}
