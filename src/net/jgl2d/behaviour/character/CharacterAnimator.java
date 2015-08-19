package net.jgl2d.behaviour.character;

import com.jogamp.opengl.GLAutoDrawable;
import net.jgl2d.behaviour.animation.Animation;
import net.jgl2d.behaviour.animation.Animator;
import net.jgl2d.sys.Debug;
import net.jgl2d.transform.Transform;

/**
 * Created by Peter on 19.08.2015.
 */
public class CharacterAnimator extends Animator {

    private CharacterController controller;
    public Animation walk, idle, jump;
    private Animation lastShown = null;

    public CharacterAnimator(Transform transform) {
        super(transform);
    }

    @Override
    public Animation getAnimation() {
        if(controller.isGrounded()) {
            if(controller.isMoving()) {
                return walk;
            }
            return idle;
        }
        return jump;
    }

    @Deprecated
    @Override
    public void setAnimations(Animation... anims) {

    }

    @Override
    public void start() {
        controller = (CharacterController) getTransform().getBehaviour(CharacterController.class);
        if(controller == null) {
            Debug.log(getTransform().name + " has no CharacterController attached to it - Destroying CharacterAnimator.");
            destroy();
        }
    }

    @Override
    public void update(GLAutoDrawable drawable) {
        if(getAnimation() != lastShown) {
            index = 0;
        }
        super.update(drawable);
        lastShown = getAnimation();
    }
}
