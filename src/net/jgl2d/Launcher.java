package net.jgl2d;

import net.jgl2d.behaviour.TestBehaviour;
import net.jgl2d.behaviour.animation.Animation;
import net.jgl2d.behaviour.character.CharacterAnimator;
import net.jgl2d.behaviour.character.CharacterController;
import net.jgl2d.behaviour.physics.BoxCollider;
import net.jgl2d.behaviour.physics.GlobalFloorCollider;
import net.jgl2d.behaviour.physics.LineCollider;
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
        SpriteSheet playerSheet = SpriteSheet.load("tilesets/testsheet.png");
        SpriteSheet sheet2 = SpriteSheet.load("tilesets/pickup.png");
        SpriteSheet sheet3 = SpriteSheet.load("tilesets/newtiles.png");

        Sprite top = sheet3.getSprite(1);
        Sprite topLEnd = sheet3.getSprite(0);
        Sprite topREnd = sheet3.getSprite(2);

        Camera camera = new Camera();
        camera.setDebuggingEnabled(false);
        camera.backgroundColor = new float[] {0.2f, 0.4f, 0.75f};

        Transform t = Transform.createEmpty("TL");
        t.addRenderer().setSprite(playerSheet.getSprite(0));
        t.position = new Vector(2,2);
        //t.addBehaviour(CircleCollider.class);
        ((CharacterController)t.addBehaviour(CharacterController.class)).offset = new Vector(0, 0.5);
        ((GlobalFloorCollider)t.addBehaviour(GlobalFloorCollider.class)).yPos = 0.2f;
        ((TestBehaviour) t.addBehaviour(TestBehaviour.class)).font = Font.load("tilesets/font.png");
        CharacterAnimator animator = (CharacterAnimator) t.addBehaviour(CharacterAnimator.class);
        animator.idle = new Animation(1);
        animator.idle.setFrame(0, playerSheet.getSprite(0));
        animator.walk = new Animation(15);
        animator.walk.setFrame(0, playerSheet.getSprite(0));
        animator.walk.setFrame(5, playerSheet.getSprite(1));
        animator.walk.setFrame(10, playerSheet.getSprite(2));
        animator.jump = new Animation(1);
        animator.jump.setFrame(0, playerSheet.getSprite(2));
        for(float i = 0.95f; i < 10; i+= 1) {
            t = Transform.createEmpty("T #" + i);
            t.addRenderer().setSprite(top);
            t.addBehaviour(BoxCollider.class);
            t.position = new Vector(i,1);
        }
        t = Transform.createEmpty("Wall");
        t.addBehaviour(LineCollider.class);
        t.rotation = -90;
        //Debug.log(collA.toArea().overlaps(collB.toArea()));
    }
}
