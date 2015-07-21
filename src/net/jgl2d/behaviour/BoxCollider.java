package net.jgl2d.behaviour;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import net.jgl2d.Camera;
import net.jgl2d.math.Vector;
import net.jgl2d.transform.Transform;

/**
 * Created by peter on 7/19/15.
 */
public class BoxCollider extends Behaviour {
    public Vector offset = new Vector(0,0), size = new Vector(1,1);
    public BoxCollider(Transform transform) {
        super(transform);
    }

    @Override
    public void update(GLAutoDrawable drawable) {
        if(!Camera.main().debug()) {
            return;
        }
        GL2 gl = drawable.getGL().getGL2();
        Vector scale = transform.scale.clone();
        float rotation = transform.rotation;
        Vector position = transform.position;

        Vector bl = position.add(offset.clone().rotate(rotation)).toFixed();
        Vector tl = bl.add(Vector.up().multiply(scale).rotate(rotation)).toFixed();
        Vector tr = tl.add(Vector.right().multiply(scale).rotate(rotation));
        Vector br = bl.add(Vector.right().multiply(scale).rotate(rotation));
        bl = Camera.main().localize(bl);
        br = Camera.main().localize(br);
        tl = Camera.main().localize(tl);
        tr = Camera.main().localize(tr);

        gl.glColor4d(0, 1, 0, 0.5);
        gl.glBegin(GL2.GL_LINE_STRIP);
        gl.glVertex2f(bl.x, bl.y);
        gl.glVertex2f(tl.x, tl.y);
        gl.glVertex2f(tr.x, tr.y);
        gl.glVertex2f(br.x, br.y);

        gl.glVertex2f(br.x, br.y);
        gl.glVertex2f(bl.x, bl.y);

        gl.glEnd();
        gl.glColor3f(1,1,1);
    }
}
