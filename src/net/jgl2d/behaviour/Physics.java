package net.jgl2d.behaviour;

import net.jgl2d.Camera;
import net.jgl2d.behaviour.collider.Collider;
import net.jgl2d.math.Ray;
import net.jgl2d.math.Vector;
import net.jgl2d.sys.Debug;
import net.jgl2d.util.Pair;
import net.jgl2d.util.QuickDraw;
import net.jgl2d.util.Triplet;

import java.util.Arrays;
import java.util.List;

/**
 * Created by peter on 8/9/15.
 */
public abstract class Physics {
    public static Triplet<Vector, Collider, Float> cast(Ray ray) {
        return cast(ray, new Collider[0]);
    }
    public static Triplet<Vector, Collider, Float> cast(Ray ray, Collider... ignore) {
        Triplet<Vector, Collider, Float> hit = null;
        List<Collider> ign = Arrays.asList(ignore);
        for(Collider coll : Collider.getColliders()) {
            if(!ign.contains(coll)) {
                Pair<Vector, Float> tHit = coll.toArea().cast(ray, Camera.getGL());
                if(tHit != null) {
                    //QuickDraw.cross(Camera.getGL(), tHit.a, 1);
                    if(hit == null || tHit.b < hit.c) {
                        hit = new Triplet<>(tHit.a, coll, tHit.b);
                    }
                }
            }
        }
        if(hit != null && Camera.main().debug()) { // DEBUGGING PURPOSES.
            QuickDraw.cross(Camera.getGL(), hit.a, 1);
        }
        return hit;
    }
}
