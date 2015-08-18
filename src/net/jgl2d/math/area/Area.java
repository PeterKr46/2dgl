package net.jgl2d.math.area;

import com.jogamp.opengl.GL2;
import net.jgl2d.math.Ray;
import net.jgl2d.math.Vector;
import net.jgl2d.util.Pair;

/**
 * Created by Peter on 26.07.2015.
 */
public interface Area {

    public abstract boolean contains(Vector point);

    public abstract Pair<Vector, Float> cast(Ray ray, GL2 debug);

    public abstract boolean overlaps(Area other);
}
