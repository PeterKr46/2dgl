package net.jgl2d.behaviour.collider;

import com.jogamp.opengl.GLAutoDrawable;
import net.jgl2d.Camera;
import net.jgl2d.math.Vector;
import net.jgl2d.math.area.Area;
import net.jgl2d.math.area.CircleArea;
import net.jgl2d.transform.Transform;
import net.jgl2d.util.QuickDraw;


/**
 * Created by peter on 7/19/15.
 */
public class CircleCollider extends Collider {
    public float radius = 0.5f;
    public Vector offset = new Vector(0.5,0.5);
    public CircleCollider(Transform transform) {
        super(transform);
    }

    @Override
    public Area toArea() {
        return new CircleArea(transform.position.clone().add(offset.clone().rotate(transform.rotation)), radius);
    }

    @Override
    public void draw(GLAutoDrawable drawable) {
        if(Camera.main().debug()) {
            Vector scale = Camera.main().getHalfsize();
            scale.x = 1 / scale.x;
            scale.y = 1 / scale.y;

            Vector center = Camera.main().localize(transform.position.clone().add(offset.clone().rotate(transform.rotation)));
            QuickDraw.circle(drawable.getGL().getGL2(), center, scale, radius, 0.4f, new float[] {0, 1, 0});
        }
    }
}
