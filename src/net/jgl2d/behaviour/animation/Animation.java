package net.jgl2d.behaviour.animation;

import net.jgl2d.sprite.texture.Sprite;

/**
 * Created by peter on 7/19/15.
 */
public class Animation {
    private int length;
    private Sprite[] sFrames;
    private float[] rFrames;


    public Animation(int length) {
        this.length = length;
        sFrames = new Sprite[length];
        rFrames = new float[length];
    }


    public void setLength(int length) {
        Sprite[] newSFrames = new Sprite[length];
        float[] newRFrames = new float[length];
        System.arraycopy(sFrames, 0, newSFrames, 0, Math.min(length, this.length));
        System.arraycopy(rFrames, 0, newRFrames, 0, Math.min(length, this.length));
        sFrames = newSFrames;
        rFrames = newRFrames;
        this.length = length;
    }

    public void setFrame(int index, Sprite sprite) {
        sFrames[index] = sprite;
    }
    public void setFrame(int index, float rotation) {
        rFrames[index] = rotation;
    }

    public int length() {
        return length;
    }

    public Sprite getSprite(int i) {
        return i >= sFrames.length ? null : sFrames[i];
    }
}
