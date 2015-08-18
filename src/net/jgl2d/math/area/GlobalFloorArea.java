package net.jgl2d.math.area;

import com.jogamp.opengl.GL2;
import net.jgl2d.math.Ray;
import net.jgl2d.math.Vector;
import net.jgl2d.util.Pair;

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
        float scal = ray.reverseEval(pos);
        return scal > 0 ? new Pair<>(pos, ray.reverseEval(pos)) : null;
    }

    @Override
    public boolean overlaps(Area other) {
        return other.overlaps(this);
    }
}
