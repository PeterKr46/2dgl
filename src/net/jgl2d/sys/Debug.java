package net.jgl2d.sys;

/**
 * Created by peter on 7/19/15.
 */
public class Debug {
    private static boolean enabled = true;
    public boolean toggleDebug() {
        return enabled = !enabled;
    }

    public static void log(Object obj) {
        if(enabled) {
            System.out.println("[Debug] " + obj);
        }
    }

    public static void warn(Object obj) {
        if(enabled) {
            System.out.println("[Warning] " + obj);
        }
    }
}
