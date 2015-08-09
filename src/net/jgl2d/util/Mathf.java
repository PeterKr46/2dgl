package net.jgl2d.util;

/**
 * Created by peter on 8/8/15.
 */
public class Mathf {

    public static float round(double in) {
        return roundDigits(in, 5);
    }

    public static float roundDigits(double in, int digits) {
        int factor = 1;
        for(int i = 0; i < digits; i++) {
            factor *= 10;
        }
        double din = in * factor;
        float rin = Math.round(din);
        return rin /((float)factor);
    }

    public static float lerp(double start, double end, double percent) {
        return Mathf.round((end - start) * percent + start);
    }
}
