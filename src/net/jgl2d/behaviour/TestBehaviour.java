package net.jgl2d.behaviour;

import net.jgl2d.transform.Transform;

import javax.media.opengl.GLAutoDrawable;

/**
 * Created by peter on 7/19/15.
 */
public class TestBehaviour extends Behaviour {

    public TestBehaviour(Transform transform) {
        super(transform);
    }

    @Override
    public void update(GLAutoDrawable drawable) {
        transform.rotation += 1;
    }
}
