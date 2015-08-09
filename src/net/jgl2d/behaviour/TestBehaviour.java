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
import net.jgl2d.util.Triplet;

import javax.swing.*;

/**
 * Created by peter on 7/19/15.
 */
public class TestBehaviour extends Behaviour {

    public Font font;

    CharacterController contrl;
    Ray ray;

    public TestBehaviour(Transform transform) {
        super(transform);
        transform.rotation = 45;
        ray = new Ray(transform.position, new Vector(0,-1));
        ray.debugColor = new float[] {0,1,0.5f};
    }

    @Override
    public void update(GLAutoDrawable drawable) {
        if(font != null) {
            font.write(drawable.getGL().getGL2(), transform.position, "FPS " + Math.round(1f/Camera.deltaTime()), 0.5f, transform.rotation, new float[] {0,0,0, 0.8f});
        }
        /*ray.visualize(drawable);
        CircleCollider coll = (CircleCollider) transform.getBehaviour(CircleCollider.class);
        Pair<Vector, Float> point = coll.toArea().cast(ray, drawable);
        if(point != null) {
            Debug.log(point.b + " ~ " + ray.reverseEval(point.a));
            point.a = point.a.toFixed();
            QuickDraw.line(drawable.getGL().getGL2(), Camera.main().localize(point.a.add(-0.1,-0.1)), Camera.main().localize(point.a.add(0.1,0.1)));
            QuickDraw.line(drawable.getGL().getGL2(), Camera.main().localize(point.a.add(0.1, -0.1)), Camera.main().localize(point.a.add(-0.1, 0.1)));
        }*/
    }

    @Override
    public void onCollide(Collider collider) {
        //Debug.log(transform  + " collided with " + collider.transform + "!");
    }
}
