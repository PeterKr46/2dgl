package net.jgl2d.behaviour.collider;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import net.jgl2d.Camera;
import net.jgl2d.math.Vector;
import net.jgl2d.math.area.CombinedArea;
import net.jgl2d.math.area.LineArea;
import net.jgl2d.transform.Transform;

/**
 * Created by peter on 7/19/15.
 */
public class BoxCollider extends Collider {
    public Vector offset = new Vector(0,0), size = new Vector(1,1);
    public BoxCollider(Transform transform) {
        super(transform);
    }

    @Override
    public void draw(GLAutoDrawable drawable) {
        if(!Camera.main().debug()) {
            return;
        }
        GL2 gl = drawable.getGL().getGL2();
        Vector scale = transform.scale.clone().multiply(size);
        float rotation = transform.rotation;
        Vector position = transform.position;

        Vector bl = position.add(offset.clone().rotate(rotation)).toFixed();
        Vector tl = bl.add(Vector.up.multiply(scale).rotate(rotation)).toFixed();
        Vector tr = tl.add(Vector.right.multiply(scale).rotate(rotation));
        Vector br = bl.add(Vector.right.multiply(scale).rotate(rotation));
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

    public CombinedArea toArea() {
        Vector scale = transform.scale.clone().multiply(size);
        float rotation = transform.rotation;
        Vector u = new Vector(0, size.y * scale.y).rotate(rotation);
        Vector r = new Vector(size.x * scale.x, 0).rotate(rotation);
        Vector position = transform.position.clone().add(offset.clone().rotate(rotation)).toFixed();
        LineArea left = new LineArea(position, u);
        LineArea right = new LineArea(position.add(r), u);
        LineArea top = new LineArea(position.add(u), r);
        LineArea bot = new LineArea(position, r);
        return new CombinedArea(left, right, top, bot);
    }
}
