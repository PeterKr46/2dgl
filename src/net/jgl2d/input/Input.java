package net.jgl2d.input;

import net.jgl2d.Camera;
import net.jgl2d.math.Vector;

import javax.swing.event.MouseInputListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

/**
 * Created by peter on 7/19/15.
 */
public class Input implements MouseMotionListener, MouseInputListener {

    public static Vector getMousePosition() {
        return mousePosition.clone();
    }

    public static Vector getPercentiveMousePosition() {
        return getMousePosition().multiply(1f/Camera.main().getScreenWidth(), 1f/Camera.main().getScreenHeight());
    }

    private static Vector mousePosition = new Vector(0,0);
    private static boolean leftClick = false, leftHeld = false,
            rightClick = false, rightHeld = false;

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {
        if(mouseEvent.getButton() == 1) {
            leftHeld = true;
        }
        if(mouseEvent.getButton() == 3) {
            rightHeld = true;
        }
    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {
        mousePosition = new Vector(mouseEvent.getX(), Camera.main().getScreenHeight() - mouseEvent.getY());
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        if(mouseEvent.getButton() == 1) {
            leftClick = true;
        }
        if(mouseEvent.getButton() == 3) {
            rightClick = true;
        }
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        if(mouseEvent.getButton() == 1) {
            leftClick = true;
            leftHeld = true;
        }
        if(mouseEvent.getButton() == 3) {
            rightClick = true;
            rightHeld = true;
        }
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        if(mouseEvent.getButton() == 1) {
            leftHeld = false;
        }
        if(mouseEvent.getButton() == 3) {
            rightHeld = false;
        }
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }

    public static boolean isRightClick() {
        return rightClick;
    }

    public static boolean isLeftClick() {
        return leftClick;
    }

    public static boolean isLeftHeld() {
        return leftHeld;
    }

    public static boolean isRightHeld() {
        return rightHeld;
    }
}
