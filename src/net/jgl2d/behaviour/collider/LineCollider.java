package net.jgl2d.behaviour.collider;

import com.jogamp.opengl.GLAutoDrawable;
import net.jgl2d.Camera;
import net.jgl2d.math.Vector;
import net.jgl2d.math.area.Area;
import net.jgl2d.math.area.LineArea;
import net.jgl2d.transform.Transform;
import net.jgl2d.util.QuickDraw;

/**
 * Created by peter on 8/9/15.
 */
public class LineCollider extends Collider {
    public Vector offset = new Vector(0,1), length = new Vector(1,0);

    public LineCollider(Transform transform) {
        super(transform);
    }

    @Override
    public Area toArea() {
        return new LineArea(transform.position.clone().add(offset.clone().rotate(transform.rotation)), length.clone().rotate(transform.rotation));
    }

    @Override
    protected void draw(GLAutoDrawable drawable) {
        if(Camera.main().debug()) {
            Vector start = transform.position.clone().add(offset.clone().rotate(transform.rotation));
            Vector end = start.clone().add(length.clone().rotate(transform.rotation));
            QuickDraw.line(drawable.getGL().getGL2(), Camera.main().localize(start), Camera.main().localize(end), new float[]{0,1,0, 0.3f});
        }
    }
}
