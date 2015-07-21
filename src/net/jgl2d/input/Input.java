package net.jgl2d.input;

import net.jgl2d.Camera;
import net.jgl2d.math.Vector;
import net.jgl2d.sys.Debug;

import javax.swing.event.MouseInputListener;
import java.awt.event.*;

/**
 * Created by peter on 7/19/15.
 */
public class Input implements MouseMotionListener, MouseInputListener, MouseWheelListener, KeyListener {

    public static Vector getMousePosition() {
        return mousePosition.clone();
    }

    public static Vector getPercentiveMousePosition() {
        return getMousePosition().multiply(1f/Camera.main().getScreenWidth(), 1f/Camera.main().getScreenHeight());
    }

    private static Vector mousePosition = new Vector(0,0);

    @Override
    public void mouseDragged(MouseEvent event) {
        Camera c = Camera.main();
        float dx = event.getX() - mousePosition.x;
        float dy = event.getY() - (c.getScreenHeight() - mousePosition.y);
        mousePosition = new Vector(event.getX(), c.getScreenHeight() - event.getY());
        float px = (dx / c.getScreenWidth()) * c.getHorizontalSize();
        float py = (dy / c.getScreenHeight()) * c.getVerticalSize();
        if(c.debug()) {
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
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        mousePosition = new Vector(mouseEvent.getX(), Camera.main().getScreenHeight() - mouseEvent.getY());
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
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {

    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        Camera c = Camera.main();
        if(c.debug()) {
            c.setVerticalSize(c.getVerticalSize() + e.getUnitsToScroll() * 0.3f);
        }
    }
}
