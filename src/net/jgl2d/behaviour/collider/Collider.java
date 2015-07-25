package net.jgl2d.behaviour.collider;

import com.jogamp.opengl.GLAutoDrawable;
import net.jgl2d.behaviour.Behaviour;
import net.jgl2d.math.area.Area;
import net.jgl2d.transform.Transform;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Peter on 26.07.2015.
 */
public abstract class Collider extends Behaviour {

    private static List<Collider> colliders = new ArrayList<>();

    private List<Collider> currentOverlaps = new ArrayList<>();

    public Collider(Transform transform) {
        super(transform);
        colliders.add(this);
    }

    public abstract Area toArea();

    public List<Collider> getEligibleColliders() {
        List<Collider> colls = new ArrayList<>();
        for(Collider coll : colliders) {
            if(coll != this) {
                colls.add(coll);
            }
            //TODO
        }
        return colls;
    }

    public final void update(GLAutoDrawable drawable) {
        currentOverlaps.clear();
        Area thisArea = toArea();
        for(Collider coll : getEligibleColliders()) {
            if(!coll.currentOverlaps.contains(this) && coll.toArea().overlaps(thisArea)) {
                currentOverlaps.add(coll);
                coll.currentOverlaps.add(this);
                transform.sendMessage("onCollide", new Class<?>[]{Collider.class}, new Object[]{coll});
                coll.transform.sendMessage("onCollide", new Class<?>[] {Collider.class}, new Object[]{this});
            }
        }
        draw(drawable);
    }

    protected abstract void draw(GLAutoDrawable drawable);
}
