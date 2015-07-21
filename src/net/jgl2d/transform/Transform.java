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

            float[] red = new float[] {1,0,0};
            float[] green = new float[] {0,1,0};

            Vector scale = Camera.main().getHalfsize();
            scale.x = 1 / scale.x;
            scale.y = 1 / scale.y;

            Vector center = Camera.main().localize(position).toFixed();

            Vector right = new Vector(0.7,0).rotate(rotation).multiply(scale).toFixed();
            Vector up = new Vector(0,0.7).rotate(rotation).multiply(scale).toFixed();
            Vector rightEnd = center.add(right).toFixed();
            Vector topEnd = center.add(up).toFixed();

            Vector tipA = new Vector(0,-0.25).rotate(rotation).rotate(25).multiply(scale);
            Vector tipB = new Vector(0,-0.25).rotate(rotation).rotate(-25).multiply(scale);
            Vector tipC = new Vector(-0.25,0).rotate(rotation).rotate(25).multiply(scale);
            Vector tipD = new Vector(-0.25,0).rotate(rotation).rotate(-25).multiply(scale);

            QuickDraw.line(gl, center, rightEnd, green);
            QuickDraw.line(gl, center, topEnd, red);

            QuickDraw.line(gl, rightEnd, rightEnd.add(tipC), green);
            QuickDraw.line(gl, rightEnd, rightEnd.add(tipD), green);

            QuickDraw.line(gl, topEnd, topEnd.add(tipA), red);
            QuickDraw.line(gl, topEnd, topEnd.add(tipB), red);

            //TODO: Clickboxes & dragging circles
            QuickDraw.filledCircle(gl, center, scale, 0.075f, 0.1f, green);
        }
    }
}