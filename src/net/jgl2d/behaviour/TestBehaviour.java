package net.jgl2d.behaviour;

import javax.media.opengl.GLAutoDrawable;
import net.jgl2d.Camera;
import net.jgl2d.behaviour.collider.BoxCollider;
import net.jgl2d.behaviour.collider.CircleCollider;
import net.jgl2d.behaviour.collider.Collider;
import net.jgl2d.input.Input;
import net.jgl2d.math.Ray;
import net.jgl2d.math.Vector;
import net.jgl2d.sprite.texture.font.Font;
import net.jgl2d.sys.Debug;
import net.jgl2d.transform.Transform;
import net.jgl2d.util.Pair;
import net.jgl2d.util.QuickDraw;

import javax.swing.*;

/**
 * Created by peter on 7/19/15.
 */
public class TestBehaviour extends Behaviour {

    public Font font;

    boolean dir = true;
    CharacterController contrl;
    float velocity = 0;
    Ray ray;

    public TestBehaviour(Transform transform) {
        super(transform);
        ray = new Ray(Vector.up(), new Vector(0.3, -0.7));
    }

    @Override
    public void update(GLAutoDrawable drawable) {
        if(contrl == null) {
            contrl = (CharacterController) transform.getBehaviour(CharacterController.class);
        }
        if(contrl != null) {
            velocity -= 9.81 / 600;
            if(contrl.isGrounded()) {
                velocity = 0f;
            }
            float speed = 1 * Camera.deltaTime() * (Input.isKeyDown('a') ? -1 : (Input.isKeyDown('d') ? 1 : 0));
            contrl.translate(new Vector(speed,velocity));
        }
        if(font != null) {
            font.write(drawable.getGL().getGL2(), transform.position, "FPS " + Math.round(1f/Camera.deltaTime()), 0.5f, transform.rotation, new float[] {0,0,0, 0.8f});
        }
        ray.visualize(drawable);
        BoxCollider coll = (BoxCollider) transform.getBehaviour(BoxCollider.class);
        Pair<Vector, Float> point = coll.toArea().cast(ray, drawable);
        if(point != null) {
            Debug.log(point.b + " ~ " + ray.reverseEval(point.a));
            point.a = point.a.toFixed();
            QuickDraw.line(drawable.getGL().getGL2(), Camera.main().localize(point.a.add(-0.1,-0.1)), Camera.main().localize(point.a.add(0.1,0.1)));
            QuickDraw.line(drawable.getGL().getGL2(), Camera.main().localize(point.a.add(0.1, -0.1)), Camera.main().localize(point.a.add(-0.1, 0.1)));
        }
    }

    @Override
    public void onCollide(Collider collider) {
        //Debug.log(transform  + " collided with " + collider.transform + "!");
    }
}
