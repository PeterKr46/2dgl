package net.jgl2d.sprite.texture;

import net.jgl2d.math.Rect;
import net.jgl2d.math.Vector;
import net.jgl2d.sprite.texture.Texture2D;

/**
 * Created by peter on 7/18/15.
 */
public class Sprite {
    public String name;
    private SpriteSheet spriteSheet;
    public Rect area;
    public Vector offset = new Vector(0, 0);

    public Sprite(SpriteSheet sheet, Rect area) {
        this.spriteSheet = sheet;
        this.area = area.clone();
        this.area.setMinX(area.getMin().x/getTexture().getWidth());
        this.area.setMinY(1-area.getMax().y/getTexture().getHeight());
        this.area.setMaxX(area.getMax().x/getTexture().getWidth());
        this.area.setMaxY(1-area.getMin().y/getTexture().getHeight());
    }

    public Sprite(SpriteSheet sheet, Rect area, String name) {
        this(sheet, area);
        this.name = name;
    }

    public Texture2D getTexture() {
        return spriteSheet.getTexture();
    }

    public int getWidth() {
        return Math.round(area.getWidth() * getTexture().getWidth());
    }

    public int getHeight() {
        return Math.round(area.getHeight() * getTexture().getHeight());
    }

    public float getAspectRatio() {
        return ((float)getWidth()) / getHeight();
    }

    public String getName() {
        return name;
    }
}
