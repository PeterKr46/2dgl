package net.jgl2d.input;

import net.jgl2d.Camera;
import net.jgl2d.math.Vector;
import net.jgl2d.sys.Debug;

import javax.swing.event.MouseInputListener;
import java.awt.event.*;
import java.util.HashMap;

/**
 * Created by peter on 7/19/15.
 */
public class Input implements MouseMotionListener, MouseInputListener, MouseWheelListener, KeyListener {


    private static boolean lMouse;
    private static HashMap<Character, Boolean> keysPressed = new HashMap<>();

    public static Vector getMousePosition() {
        return mousePosition.clone();
    }

    public static Vector getPercentiveMousePosition() {
        return getMousePosition().multiply(1f/Camera.main().getScreenWidth(), 1f/Camera.main().getScreenHeight());
    }

    private static Vector mousePosition = new Vector(0,0);

    public static boolean isLMouseDown() {
        return lMouse;
    }

    public static boolean isKeyDown(char c) {
        return keysPressed.containsKey(c) && keysPressed.get(c);
    }

    @Override
    public void mouseDragged(MouseEvent event) {
        Camera c = Camera.main();
        float dx = event.getX() - mousePosition.x;
        float dy = event.getY() - (c.getScreenHeight() - mousePosition.y);
        mousePosition = new Vector(event.getX(), c.getScreenHeight() - event.getY());
        float px = (dx / c.getScreenWidth()) * c.getHorizontalSize();
        float py = (dy / c.getScreenHeight()) * c.getVerticalSize();
        if(c.debug() && Debug.dragging == null) {
            c.setPosition(c.getPosition().add(-px, py));
        }
    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {
        mousePosition = new Vector(mouseEvent.getX(), Camera.main().getScreenHeight() - mouseEvent.getY());
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        mousePosition = new Vector(mouseEvent.getX(), Camera.main().getScreenHeight() - mouseEvent.getY());
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        mousePosition = new Vector(mouseEvent.getX(), Camera.main().getScreenHeight() - mouseEvent.getY());
        if(mouseEvent.getButton() == 1) {
            lMouse = true;
        }
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        mousePosition = new Vector(mouseEvent.getX(), Camera.main().getScreenHeight() - mouseEvent.getY());
        if(mouseEvent.getButton() == 1) {
            lMouse = false;
            Debug.dragging = null;
        }
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {
        mousePosition = new Vector(mouseEvent.getX(), Camera.main().getScreenHeight() - mouseEvent.getY());
    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {
        mousePosition = new Vector(mouseEvent.getX(), Camera.main().getScreenHeight() - mouseEvent.getY());
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {

    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        if(keyEvent.getKeyChar() == 'd') {
            Camera.main().toggleDebugging();
        }
        keysPressed.put(keyEvent.getKeyChar(), true);
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        keysPressed.put(keyEvent.getKeyChar(), false);
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        Camera c = Camera.main();
        if(c.debug()) {
            c.setVerticalSize(c.getVerticalSize() + e.getUnitsToScroll() * 0.3f);
        }
    }
}
