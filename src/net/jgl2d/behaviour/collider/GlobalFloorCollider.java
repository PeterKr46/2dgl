package net.jgl2d.behaviour.collider;

import com.jogamp.opengl.GLAutoDrawable;
import net.jgl2d.Camera;
import net.jgl2d.math.Vector;
import net.jgl2d.math.area.Area;
import net.jgl2d.math.area.GlobalFloorArea;
import net.jgl2d.transform.Transform;
import net.jgl2d.util.QuickDraw;

/**
 * Created by peter on 8/9/15.
 */
public class GlobalFloorCollider extends Collider{
    public GlobalFloorCollider(Transform transform) {
        super(transform);
    }

    public float yPos = 0;

    @Override
    public Area toArea() {
        return new GlobalFloorArea(yPos);
    }

    @Override
    protected void draw(GLAutoDrawable drawable) {
        if(!Camera.main().debug()) {
            return;
        }
        Vector min = Camera.main().getMin();
        Vector max = Camera.main().getMax();
        if(min.y <= yPos) {
            Vector uL = Camera.main().localize(new Vector(min.x, yPos));
            Vector uR = Camera.main().localize(new Vector(max.x, yPos));
            Vector bL = Camera.main().localize(min);
            Vector bR = Camera.main().localize(new Vector(max.x, min.y));
            if(max.y >= yPos) {
                QuickDraw.line(drawable.getGL().getGL2(), uL, uR, new float[]{0,1,0,0.8f});
            }
            QuickDraw.filledQuad(drawable.getGL().getGL2(), uL, uR, bR, bL, new float[] {0,1,0,0.4f});
        }
    }
}
