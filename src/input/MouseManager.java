package input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

/**
 * Singleton class that handles mouse input events for the game.
 *
 * <p>The {@code MouseManager} implements multiple listener interfaces to track
 * mouse position, button states, clicks, and wheel movement. It provides a simple
 * polling-based interface so other parts of the game can easily query the
 * current mouse state.</p>
 *
 * <p>This class follows the singleton pattern, ensuring only one instance manages
 * mouse input across the entire application.</p>
 *
 * @see java.awt.event.MouseListener
 * @see java.awt.event.MouseMotionListener
 * @see java.awt.event.MouseWheelListener
 */
public class MouseManager implements MouseListener, MouseMotionListener, MouseWheelListener {

    private int mouseX, mouseY;
    private int clickX, clickY;
    private boolean leftClicked;
    private boolean leftPressed, rightPressed, middlePressed;

    private static MouseManager mouseManager;

    /** Private constructor to enforce singleton pattern. */
    private MouseManager() {}

    /**
     * Returns the singleton instance of the {@code MouseManager}.
     *
     * @return the global mouse manager instance
     */
    public static MouseManager getInstance() {
        if (mouseManager == null)
            mouseManager = new MouseManager();
        return mouseManager;
    }

    /** @return the current X coordinate of the mouse cursor. */
    public int getX() {
        return mouseX;
    }

    /** @return the current Y coordinate of the mouse cursor. */
    public int getY() {
        return mouseY;
    }

    /** @return the X coordinate of the last left click. */
    public int getClickX() {
        return clickX;
    }

    /** @return the Y coordinate of the last left click. */
    public int getClickY() {
        return clickY;
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

    /**
     * Returns whether the left mouse button was clicked since the last check.
     * <p>Calling this method resets the click flag, so it returns {@code true}
     * only once per click event.</p>
     *
     * @return {@code true} if the left button was just clicked, {@code false} otherwise
     */
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

    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
    @Override public void mouseWheelMoved(MouseWheelEvent e) {}
}
