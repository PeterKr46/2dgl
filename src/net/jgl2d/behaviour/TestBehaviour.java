package net.jgl2d.behaviour;

import net.jgl2d.Camera;
import net.jgl2d.math.Vector;
import net.jgl2d.sprite.texture.font.Font;
import net.jgl2d.transform.Transform;
import net.jgl2d.util.QuickDraw;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

/**
 * Created by peter on 7/19/15.
 */
public class TestBehaviour extends Behaviour {

    public Font font;

    public TestBehaviour(Transform transform) {
        super(transform);
    }

    @Override
    public void update(GLAutoDrawable drawable) {
        transform.rotation += 1;
        if(font != null) {
            font.write(drawable.getGL().getGL2(), transform.position, "Text - fonts and stuff!", 0.5f, transform.rotation, new float[] {0,0,0, 0.8f});
        }

        GL2 gl = drawable.getGL().getGL2();
        Camera cam = Camera.main();
        Vector center = new Vector(3,3).toFixed();
        float rot = 123.5f;
        Vector a = cam.localize(center);
        Vector b = cam.localize(center.add(new Vector(1,0).rotate(rot)));
        Vector c = cam.localize(center.add(new Vector(0,1).rotate(rot)));
        Vector d = cam.localize(center.add(new Vector(1,1).rotate(rot)));

        QuickDraw.line(gl, a, b);
        QuickDraw.line(gl, b, d);
        QuickDraw.line(gl, d, c);
        QuickDraw.line(gl, c, a);
    }
}
