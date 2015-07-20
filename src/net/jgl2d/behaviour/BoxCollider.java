package net.jgl2d.behaviour;

import net.jgl2d.Camera;
import net.jgl2d.math.Vector;
import net.jgl2d.transform.Transform;
import net.jgl2d.util.QuickDraw;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

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
        GL2 gl = drawable.getGL().getGL2();
        Vector scale = transform.scale.clone();
        float rotation = transform.rotation;
        Vector position = transform.position;

        Vector screenScale = Camera.main().getHalfsize();
        screenScale.x = 1 / screenScale.x * (size.x/size.y);
        screenScale.y = 1 / screenScale.y;
        screenScale = screenScale.toFixed();

        Vector scaledOffset = offset.clone().multiply(scale).multiply(-1, -1).rotate(rotation).multiply(screenScale).toFixed();
        Vector screenPos = Camera.main().localize(position).toFixed();

        Vector bl = screenPos.add(scaledOffset);
        Vector tl = bl.clone().add(new Vector(0, scale.y).rotate(rotation).multiply(screenScale));
        Vector tr = bl.clone().add(new Vector(scale.x, scale.y).rotate(rotation).multiply(screenScale));
        Vector br = bl.clone().add(new Vector(scale.x, 0).rotate(rotation).multiply(screenScale));

        if(Camera.main().debug()) {
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
}
