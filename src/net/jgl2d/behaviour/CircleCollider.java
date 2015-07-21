package net.jgl2d.behaviour;

import com.jogamp.opengl.GLAutoDrawable;
import net.jgl2d.Camera;
import net.jgl2d.math.Vector;
import net.jgl2d.transform.Transform;
import net.jgl2d.util.QuickDraw;


/**
 * Created by peter on 7/19/15.
 */
public class CircleCollider extends Behaviour {
    public float radius = 0.5f;
    public Vector offset = new Vector(0,0);
    public CircleCollider(Transform transform) {
        super(transform);
    }

    @Override
    public void update(GLAutoDrawable drawable) {
        Vector scale = Camera.main().getHalfsize();
        scale.x = 1 / scale.x;
        scale.y = 1 / scale.y;

        Vector center = Camera.main().localize(transform.position.clone().add(offset));
        if(Camera.main().debug()) {
            QuickDraw.circle(drawable.getGL().getGL2(), center, scale, radius, 0.4f, new float[] {0, 1, 0});
        }
    }
}
