package net.jgl2d.transform;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import net.jgl2d.Camera;
import net.jgl2d.behaviour.Behaviour;
import net.jgl2d.math.Vector;
import net.jgl2d.sprite.SpriteRenderer;
import net.jgl2d.util.QuickDraw;

import java.lang.reflect.InvocationTargetException;
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

    private Transform(String name) {
        transforms.add(this);
        this.name = name;
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

    public void update(GLAutoDrawable drawable) {
        if(renderer != null && renderer.isEnabled()) {
            renderer.draw(Camera.main(), drawable);
        }
        drawArrows(drawable);
    }

    public void updateBehaviours(GLAutoDrawable drawable) {
        for(Behaviour behaviour : behaviours) {
            behaviour.update(drawable);
        }
    }

    private void drawArrows(GLAutoDrawable drawable) {
        if(Camera.main().debug()) {
            GL2 gl = drawable.getGL().getGL2();
            Camera cam = Camera.main();

            float[] red = new float[] {1,0,0};
            float[] green = new float[] {0,1,0};

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

            //TODO: Clickboxes & dragging circles
            QuickDraw.filledCircle(gl, center, scale, 0.075f, 0.1f, green);
        }
    }
}