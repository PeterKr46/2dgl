package net.jgl2d.math.area;

import net.jgl2d.math.Ray;
import net.jgl2d.math.Vector;
import net.jgl2d.util.Pair;

import javax.media.opengl.GLAutoDrawable;

/**
 * Created by Peter on 26.07.2015.
 */
public interface Area {

    public abstract boolean contains(Vector point);

    public abstract Pair<Vector, Float> cast(Ray ray, GLAutoDrawable debug);

    public abstract boolean overlaps(Area other);
}
