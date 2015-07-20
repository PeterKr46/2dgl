package net.jgl2d;

import junit.framework.Test;
import net.jgl2d.behaviour.BoxCollider;
import net.jgl2d.behaviour.CircleCollider;
import net.jgl2d.behaviour.TestBehaviour;
import net.jgl2d.behaviour.animation.Animation;
import net.jgl2d.behaviour.animation.Animator;
import net.jgl2d.math.Vector;
import net.jgl2d.sprite.texture.Sprite;
import net.jgl2d.sprite.texture.SpriteSheet;
import net.jgl2d.sprite.texture.font.Font;
import net.jgl2d.sys.BehaviourInspector;
import net.jgl2d.transform.Transform;

/**
 * Created by peter on 7/18/15.
 */
public class Launcher {
    public static void main(String[] args) {
        SpriteSheet sheet = SpriteSheet.load("/home/peter/Desktop/tilesets/door.png");
        SpriteSheet sheet2 = SpriteSheet.load("/home/peter/Desktop/tilesets/pickup.png");
        SpriteSheet sheet3 = SpriteSheet.load("/home/peter/Desktop/tilesets/newtiles.png");

        Animation anim = new Animation(40);
        anim.setFrame(0, sheet3.getSprite(0));
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
        //camera.setDebuggingEnabled(false);

        Transform t = Transform.createEmpty("TL");
        t.addRenderer().setSprite(topLEnd);
        t.position = new Vector(-1,0);
        t.rotation = 35;
        t.addBehaviour(BoxCollider.class);
        ((TestBehaviour)t.addBehaviour(TestBehaviour.class)).font = Font.load("/home/peter/Desktop/tilesets/font.png");;

        for(int i = 1; i < 3; i++) {
            t = Transform.createEmpty("T #" + i);
            t.addRenderer().setSprite(top);
            t.position = new Vector(i,0);
        }
        t = Transform.createEmpty("TR");
        t.addRenderer().setSprite(topREnd);
        t.position = new Vector(3,0);
    }
}
