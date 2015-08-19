package net.jgl2d.behaviour.character;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import net.jgl2d.Camera;
import net.jgl2d.behaviour.Behaviour;
import net.jgl2d.behaviour.physics.Collider;
import net.jgl2d.behaviour.physics.Physics;
import net.jgl2d.input.Input;
import net.jgl2d.math.Ray;
import net.jgl2d.math.Vector;
import net.jgl2d.math.area.Area;
import net.jgl2d.math.area.CircleArea;
import net.jgl2d.math.area.CombinedArea;
import net.jgl2d.math.area.RectArea;
import net.jgl2d.transform.Transform;
import net.jgl2d.util.QuickDraw;
import net.jgl2d.util.Triplet;

/**
 * Created by Peter on 28.07.2015.
 */
public class CharacterController extends Behaviour {
    private float height = 1, radius = 0.25f;
    public Vector offset;
    public Vector gravity = new Vector(0, -13);
    public float currentVertVelocity = 0;

    public CharacterController(Transform transform) {
        super(transform);
    }

    private boolean grounded = false, moving = false;

    @Override
    public void update(GLAutoDrawable drawable) {
        if(!grounded) {
            currentVertVelocity += gravity.y * Camera.deltaTime();
        } else {
            currentVertVelocity = -0.001f;
        }
        if(Input.isKeyDown('w') && grounded) {
            currentVertVelocity = 5f;
        }
        grounded = false;
        if(currentVertVelocity < 0) {
            Ray center = new Ray(getLowerCenter(), new Vector(0,-1));
            Ray right = new Ray(getLowerCenter().add(radius*0.8,0), new Vector(0,-1));
            Ray left = new Ray(getLowerCenter().add(-radius*0.8,0), new Vector(0,-1));
            if(Camera.main().debug()) {
                center.visualize(drawable.getGL().getGL2());
                right.visualize(drawable.getGL().getGL2());
                left.visualize(drawable.getGL().getGL2());
            }
            Triplet<Vector, Collider, Float> hitCenter = Physics.cast(center);
            Triplet<Vector, Collider, Float> hitLeft = Physics.cast(left);
            Triplet<Vector, Collider, Float> hitRight = Physics.cast(right);
            hitCenter = (hitCenter == null) ? new Triplet<Vector, Collider, Float>(null, null, Float.MAX_VALUE) : hitCenter;
            hitLeft = (hitLeft == null) ? new Triplet<Vector, Collider, Float>(null, null, Float.MAX_VALUE) : hitLeft;
            hitRight = (hitRight == null) ? new Triplet<Vector, Collider, Float>(null, null, Float.MAX_VALUE) : hitRight;

            float minY = transform.position.y;
            if(hitCenter.a != null && hitCenter.a.y < minY) {
                minY = hitCenter.a.y;
            }
            if(hitRight.a != null && hitRight.a.y > minY) {
                minY = hitRight.a.y;
            }
            if(hitLeft.a != null && hitLeft.a.y > minY) {
                minY = hitLeft.a.y;
            }
            float currentdelta = minY - transform.position.y;
            float moveDelta = currentVertVelocity * Camera.deltaTime();
            if(currentVertVelocity < 0 && currentdelta == 0) {
                moveDelta = 0;
            }
            if(currentdelta < 0 && moveDelta > currentdelta) {
                transform.position.y += moveDelta;
            } else {
                transform.position.y = minY;
            }
            grounded = hitCenter.c <= radius || hitLeft.c <= radius || hitRight.c <= radius;
        } else if(currentVertVelocity > 0) {
            Ray center = new Ray(getUpperCenter().add(0, radius/4), new Vector(0,1));
            Ray right = new Ray(getUpperCenter().add(radius*0.8, radius/4), new Vector(0,1));
            Ray left = new Ray(getUpperCenter().add(-radius*0.8, radius/4), new Vector(0,1));
            if(Camera.main().debug()) {
                center.visualize(drawable.getGL().getGL2());
                right.visualize(drawable.getGL().getGL2());
                left.visualize(drawable.getGL().getGL2());
            }
            Triplet<Vector, Collider, Float> hitLeft = Physics.cast(left);
            Triplet<Vector, Collider, Float> hitCenter = Physics.cast(center);
            Triplet<Vector, Collider, Float> hitRight = Physics.cast(right);
            hitLeft = (hitLeft == null) ? new Triplet<Vector, Collider, Float>(null, null, Float.MAX_VALUE) : hitLeft;
            hitCenter = (hitCenter == null) ? new Triplet<Vector, Collider, Float>(null, null, Float.MAX_VALUE) : hitCenter;
            hitRight = (hitRight == null) ? new Triplet<Vector, Collider, Float>(null, null, Float.MAX_VALUE) : hitRight;
            float minDelta = currentVertVelocity * Camera.deltaTime();
            minDelta = Math.min(minDelta, hitLeft.c - radius*0.8f);
            minDelta = Math.min(minDelta, hitRight.c - radius*0.8f);
            minDelta = Math.min(minDelta, hitCenter.c - radius*0.8f);
            if(minDelta < currentVertVelocity * Camera.deltaTime()) {
                currentVertVelocity = 0;
            }
            transform.position.y += minDelta;
        }
        float mvX = (Input.isKeyDown('a') ? -1 : Input.isKeyDown('d') ? 1 : 0) * Camera.deltaTime() * 5;
        if(mvX != 0) {
            Ray lower = new Ray(getLowerCenter(), mvX > 0 ? Vector.right : Vector.left);
            Ray upper = new Ray(getUpperCenter().add(0,radius * 0.8), mvX > 0 ? Vector.right : Vector.left);
            Triplet<Vector, Collider, Float> lowerHit = Physics.cast(lower);
            Triplet<Vector, Collider, Float> upperHit = Physics.cast(upper);
            lowerHit = (lowerHit == null) ? new Triplet<Vector, Collider, Float>(null, null, Float.MAX_VALUE) : lowerHit;
            upperHit = (upperHit == null) ? new Triplet<Vector, Collider, Float>(null, null, Float.MAX_VALUE) : upperHit;
            if (lowerHit.c - radius < Math.abs(mvX)) {
                mvX = Math.max(0, lowerHit.c - radius) * (mvX < 0 ? -1 : 1);
            }
            if (upperHit.c - radius < Math.abs(mvX)) {
                mvX = Math.max(0, upperHit.c - radius) * (mvX < 0 ? -1 : 1);
            }
            if(Camera.main().debug()) {
                lower.visualize(drawable.getGL().getGL2());
                upper.visualize(drawable.getGL().getGL2());
            }
        }
        if(mvX != 0) {
            transform.scale.x = (mvX < 0 ? -1 : 1);
        }
        transform.position.x += mvX;
        moving = mvX != 0;
        draw(drawable);
    }

    public Area toArea() {
        return new CombinedArea(getHeadArea(), getBodyArea(), getFeetArea());
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

    public boolean isMoving() {
        return moving;
    }
}
