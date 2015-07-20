package net.jgl2d.sprite.texture;

import net.jgl2d.math.Rect;
import net.jgl2d.sys.Debug;
import net.jgl2d.util.json.JsonReader;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by peter on 7/19/15.
 */
public class SpriteSheet {
    private Texture2D texture;
    private List<Sprite> sprites = new ArrayList<>();
    public SpriteSheet(Texture2D texture2D) {
        this.texture = texture2D;
    }

    public static SpriteSheet load(String path) {
        File image = new File(path);
        if(!image.exists() || !image.isFile()) {
            Debug.warn("No image at '" + path + "'!");
            return null;
        }
        String sheetName = image.getName().replace(".png", "").replace(".jpg", "").replace(".jpeg", "");
        Debug.log("Loading sheet '" + sheetName + "'...");
        SpriteSheet sheet = new SpriteSheet(new Texture2D(path));
        File config = new File(path + ".json");
        if(config.exists()) {
            JsonReader reader = null;
            try {
                reader = new JsonReader(config);
            } catch (FileNotFoundException ignored) {
                // This cannot happen
            }
            assert reader != null;
            List<Object> json = reader.getTopNode().build();
            for(Object block : json) {
                HashMap<Object, Object> sprite = (HashMap<Object, Object>) block;
                int x = (int) sprite.get("x");
                int y = (int) sprite.get("y");
                int width = (int) sprite.get("width");
                int height = (int) sprite.get("height");
                sheet.sprites.add(new Sprite(sheet, new Rect(x, y, width, height), image.getName() + sprite.get("id") + ""));
            }
        } else {
            Debug.log("No json found for '" + path + "', creating single sprite.");
            sheet.sprites.add(new Sprite(sheet, new Rect(0,0,1,1)));
        }
        return sheet;
    }

    public Sprite getSprite(int i) {
        return sprites.get(i);
    }

    public Texture2D getTexture() {
        return texture;
    }
}
