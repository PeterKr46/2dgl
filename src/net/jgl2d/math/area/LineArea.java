package net.jgl2d.math.area;

import net.jgl2d.Camera;
import net.jgl2d.math.Ray;
import net.jgl2d.math.Vector;
import net.jgl2d.sys.Debug;
import net.jgl2d.util.Pair;
import net.jgl2d.util.QuickDraw;
import net.jgl2d.util.Triplet;

import javax.media.opengl.GL2;

/**
 * Created by peter on 8/9/15.
 */
public class LineArea implements Area {
    public Vector origin, length;

    public LineArea(Vector origin, Vector length) {
        this.origin = origin.clone();
        this.length = length.clone();
    }

    @Override
    public boolean contains(Vector point) {
        Vector delta = Vector.difference(origin, point);
        if(!Vector.isParallel(delta, length)) {
            return false;
        }
        if(Vector.isOpposite(delta,length)) {
            return false;
        }
        if(length.sqrMagnitude() < delta.sqrMagnitude()) {
            return false;
        }
        return true;
    }

    @Override
    public Pair<Vector, Float> cast(Ray ray, GL2 debug) {
        Ray own = new Ray(origin,length);
        Triplet<Vector, Float, Float> hit = own.intersect(ray);
        if(hit != null) {
            if(hit.b >= 0 && hit.b <= 1 && (hit.c >= 0)) {
                return new Pair<>(hit.a, hit.c);
            }
        }
        return null;
    }

    // TODO: Either get rid of overlap entirely or find something for this. - nvm, get rid of it.
    @Override
    public boolean overlaps(Area other) {
        return other.contains(origin) || other.contains(origin.clone().add(length));
    }
}
