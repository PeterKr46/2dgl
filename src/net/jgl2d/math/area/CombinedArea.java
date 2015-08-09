package net.jgl2d.math.area;

import net.jgl2d.math.Ray;
import net.jgl2d.math.Vector;
import net.jgl2d.util.Pair;

import javax.media.opengl.GLAutoDrawable;

/**
 * Created by peter on 8/9/15.
 */
public class CombinedArea implements Area {
    public Area[] parts;

    public CombinedArea(Area... parts) {
        this.parts = parts;
    }

    @Override
    public boolean contains(Vector point) {
        for(Area a : parts) {
            if(a.contains(point)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Pair<Vector, Float> cast(Ray ray, GLAutoDrawable debug) {
        return null; //TODO
    }

    @Override
    public boolean overlaps(Area other) {
        for(Area a : parts) {
            if(a.overlaps(other)) {
                return true;
            }
        }
        return false;
    }
}
