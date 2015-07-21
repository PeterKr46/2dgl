package net.jgl2d.behaviour;

import com.jogamp.opengl.GLAutoDrawable;
import net.jgl2d.sprite.texture.font.Font;
import net.jgl2d.transform.Transform;

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
    }
}
