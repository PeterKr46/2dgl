package net.jgl2d;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;
import net.jgl2d.input.Input;
import net.jgl2d.math.Vector;
import net.jgl2d.sprite.SpriteRenderer;
import net.jgl2d.transform.Transform;

import java.util.List;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by peter on 7/18/15.
 */
public class Camera implements GLEventListener {

    private static Camera main = null;

    public static Camera main() {
        return main;
    }

    private Frame windowFrame;
    private FPSAnimator animator;

    private GLCanvas canvas;
    private GLProfile glProfile;
    private GLCapabilities glCapabilities;

    private boolean debuggingEnabled = true;

    public Camera() {
        if(main == null) {
            main = this;
        }

        glProfile = GLProfile.getDefault();
        glCapabilities = new GLCapabilities(glProfile);
        canvas = new GLCanvas(glCapabilities);

        canvas.addGLEventListener(this);

        animator = new FPSAnimator(canvas, 60);
        animator.start();

        windowFrame = new Frame("jgl2d");
        windowFrame.setSize(800, 480);
        windowFrame.add(canvas);
        windowFrame.setVisible(true);
        windowFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        Input input = new Input();
        canvas.addMouseMotionListener(input);
        canvas.addMouseListener(input);
        canvas.addKeyListener(input);
        canvas.addMouseWheelListener(input);
    }

    private Vector position = new Vector(0,0);
    private float aspectRatio;
    private float verticalSize = 5;

    public int getScreenHeight() {
        return canvas.getHeight();
    }
    public int getScreenWidth() {
        return canvas.getWidth();
    }

    public Vector getHalfsize() {
        return new Vector(verticalSize * aspectRatio, verticalSize);
    }

    public Vector getMin() {
        return getPosition().add(getHalfsize().multiply(-1, -1));
    }
    public Vector getMax() {
        return getPosition().add(getHalfsize());
    }

    public boolean canSee(SpriteRenderer spriteRenderer) {
        return spriteRenderer.isEnabled(); //TODO
    }

    public Vector worldToScreenPos(Vector worldPos) {
        Vector diff = Vector.difference(position.clone().add(getHalfsize().divide(-1)), worldPos);
        diff.setX(diff.x / verticalSize * aspectRatio);
        diff.setY(diff.y / verticalSize);
        diff.add(-0.5, -0.5);
        return diff;
    }

    /**
     * @param screenPos Vector 0-1 on both axes
     */
    public Vector screenToWorldPos(Vector screenPos) {
        screenPos = screenPos.clone().add(-0.5, -0.5).multiply(2,2);
        screenPos.multiply(getHalfsize());
        screenPos.add(position);
        return screenPos;
    }

    public Vector localize(Vector worldPos) {
        Vector diff = Vector.difference(position.clone(), worldPos);
        diff.setX(diff.x / (verticalSize * aspectRatio));
        diff.setY(diff.y / verticalSize);
        return diff;
    }

    @Override
    public void init(GLAutoDrawable drawable) {

    }

    @Override
    public void dispose(GLAutoDrawable drawable) {

    }

    @Override
    public void display(GLAutoDrawable drawable) {
        render(drawable);
    }



    private void drawGrid(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glColor3f(0.15f, 0.15f, 0.15f);
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2f(-1, -1);
        gl.glVertex2f(1, -1);
        gl.glVertex2f(1, 1);
        gl.glVertex2f(-1, 1);
        gl.glEnd();
        gl.glColor3f(1,1,1);
        gl.glBegin(GL.GL_LINES);
        Vector min = getMin();
        Vector max = getMax();
        for(int i = Math.round(min.x); i < max.x; i++) {
            float color = 0.2f;
            if( i % 10 == 0) {
                color = 0.4f;
            }
            gl.glColor3f(color, color, color);
            Vector x = localize(new Vector(i,0));
            gl.glVertex2f(x.x, -1);
            gl.glVertex2f(x.x, 1);
        }
        for(int i = Math.round(min.y); i < max.y; i++) {
            float color = 0.2f;
            if( i % 10 == 0) {
                color = 0.4f;
            }
            gl.glColor3f(color, color, color);
            Vector y = localize(new Vector(0,i));
            gl.glVertex2f(-1, y.y);
            gl.glVertex2f(1, y.y);
        }
        gl.glEnd();
        gl.glColor3f(1,1,1);
    }

    private void render(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
        drawGrid(drawable);
        List<Transform> transforms = Transform.getAllTransforms();
        for(Transform transform : transforms) {
            transform.update(drawable);
        }

        for(Transform transform : transforms) {
            transform.updateBehaviours(drawable);
        }
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        aspectRatio = ((float) width) / height;
    }

    public float getVerticalSize() {
        return verticalSize;
    }


    public Vector getPosition() {
        return position.clone();
    }

    public boolean debug() {
        return debuggingEnabled;
    }

    public void setDebuggingEnabled(boolean enabled) {
        debuggingEnabled = enabled;
    }

    public float getAspectRatio() {
        return aspectRatio;
    }

    public void toggleDebugging() {
        debuggingEnabled = !debuggingEnabled;
    }

    public void setVerticalSize(float verticalSize) {
        this.verticalSize = verticalSize;
    }

    public void setPosition(Vector position) {
        this.position = position.clone();
    }

    public float getHorizontalSize() {
        return verticalSize * aspectRatio;
    }
}
