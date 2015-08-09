package net.jgl2d.math.area;

import net.jgl2d.math.Ray;
import net.jgl2d.math.Vector;
import net.jgl2d.sys.Debug;
import net.jgl2d.util.Pair;
import net.jgl2d.util.Triplet;

import javax.media.opengl.GL2;

/**
 * Created by peter on 8/9/15.
 */
public class GlobalFloorArea implements Area {

    public float yPos = 0;

    public GlobalFloorArea(float yPos) {
        this.yPos = yPos;
    }

    @Override
    public boolean contains(Vector point) {
        return point.y >= yPos;
    }

    @Override
    public Pair<Vector, Float> cast(Ray ray, GL2 debug) {
        float x = ray.xOf(yPos);
        if(x == Float.NaN) return null;
        Vector pos = new Vector(x, yPos);
        return new Pair<>(pos, ray.reverseEval(pos));
    }

    @Override
    public boolean overlaps(Area other) {
        return other.overlaps(this);
    }
}
