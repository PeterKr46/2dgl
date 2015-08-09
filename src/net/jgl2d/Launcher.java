package net.jgl2d;

import net.jgl2d.behaviour.CharacterController;
import net.jgl2d.behaviour.TestBehaviour;
import net.jgl2d.behaviour.animation.Animation;
import net.jgl2d.behaviour.animation.Animator;
import net.jgl2d.behaviour.collider.BoxCollider;
import net.jgl2d.behaviour.collider.CircleCollider;
import net.jgl2d.behaviour.collider.GlobalFloorCollider;
import net.jgl2d.math.Ray;
import net.jgl2d.math.Vector;
import net.jgl2d.sprite.texture.Sprite;
import net.jgl2d.sprite.texture.SpriteSheet;
import net.jgl2d.sprite.texture.font.Font;
import net.jgl2d.sys.Debug;
import net.jgl2d.transform.Transform;
import net.jgl2d.util.Mathf;

/**
 * Created by peter on 7/18/15.
 */
public class Launcher {
    public static void main(String[] args) {
        Debug.log(new Vector(0.3,-0.1).getAngle());
        SpriteSheet sheet = SpriteSheet.load("tilesets/testsheet.png");
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
        Animation anim2 = new Animation(30);
        anim2.setFrame(0, sheet.getSprite(0));
        anim2.setFrame(10, sheet.getSprite(1));
        anim2.setFrame(20, sheet.getSprite(2));

        Sprite top = sheet3.getSprite(1);
        Sprite topLEnd = sheet3.getSprite(0);
        Sprite topREnd = sheet3.getSprite(2);

        Camera camera = new Camera();
        camera.setDebuggingEnabled(false);

        Transform t = Transform.createEmpty("TL");
        t.addRenderer().setSprite(topLEnd);
        t.position = new Vector(2,1);
        //t.addBehaviour(CircleCollider.class);
        ((CharacterController)t.addBehaviour(CharacterController.class)).offset = new Vector(0.5, 0.5);
        ((GlobalFloorCollider)t.addBehaviour(GlobalFloorCollider.class)).yPos = -3;
        //((TestBehaviour) t.addBehaviour(TestBehaviour.class)).font = Font.load("tilesets/font.png");
        Animator animator = (Animator) t.addBehaviour(Animator.class);
        animator.setAnimations(anim, anim2);
        animator.animation = 1;
        for(float i = 0.95f; i < 10; i+= 1) {
            t = Transform.createEmpty("T #" + i);
            t.addRenderer().setSprite(top);
            t.addBehaviour(CircleCollider.class);
            t.position = new Vector(i,Math.random());
        }
        t = Transform.createEmpty("TR");
        t.addRenderer().setSprite(topREnd);
        t.position = new Vector(1,1);
        //Debug.log(collA.toArea().overlaps(collB.toArea()));
    }
}
