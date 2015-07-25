package net.jgl2d.math;

/**
 * Created by peter on 7/18/15.
 */
public class Vector {

    public static Vector right() {
        return new Vector(1,0);
    }

    public static Vector up() {
        return new Vector(0,1);
    }

    public float x, y;
    public Vector(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector(double x, double y) {
        this.x = (float) x;
        this.y = (float) y;
    }


    @Override
    public Vector clone() {
        return new Vector(x,y);
    }

    public Vector add(Vector other) {
        return add(other.x, other.y);
    }

    public Vector add(float x, float y) {
        this.x += x;
        this.y += y;
        return this;
    }

    public Vector add(double x, double y) {
        this.x += x;
        this.y += y;
        return this;
    }

    public Vector divide(float factor) {
        x /= factor;
        y /= factor;
        return this;
    }

    public Vector divide(double factor) {
        x /= factor;
        y /= factor;
        return this;
    }

    public Vector multiply(Vector other) {
        return multiply(other.x, other.y);
    }

    public Vector multiply(float x, float y) {
        this.x *= x;
        this.y *= y;
        return this;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }


    public Vector rotate(double a) {
        a = (a / 360f) * 2 * Math.PI;
        float tmpX = this.x;
        this.x = (float) (x * Math.cos(a) - y * Math.sin(a));
        this.y = (float) (tmpX * Math.sin(a) + y * Math.cos(a));
        return this;
    }

    public String toString() {
        return "Vector(" + x + ", " + y + ")";
    }

    public FixedVector toFixed() {
        return new FixedVector(x, y);
    }

    public static Vector difference(Vector from, Vector to) {
        return new Vector(to.x - from.x, to.y - from.y);
    }

    public float sqrMagnitude() {
        return (float) (Math.pow(x,2) + Math.pow(y,2));
    }

    public float magnitude() {
        return (float) Math.sqrt(sqrMagnitude());
    }

    public static class FixedVector extends Vector {

        public FixedVector(float x, float y) {
            super(x, y);
        }

        @Override
        public Vector add(Vector other) {
            return super.clone().add(other);
        }

        @Override
        public Vector add(float x, float y) {
            return super.clone().add(x, y);
        }

        @Override
        public Vector add(double x, double y) {
            return super.clone().add(x, y);
        }

        @Override
        public Vector divide(double factor) {
            return super.clone().divide(factor);
        }

        @Override
        public Vector divide(float factor) {
            return super.clone().divide(factor);
        }

        @Override
        public Vector multiply(Vector other) {
            return super.clone().multiply(other);
        }

        @Override
        public Vector multiply(float x, float y) {
            return super.clone().multiply(x, y);
        }

        @Override
        public Vector rotate(double a) {
            return super.clone().rotate(a);
        }

        @Override
        public void setX(float x) {
            super.clone().setX(x);
        }

        @Override
        public void setY(float y) {
            super.clone().setY(y);
        }
    }
}
