package net.jgl2d;

import net.jgl2d.behaviour.CharacterController;
import net.jgl2d.behaviour.collider.BoxCollider;
import net.jgl2d.behaviour.TestBehaviour;
import net.jgl2d.behaviour.animation.Animation;
import net.jgl2d.behaviour.animation.Animator;
import net.jgl2d.math.Vector;
import net.jgl2d.sprite.texture.Sprite;
import net.jgl2d.sprite.texture.SpriteSheet;
import net.jgl2d.sprite.texture.font.Font;
import net.jgl2d.transform.Transform;

/**
 * Created by peter on 7/18/15.
 */
public class Launcher {
    public static void main(String[] args) {
        SpriteSheet sheet = SpriteSheet.load("tilesets/door.png");
        SpriteSheet sheet2 = SpriteSheet.load("tilesets/pickup.png");
        SpriteSheet sheet3 = SpriteSheet.load("tilesets/newtiles.png");

        Animation anim = new Animation(40);
        anim.setFrame(0, sheet2.getSprite(0));
        anim.setFrame(5, sheet3.getSprite(1));
        anim.setFrame(10, sheet3.getSprite(2));
        anim.setFrame(15, sheet3.getSprite(3));
        anim.setFrame(20, sheet3.getSprite(4));
        anim.setFrame(25, sheet3.getSprite(5));
        anim.setFrame(30, sheet3.getSprite(6));
        Animation anim2 = new Animation(60);
        anim2.setFrame(0, sheet.getSprite(0));
        anim2.setFrame(30, sheet.getSprite(1));

        Sprite top = sheet3.getSprite(1);
        Sprite topLEnd = sheet3.getSprite(0);
        Sprite topREnd = sheet3.getSprite(2);

        Camera camera = new Camera();
        camera.setDebuggingEnabled(false);

        Transform t = Transform.createEmpty("TL");
        t.addRenderer().setSprite(topLEnd);
        t.position = new Vector(2,1);
        t.addBehaviour(CharacterController.class);
        ((TestBehaviour) t.addBehaviour(TestBehaviour.class)).font = Font.load("tilesets/font.png");
        for(float i = 0.95f; i < 3; i+= 0.95) {
            t = Transform.createEmpty("T #" + i);
            t.addRenderer().setSprite(top);
            t.addBehaviour(BoxCollider.class);
            t.position = new Vector(i,0);
            Animator animator = (Animator) t.addBehaviour(Animator.class);
            animator.setAnimations(anim, anim2);
            animator.animation = 1;
        }
        t = Transform.createEmpty("TR");
        t.addRenderer().setSprite(topREnd);
        t.position = new Vector(1,1);
        //Debug.log(collA.toArea().overlaps(collB.toArea()));
    }
}
