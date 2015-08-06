package net.jgl2d.behaviour;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import net.jgl2d.Camera;
import net.jgl2d.behaviour.collider.Collider;
import net.jgl2d.math.Vector;
import net.jgl2d.math.area.Area;
import net.jgl2d.math.area.CircleArea;
import net.jgl2d.math.area.RectArea;
import net.jgl2d.transform.Transform;
import net.jgl2d.util.QuickDraw;

import java.util.Stack;

/**
 * Created by Peter on 28.07.2015.
 */
public class CharacterController extends Behaviour {
    private float height = 1, radius = 0.25f;
    public Vector offset;

    public CharacterController(Transform transform) {
        super(transform);
    }

    private boolean grounded = false, wallHit = false, headHit = false;

    @Override
    public void update(GLAutoDrawable drawable) {
        grounded = false;
        wallHit = false;
        headHit = false;
        Area head = getHeadArea();
        Area body = getBodyArea();
        Area feet = getFeetArea();
        for (Collider coll : Collider.getColliders()) {
            boolean hit = false;
            if (coll.toArea().overlaps(head)) {
                headHit = true;
                hit = true;
            }
            if (coll.toArea().overlaps(body)) {
                wallHit = true;
                hit = true;
            }
            if (coll.toArea().overlaps(feet)) {
                grounded = true;
                hit = true;
            }

            if(hit) {
                transform.sendMessage("onCollide", new Class<?>[]{Collider.class}, new Object[]{coll});
            }
        }
        draw(drawable);
    }

    public void translate(Vector delta) {
        //TODO: Absolutely horrific implementation.
        Vector origin = transform.position.toFixed();
        Vector direction = delta.clone().normalize().divide(100);
        Vector mv = new Vector(0,0);
        boolean collided = false;
        while(!collided && mv.sqrMagnitude() <= delta.sqrMagnitude()) {
            transform.position = origin.add(mv);
            collided = false;
            Area head = getHeadArea();
            Area body = getBodyArea();
            Area feet = getFeetArea();
            for (Collider coll : Collider.getColliders()) {
                if (coll.toArea().overlaps(head)) {
                    collided = true;
                }
                if (coll.toArea().overlaps(body)) {
                    collided = true;
                }
                if (coll.toArea().overlaps(feet)) {
                    collided = true;
                }
            }
            mv.add(direction);
        }
        transform.position = origin.add(mv);
    }

    private Vector getUpperCenter() {
        return transform.position.clone().add(offset).add(0,(height-2*radius)/2);
    }

    private Vector getLowerCenter() {
        return transform.position.clone().add(offset).add(0,-(height-2*radius)/2);
    }

    protected void draw(GLAutoDrawable drawable) {
        if(Camera.main().debug()) {
            float[] color = new float[] {0,1,0,0.5f};
            GL2 gl = drawable.getGL().getGL2();

            Vector scale = Camera.main().getHalfsize();
            scale.x = 1 / scale.x;
            scale.y = 1 / scale.y;
            Vector upperCenter = Camera.main().localize(getUpperCenter()).toFixed();
            Vector lowerCenter = Camera.main().localize(getLowerCenter()).toFixed();

            Vector right = new Vector(radius,0).multiply(scale);
            Vector left = new Vector(-radius,0).multiply(scale);


            QuickDraw.circleCutout(gl, upperCenter, scale, radius, 0.5f, 0, 180, color);
            QuickDraw.circleCutout(gl, lowerCenter, scale, radius, 0.5f, 180, 360, color);
            QuickDraw.line(gl, upperCenter.add(right), upperCenter.add(left), color);
            QuickDraw.line(gl, lowerCenter.add(right), lowerCenter.add(left), color);

            QuickDraw.line(gl, upperCenter.add(right), lowerCenter.add(right), color);
            QuickDraw.line(gl, upperCenter.add(left), lowerCenter.add(left), color);
        }
    }


    public Area getFeetArea() {
        return new CircleArea(getLowerCenter(), radius);
    }
    public Area getHeadArea() {
        return new CircleArea(getUpperCenter(), radius);
    }

    public Area getBodyArea() {
        return new RectArea(getLowerCenter().add(-radius,0), 0, radius * 2, height - radius * 2);
    }

    public boolean isGrounded() {
        return grounded;
    }

    public boolean isWallHit() {
        return wallHit;
    }

    public boolean isHeadHit() {
        return headHit;
    }
}
