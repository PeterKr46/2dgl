package net.jgl2d.behaviour.animation;

import javax.media.opengl.GLAutoDrawable;
import net.jgl2d.behaviour.Behaviour;
import net.jgl2d.sprite.texture.Sprite;
import net.jgl2d.transform.Transform;

/**
 * Created by peter on 7/19/15.
 */
public class Animator extends Behaviour {
    public int animation;
    private Animation[] animations;
    private int index;
    private int fps = 30;
    private long lastFrame;

    public Animator(Transform transform) {
        super(transform);
        this.index = 0;
        this.animation = 0;
        this.animations = new Animation[0];
    }

    public void setAnimations(Animation... anims) {
        animations = anims;
    }

    public Animation getAnimation() {
        return (animations.length > animation ? animations[animation] : null);
    }

    public int next() {
        if(getAnimation() == null) {
            return 0;
        }
        index++;
        if(index == getAnimation().length() - 1){
            index = 0;
        }
        return index;
    }

    public Sprite getCurrentSprite() {
        if(getAnimation() == null) {
            return null;
        }
        int i = index;
        while(getAnimation().getSprite(i) == null && i > 0) {
            i--;
        }
        return getAnimation().getSprite(i);
    }

    @Override
    public void update(GLAutoDrawable drawable) {
        long now = System.currentTimeMillis();
        if(now - lastFrame > 1f/fps) {
            lastFrame = now;
            next();
            transform.getRenderer().setSprite(getCurrentSprite());
        }
    }
}
