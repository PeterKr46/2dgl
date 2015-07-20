package net.jgl2d.sprite;

import net.jgl2d.Camera;
import net.jgl2d.behaviour.Behaviour;
import net.jgl2d.math.Vector;
import net.jgl2d.sprite.texture.Sprite;
import net.jgl2d.sys.Debug;
import net.jgl2d.transform.Transform;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

/**
 * Created by peter on 7/18/15.
 */
public class SpriteRenderer {
    private Transform transform;
    private int layerIndex = 0;
    private int layer = 0;
    private Sprite sprite;
    private boolean enabled = true;

    public SpriteRenderer(Transform transform) {
        this.transform = transform;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }
    public void draw(Camera camera, GLAutoDrawable drawable) {
        // Not in visible area, skip drawing.
        if(sprite == null || !camera.canSee(this)) {
            return;
        }

        GL2 gl = drawable.getGL().getGL2();

        Vector scale = transform.scale.clone();
        float rotation = transform.rotation;
        Vector position = transform.position;

        Vector screenScale = camera.getHalfsize();
        screenScale.x = 1 / screenScale.x * sprite.getAspectRatio();
        screenScale.y = 1 / screenScale.y;
        screenScale = screenScale.toFixed();

        Vector scaledOffset = sprite.offset.clone().multiply(scale).multiply(-1, -1).rotate(rotation).multiply(screenScale).toFixed();
        Vector screenPos = camera.localize(position).toFixed();

        Vector bl = screenPos.add(scaledOffset);
        Vector tl = bl.clone().add(new Vector(0, scale.y).rotate(rotation).multiply(screenScale));
        Vector tr = bl.clone().add(new Vector(scale.x, scale.y).rotate(rotation).multiply(screenScale));
        Vector br = bl.clone().add(new Vector(scale.x, 0).rotate(rotation).multiply(screenScale));

        if(sprite.getTexture() != null) {
            gl.glEnable(3553);
            sprite.getTexture().loadGLTexture(gl);
            sprite.getTexture().getTexture(gl).bind(gl);
        }

        gl.glEnable(GL.GL_BLEND);
        gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        gl.glBegin(GL2.GL_QUADS);

        gl.glTexCoord2f(sprite.area.getMin().x, sprite.area.getMax().y);
        gl.glVertex2f(tl.x, tl.y);

        gl.glTexCoord2f(sprite.area.getMin().x, sprite.area.getMin().y);
        gl.glVertex2f(bl.x, bl.y);

        gl.glTexCoord2f(sprite.area.getMax().x, sprite.area.getMin().y);
        gl.glVertex2f(br.x, br.y);

        gl.glTexCoord2f(sprite.area.getMax().x, sprite.area.getMax().y);
        gl.glVertex2f(tr.x, tr.y);

        gl.glEnd();
        gl.glDisable(3553);

        if(Camera.main().debug()) {
            gl.glColor4d(0.8, 0.8, 0.8, 0.5);
            gl.glBegin(GL2.GL_LINE_STRIP);
            gl.glVertex2f(bl.x, bl.y);
            gl.glVertex2f(tl.x, tl.y);
            gl.glVertex2f(tr.x, tr.y);
            gl.glVertex2f(br.x, br.y);

            gl.glVertex2f(br.x, br.y);
            gl.glVertex2f(bl.x, bl.y);

            gl.glEnd();
            gl.glColor3f(1,1,1);
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void enable() {
        enabled = true;
    }

    public void disable() {
        enabled = false;
    }
}
