package net.jgl2d.behaviour;

import com.jogamp.opengl.GLAutoDrawable;
import net.jgl2d.Camera;
import net.jgl2d.behaviour.collider.BoxCollider;
import net.jgl2d.behaviour.collider.Collider;
import net.jgl2d.input.Input;
import net.jgl2d.math.Vector;
import net.jgl2d.sprite.texture.font.Font;
import net.jgl2d.sys.Debug;
import net.jgl2d.transform.Transform;

import javax.swing.*;

/**
 * Created by peter on 7/19/15.
 */
public class TestBehaviour extends Behaviour {

    public Font font;

    boolean dir = true;
    CharacterController contrl;
    float velocity = 0;

    public TestBehaviour(Transform transform) {
        super(transform);
    }

    @Override
    public void update(GLAutoDrawable drawable) {
        if(contrl == null) {
            contrl = (CharacterController) transform.getBehaviour(CharacterController.class);
        }
        velocity -= 9.81 / 600;
        if(contrl.isGrounded()) {
            velocity = 0f;
        }
        float speed = 1 * Camera.deltaTime() * (Input.isKeyDown('a') ? -1 : (Input.isKeyDown('d') ? 1 : 0));
        contrl.translate(new Vector(speed,velocity));
        if(font != null) {
            font.write(drawable.getGL().getGL2(), transform.position, "FPS " + Math.round(1f/Camera.deltaTime()), 0.5f, transform.rotation, new float[] {0,0,0, 0.8f});
        }
        Debug.log(transform.position);
    }

    @Override
    public void onCollide(Collider collider) {
        //Debug.log(transform  + " collided with " + collider.transform + "!");
    }
}
