package net.jgl2d.math.area;

import net.jgl2d.math.Ray;
import net.jgl2d.math.Vector;
import net.jgl2d.util.Pair;

import javax.media.opengl.GL2;

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
    public Pair<Vector, Float> cast(Ray ray, GL2 debug) {
        Pair<Vector, Float> hit = null;
        for(Area area : parts) {
            Pair<Vector, Float> tHit = area.cast(ray, debug);
            if(tHit != null && (hit == null || hit.b > tHit.b)) {
                hit = tHit;
            }
        }
        return hit;
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
