package input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class MouseManager implements MouseListener, MouseMotionListener, MouseWheelListener {

    private int mouseX, mouseY;
    private int clickX, clickY;
    private boolean leftClicked;
    private boolean leftPressed, rightPressed, middlePressed;
    private int wheelRotation;

    private static MouseManager mouseManager;

    private MouseManager() {
    }

    public static MouseManager getInstance() {
        if (mouseManager == null)
            mouseManager = new MouseManager();
        return mouseManager;
    }

    public int getX() {
        return mouseX;
    }

    public int getY() {
        return mouseY;
    }

    public int getClickX() {
        return clickX;
    }

    public int getClickY() {
        return clickY;
    }


    public boolean isLeftPressed() {
        return leftPressed;
    }

    public boolean isRightPressed() {
        return rightPressed;
    }

    public boolean isMiddlePressed() {
        return middlePressed;
    }

    public int getWheelRotation() {
        return wheelRotation;
    }

    public void resetWheel() {
        wheelRotation = 0;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        switch (e.getButton()) {
            case MouseEvent.BUTTON1 -> leftPressed = true;
            case MouseEvent.BUTTON2 -> middlePressed = true;
            case MouseEvent.BUTTON3 -> rightPressed = true;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        switch (e.getButton()) {
            case MouseEvent.BUTTON1 -> {
                leftPressed = false;
                leftClicked = true;
                clickX = e.getX();
                clickY = e.getY();
            }
            case MouseEvent.BUTTON2 -> middlePressed = false;
            case MouseEvent.BUTTON3 -> rightPressed = false;
        }
    }

    public boolean isLeftClicked() {
        if (leftClicked) {
            leftClicked = false;
            return true;
        }
        return false;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        wheelRotation = e.getWheelRotation();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
