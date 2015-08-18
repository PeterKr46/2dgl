package net.jgl2d.math;

import net.jgl2d.sys.Debug;
import net.jgl2d.util.Mathf;

/**
 * Created by peter on 7/18/15.
 */
public class Vector {

    public static FixedVector left = new FixedVector(-1,0);
    public static FixedVector right = new FixedVector(1,0);
    public static FixedVector up = new FixedVector(0,1);
    public static FixedVector down = new FixedVector(0,-1);
    public static FixedVector zero = new FixedVector(0,0);

    public float x, y;
    public Vector(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector(double x, double y) {
        this.x = Mathf.round(x);
        this.y = Mathf.round(y);
    }

    public Vector getOrth() {
        if(x == 0) {
            return Vector.right;
        }
        if(y == 0) {
            return Vector.up;
        }
        return new Vector(-y, x);
    }


    @Override
    public Vector clone() {
        return new Vector(x,y);
    }

    public Vector subtract(Vector other) {
        return add(-other.x, -other.y);
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

    public Vector multiply(double factor) {
        return multiply(factor, factor);
    }

    public Vector multiply(Vector other) {
        return multiply(other.x, other.y);
    }

    public Vector multiply(double x, double y) {
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
        this.x = Mathf.round(x * Math.cos(a) - y * Math.sin(a));
        this.y = Mathf.round(tmpX * Math.sin(a) + y * Math.cos(a));
        return this;
    }

    public String toString() {
        return "Vector(" + x + ", " + y + ")";
    }

    public FixedVector toFixed() {
        return new FixedVector(x, y);
    }

    public float getAngle() {
        if(x == 0) {
            return (y < 0 ? 180 : 0);
        } else if(y == 0) {
            return (x < 0 ? 270 : 90);
        }
        float baseAngle = Mathf.round(Math.atan(Math.abs(y)/Math.abs(x)) * 180/Math.PI);
        if(y < 0) {
            if(x > 0) {
                return 90 + baseAngle;
            }
            return 180 + 90-baseAngle;
        } else {
            if(x > 0) {
                return 90-baseAngle;
            }
            return 270 + baseAngle;
        }
    }

    public static Vector difference(Vector from, Vector to) {
        return new Vector(to.x - from.x, to.y - from.y);
    }

    public static boolean isParallel(Vector a, Vector b) {
        a = a.clone().normalize();
        b = b.clone().normalize();
        a.x = Mathf.roundDigits(a.x, 3);
        a.y = Mathf.roundDigits(a.y, 3);
        b.x = Mathf.roundDigits(b.x, 3);
        b.y = Mathf.roundDigits(b.y, 3);
        return Math.abs(a.x) == Math.abs(b.x) && Math.abs(a.y) == Math.abs(b.y);
    }

    public static boolean isOpposite(Vector a, Vector b) {
        if(!isParallel(a,b)) return false;
        a = a.clone().normalize();
        b = b.clone().normalize();
        a.x = Mathf.roundDigits(a.x, 3);
        a.y = Mathf.roundDigits(a.y, 3);
        b.x = Mathf.roundDigits(b.x, 3);
        b.y = Mathf.roundDigits(b.y, 3);
        return a.x == -b.x && a.y == -b.y;
    }


    public static double dotProd(Vector a, Vector b) {
        return a.x * b.x + a.y * b.y;
    }

    public float sqrMagnitude() {
        return Mathf.round(Math.pow(x,2) + Math.pow(y,2));
    }

    public float magnitude() {
        return Mathf.round(Math.sqrt(sqrMagnitude()));
    }

    public Vector normalize() {
        if(x == 0 && y == 0) {
            return this;
        }
        double sc = 1.0/magnitude();
        this.x *= sc;
        this.y *= sc;
        return this;
    }

    public static class FixedVector extends Vector {

        public FixedVector(float x, float y) {
            super(x, y);
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
        public Vector multiply(double x, double y) {
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

        @Override
        public Vector normalize() {
            return super.clone().normalize();
        }
    }
}
