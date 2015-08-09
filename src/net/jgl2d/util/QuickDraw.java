package net.jgl2d.util;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

import net.jgl2d.Camera;
import net.jgl2d.math.Vector;


/**
 * Created by peter on 7/19/15.
 */
public class QuickDraw {
    public static void line(GL2 gl, Vector from, Vector to) {
        gl.glBegin(GL.GL_LINES);
        gl.glVertex2f(from.x, from.y);
        gl.glVertex2f(to.x, to.y);
        gl.glEnd();
    }

    public static void line(GL2 gl, Vector from, Vector to, float[] rgb) {
        if(rgb.length == 3) {
            gl.glColor3f(rgb[0], rgb[1], rgb[2]);
        } else if(rgb.length == 4) {
            gl.glColor4f(rgb[0], rgb[1], rgb[2], rgb[3]);
        }
        line(gl, from, to);
        gl.glColor3f(1,1,1);
    }

    public static void circle(GL2 gl, Vector center, Vector scale, float radius, float quality) {
        center = center.toFixed();
        Vector right = new Vector(radius, 0).toFixed();

        Vector tmp, last = center.add(right.multiply(scale));

        gl.glBegin(GL.GL_LINES);

        for(float i = 0; i <= 360; i += 1/quality) {
            tmp = center.add(right.rotate(i).multiply(scale));
            gl.glVertex2f(last.x, last.y);
            gl.glVertex2f(tmp.x, tmp.y);
            last = tmp;
        }
        gl.glEnd();
    }

    public static void filledCircle(GL2 gl, Vector center, Vector scale, float radius, float quality) {
        center = center.toFixed();
        Vector right = new Vector(radius, 0).toFixed();

        Vector tmp, last = center.add(right.multiply(scale));

        gl.glBegin(GL.GL_TRIANGLES);

        for(float i = 0; i <= 360; i += 1/quality) {
            tmp = center.add(right.rotate(i).multiply(scale));
            gl.glVertex2f(center.x, center.y);
            gl.glVertex2f(last.x, last.y);
            gl.glVertex2f(tmp.x, tmp.y);
            last = tmp;
        }
        gl.glEnd();
    }

    public static void filledCircle(GL2 gl, Vector center, Vector scale, float radius, float quality, float[] rgb) {
        if(rgb.length == 3) {
            gl.glColor3f(rgb[0], rgb[1], rgb[2]);
        } else if(rgb.length == 4) {
            gl.glColor4f(rgb[0], rgb[1], rgb[2], rgb[3]);
        }
        filledCircle(gl, center, scale, radius, quality);
        gl.glColor3f(1,1,1);
    }

    public static void circle(GL2 gl, Vector center, Vector scale, float radius, float quality, float[] rgb) {
        if(rgb.length == 3) {
            gl.glColor3f(rgb[0], rgb[1], rgb[2]);
        } else if(rgb.length == 4) {
            gl.glColor4f(rgb[0], rgb[1], rgb[2], rgb[3]);
        }
        circle(gl, center, scale, radius, quality);
        gl.glColor3f(1,1,1);
    }

    public static void circleCutout(GL2 gl, Vector center, Vector scale, float radius, float quality, float minR, float maxR) {
        center = center.toFixed();
        Vector right = new Vector(radius, 0).toFixed();

        Vector tmp, last = center.add(right.multiply(scale).rotate(minR));

        gl.glBegin(GL.GL_LINES);

        for(float i = minR; i <= maxR; i += 1/quality) {
            tmp = center.add(right.rotate(i).multiply(scale));
            gl.glVertex2f(last.x, last.y);
            gl.glVertex2f(tmp.x, tmp.y);
            last = tmp;
        }
        gl.glEnd();
    }

    public static void circleCutout(GL2 gl, Vector center, Vector scale, float radius, float quality, float minR, float maxR, float[] rgb) {
        if(rgb.length == 3) {
            gl.glColor3f(rgb[0], rgb[1], rgb[2]);
        } else if(rgb.length == 4) {
            gl.glColor4f(rgb[0], rgb[1], rgb[2], rgb[3]);
        }
        circleCutout(gl, center, scale, radius, quality, minR, maxR);
        gl.glColor3f(1,1,1);
    }

    public static void cross(GL2 gl, Vector center, float size) {
        center = center.toFixed();
        QuickDraw.line(gl, Camera.main().localize(center.add(-0.1,-0.1)), Camera.main().localize(center.add(0.1,0.1)));
        QuickDraw.line(gl, Camera.main().localize(center.add(0.1, -0.1)), Camera.main().localize(center.add(-0.1, 0.1)));
    }

    public static void filledQuad(GL2 gl, Vector a, Vector b, Vector c, Vector d, float[] rgb) {
        if(rgb.length == 3) {
            gl.glColor3f(rgb[0], rgb[1], rgb[2]);
        } else if(rgb.length == 4) {
            gl.glColor4f(rgb[0], rgb[1], rgb[2], rgb[3]);
        }
        filledQuad(gl, a, b, c, d);
        gl.glColor3f(1,1,1);
    }

    public static void filledQuad(GL2 gl, Vector a, Vector b, Vector c, Vector d) {
        gl.glBegin(GL2.GL_QUADS);

        gl.glVertex2f(a.x, a.y);

        gl.glVertex2f(b.x, b.y);

        gl.glVertex2f(c.x, c.y);

        gl.glVertex2f(d.x, d.y);

        gl.glEnd();

    }
}
