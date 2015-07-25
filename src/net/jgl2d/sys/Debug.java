package net.jgl2d.sys;

import net.jgl2d.transform.Transform;

/**
 * Created by peter on 7/19/15.
 */
public class Debug {

    public static Transform dragging = null;

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
