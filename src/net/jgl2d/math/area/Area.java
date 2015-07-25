package net.jgl2d.math.area;

import net.jgl2d.math.Vector;

/**
 * Created by Peter on 26.07.2015.
 */
public interface Area {

    public abstract boolean contains(Vector point);

    public abstract boolean overlaps(Area other);
}
