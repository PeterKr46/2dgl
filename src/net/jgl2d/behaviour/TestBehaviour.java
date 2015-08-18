package net.jgl2d.behaviour;

import com.jogamp.opengl.GLAutoDrawable;
import net.jgl2d.behaviour.collider.Collider;
import net.jgl2d.math.Ray;
import net.jgl2d.sprite.texture.font.Font;
import net.jgl2d.transform.Transform;

/**
 * Created by peter on 7/19/15.
 */
public class TestBehaviour extends Behaviour {

    public Font font;

    Ray one, two;
    CharacterController contrl;

    public TestBehaviour(Transform transform) {
        super(transform);
    }

    @Override
    public void update(GLAutoDrawable drawable) {
        /*if(font != null) {
            font.write(drawable.getGL().getGL2(), transform.position, "FPS " + Math.round(1f/Camera.deltaTime()), 0.5f, transform.rotation, new float[] {0,0,0, 0.8f});
        }*/
    }

    @Override
    public void onCollide(Collider collider) {
        //Debug.log(transform  + " collided with " + collider.transform + "!");
    }
}
