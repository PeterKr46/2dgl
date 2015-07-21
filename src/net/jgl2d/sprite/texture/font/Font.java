package net.jgl2d.sprite.texture.font;

import net.jgl2d.Camera;
import net.jgl2d.math.Rect;
import net.jgl2d.math.Vector;
import net.jgl2d.sprite.texture.Sprite;
import net.jgl2d.sprite.texture.SpriteSheet;
import net.jgl2d.sprite.texture.Texture2D;
import net.jgl2d.sys.Debug;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import java.io.File;
import java.util.HashMap;

/**
 * Created by peter on 7/20/15.
 */
public class Font extends SpriteSheet {
    private Font(Texture2D texture2D, String name) {
        super(texture2D);
    }

    private void setChar(char c, Sprite sprite) {
        fonts.put(c, sprite);
    }
    private HashMap<Character, Sprite> fonts = new HashMap<>();

    public static Font load(String path) {
        File image = new File(path);
        if(!image.exists() || !image.isFile()) {
            Debug.warn("No image at '" + path + "'!");
            return null;
        }
        String fontName = image.getName().replace(".png", "").replace(".jpg", "").replace(".jpeg", "");
        Texture2D texture = new Texture2D(path);
        //texture.enableAAFilter();
        Font font = new Font(texture, fontName);
        char[][] layout = new char[][] {
                new char[] {'a','b','c','d','e'},
                new char[] {'f','g','h','i','j'},
                new char[] {'k','l','m','n','o'},
                new char[] {'p','q','r','s','t'},
                new char[] {'u','v','w','x','y'},
                new char[] {'z','.','!','?','-'},
        };
        for(int x = 0; x < 5; x++) {
            for(int y = 0; y < 6; y++) {
                Sprite sprite = new Sprite(font, new Rect(x*8 + 1, y*8, 6, 8), fontName + "_" + layout[y][x]);
                font.fonts.put(layout[y][x], sprite);
            }
        }
        return font;
    }

    public void write(GL2 gl, Vector position, String text, float size, float rotation) {
        write(gl, position, text, size, rotation, new float[]{0,0,0});
    }

    public void write(GL2 gl, Vector position, String text, float size, float rotation, float[] rgb) {
        text = text.toLowerCase();
        Vector screenScale = Camera.main().getHalfsize();
        screenScale.x = 1 / screenScale.x;
        screenScale.y = 1 / screenScale.y;
        screenScale = screenScale.toFixed();
        Vector screenPos = Camera.main().localize(position).toFixed();
        for(int i = 0; i < text.length(); i++) {
            char chr = text.charAt(i);
            Sprite sprite = fonts.get(chr);
            if(sprite != null) {
                Vector scaledOffset = new Vector(-i * size * 0.75f,0).multiply(-1, -1).rotate(rotation).multiply(screenScale).toFixed();

                Vector bl = screenPos.add(scaledOffset);
                Vector tl = bl.clone().add(new Vector(0, size).rotate(rotation).multiply(screenScale));
                Vector tr = bl.clone().add(new Vector(size*0.75f, size).rotate(rotation).multiply(screenScale));
                Vector br = bl.clone().add(new Vector(size*0.75f, 0).rotate(rotation).multiply(screenScale));
                if(sprite.getTexture() != null) {
                    gl.glEnable(3553);
                    sprite.getTexture().loadGLTexture(gl);
                    sprite.getTexture().getTexture(gl).bind(gl);
                }

                if(rgb.length == 3) {
                    gl.glColor3f(rgb[0], rgb[1], rgb[2]);
                }
                if(rgb.length == 4) {
                    gl.glColor4f(rgb[0], rgb[1], rgb[2], rgb[3]);
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
                    gl.glColor4d(0.8, 0.8, 0.8, 0.1);
                    gl.glBegin(GL2.GL_LINE_STRIP);
                    gl.glVertex2f(bl.x, bl.y);
                    gl.glVertex2f(tl.x, tl.y);
                    gl.glVertex2f(tr.x, tr.y);
                    gl.glVertex2f(br.x, br.y);

                    gl.glVertex2f(br.x, br.y);
                    gl.glVertex2f(bl.x, bl.y);
                    gl.glVertex2f(tr.x, tr.y);

                    gl.glEnd();
                }
            }

        }

    }

}
