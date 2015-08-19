package net.jgl2d.transform;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import net.jgl2d.Camera;
import net.jgl2d.behaviour.Behaviour;
import net.jgl2d.input.Input;
import net.jgl2d.math.Vector;
import net.jgl2d.sprite.SpriteRenderer;
import net.jgl2d.sys.Debug;
import net.jgl2d.util.QuickDraw;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by peter on 7/19/15.
 */
public class Transform {

    private static List<Transform> transforms = new ArrayList<>();

    public static List<Transform> getAllTransforms() {
        return new ArrayList<>(transforms);
    }

    public static Transform createEmpty(String name) {
        return new Transform(name);
    }

    // Public for easy access.
    public String name;
    public Vector position = new Vector(0,0);
    public Vector scale = new Vector(1,1);
    public float rotation = 0;

    private List<Behaviour> behaviours = new ArrayList<>();
    private SpriteRenderer renderer;
    private int ticksLived = 0;

    private Transform(String name) {
        transforms.add(this);
        this.name = name;
    }

    public boolean removeBehaviour(Class<? extends Behaviour> cls) {
        for(Behaviour b : behaviours) {
            if(b.getClass() ==  cls) {
                behaviours.remove(b);
                return true;
            }
        }
        return false;
    }

    public Behaviour addBehaviour(Class<? extends Behaviour> cls) {
        try {
            Behaviour b = cls.getConstructor(Transform.class).newInstance(this);
            behaviours.add(b);
            return b;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
    }

    public SpriteRenderer getRenderer() {
        return renderer;
    }

    public SpriteRenderer addRenderer() {
        if(renderer == null) {
            renderer = new SpriteRenderer(this);
        }
        return renderer;
    }

    //TODO
    public Behaviour getBehaviour(Class<? extends Behaviour> cls) {
        for(Behaviour b : behaviours) {
            if(b.getClass() == cls) {
                return b;
            }
        }
        return null;
    }

    /**
     * Calls a method on all Behaviours attached to this Transform.
     * @param methodName Name of the method to invoke on Behaviours
     * @return true if at least one Behaviour received it.
     */

    public boolean sendMessage(String methodName) {
        boolean ret = false;
        for(Behaviour behaviour : behaviours) {
            try {
                Method method = behaviour.getClass().getMethod(methodName);
                ret = true;
                method.invoke(behaviour);
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }

    /**
     * Calls a method on all Behaviours attached to this Transform.
     * @param methodName The name of the method to invoke on Behaviours
     * @param argTypes An Array of types the method requires as arguments.
     * @param args The values to pass as arguments.
     * @return true if at least one Behaviour received it.
     */

    public boolean sendMessage(String methodName, Class<?>[] argTypes, Object[] args) {
        boolean ret = false;
        for(Behaviour behaviour : behaviours) {
            try {
                Method method = behaviour.getClass().getMethod(methodName, argTypes);
                ret = true;
                method.invoke(behaviour, args);
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }

    public void update(GLAutoDrawable drawable) {
        if(renderer != null && renderer.isEnabled()) {
            renderer.draw(Camera.main(), drawable);
        }
    }

    public void updateBehaviours(GLAutoDrawable drawable) {
        if(ticksLived == 0) {
            for(Behaviour behaviour : behaviours) {
                behaviour.start();
            }
        }
        for(Behaviour behaviour : behaviours) {
            behaviour.update(drawable);
        }
        drawArrows(drawable);
        ticksLived++;
    }

    private void drawArrows(GLAutoDrawable drawable) {
        if(Camera.main().debug()) { //&& Vector.difference(Camera.main().screenToWorldPos(Input.getPercentiveMousePosition()), position).sqrMagnitude() <= 25) {
            GL2 gl = drawable.getGL().getGL2();
            Camera cam = Camera.main();

            float[] red = new float[] {1,0,0};
            float[] green = new float[] {0,1,0};
            float[] yellow = new float[]{1,1,0};

            Vector center = position.toFixed();
            float len = cam.getVerticalSize()/5f;

            Vector right = new Vector(len,0).rotate(rotation).toFixed();
            Vector up = new Vector(0,len).rotate(rotation).toFixed();
            Vector rightEnd = center.add(right).toFixed();
            Vector topEnd = center.add(up).toFixed();

            Vector tipA = new Vector(0,-len/3).rotate(rotation).rotate(25).add(topEnd);
            Vector tipB = new Vector(0,-len/3).rotate(rotation).rotate(-25).add(topEnd);
            Vector tipC = new Vector(-len/3,0).rotate(rotation).rotate(25).add(rightEnd);
            Vector tipD = new Vector(-len/3,0).rotate(rotation).rotate(-25).add(rightEnd);

            center = cam.localize(center);
            rightEnd = cam.localize(rightEnd);
            topEnd = cam.localize(topEnd);
            tipA = cam.localize(tipA);
            tipB = cam.localize(tipB);
            tipC = cam.localize(tipC);
            tipD = cam.localize(tipD);

            QuickDraw.line(gl, center, rightEnd, green);
            QuickDraw.line(gl, center, topEnd, red);

            QuickDraw.line(gl, rightEnd, tipC, green);
            QuickDraw.line(gl, rightEnd, tipD, green);

            QuickDraw.line(gl, topEnd, tipA, red);
            QuickDraw.line(gl, topEnd, tipB, red);

            Vector scale = Camera.main().getHalfsize();
            scale.x = 1 / scale.x;
            scale.y = 1 / scale.y;

            Vector pp = cam.worldToPixelPos(position);
            Vector mp = Input.getMousePosition();
            Vector diff = Vector.difference(pp, mp);
            float rad = 0.12f * len / (cam.getVerticalSize() * 2);
            rad *= cam.getScreenHeight();
            if ((diff.magnitude() < rad && Debug.dragging == null) || Debug.dragging == this) {
                QuickDraw.filledCircle(gl, center, scale, 0.075f * len, 0.1f, red);
                if(Input.isLMouseDown() && (Debug.dragging == null || Debug.dragging == this)) {
                    Debug.dragging = this;
                }
            } else {
                QuickDraw.filledCircle(gl, center, scale, 0.075f * len, 0.1f, yellow);
            }
            if(Debug.dragging == this) {
                position = cam.screenToWorldPos(Input.getPercentiveMousePosition());
                if(Input.isKeyDown('q')) {
                    position = new Vector(Math.round(position.x), (int)position.y);
                }
            }
        }
    }

    public String toString() {
        return "Transform(" + name + ", " + position + ")";
    }
}