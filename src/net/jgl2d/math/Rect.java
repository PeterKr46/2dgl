package net.jgl2d.math;

/**
 * Created by peter on 7/18/15.
 */
public class Rect {
    private Vector min, max;
    public Rect(float xMin, float yMin, float width, float height) {
        min = new Vector(xMin, yMin);
        max = new Vector(xMin + width, yMin + height);
    }

    public Vector getMin() {
        return min.clone();
    }

    public Vector getMax() {
        return max.clone();
    }

    public void setWidth(float width) {
        max.setX(min.x + width);
    }

    public void setHeight(float height) {
        max.setY(min.y + height);
    }

    public void setMinX(float x) {
        min.x = x;
    }

    public void setMinY(float y) {
        min.y = y;
    }

    public void setMaxX(float x) {
        max.x = x;
    }

    public void setMaxY(float y) {
        max.y = y;
    }



    @Override
    public Rect clone() {
        return new Rect(min.x, min.y, getWidth(), getHeight());
    }

    public float getWidth() {
        return max.x - min.x;
    }

    public float getHeight() {
        return max.y - min.y;
    }

    public void setMin(Vector min) {
        this.min = min.clone();
    }

    public void setMax(Vector max) {
        this.max = max.clone();
    }
}
