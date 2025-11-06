package input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Set;

/**
 * Singleton class responsible for managing keyboard input events.
 *
 * <p>The {@code KeyboardManager} implements {@link KeyListener} to track which keys
 * are currently pressed. It stores active key codes in a thread-safe {@link Set},
 * allowing other game components to query key states at any time.</p>
 *
 * <p>This class uses the <b>double-checked locking</b> pattern to ensure that only
 * one instance exists across the entire application, making it safe to access
 * from multiple threads.</p>
 *
 * @see java.awt.event.KeyListener
 * @see java.awt.event.KeyEvent
 */
public class KeyboardManager implements KeyListener {

    /** Singleton instance of the keyboard manager. */
    private static volatile KeyboardManager keyboardManager;

    /** Set of key codes currently being pressed. */
    private final Set<Integer> pressedKeys = new HashSet<>();

    /** Private constructor to prevent external instantiation. */
    private KeyboardManager() {}

    /**
     * Returns the singleton instance of the {@code KeyboardManager}.
     *
     * @return the global keyboard manager instance
     */
    public static KeyboardManager getInstance() {
        if (keyboardManager == null) {
            synchronized (KeyboardManager.class) {
                if (keyboardManager == null) {
                    keyboardManager = new KeyboardManager();
                }
            }
        }
        return keyboardManager;
    }

    /** {@inheritDoc} */
    @Override
    public void keyTyped(KeyEvent e) {
        // Not used, but required by KeyListener
    }

    /** {@inheritDoc} */
    @Override
    public void keyPressed(KeyEvent e) {
        pressedKeys.add(e.getKeyCode());
    }

    /** {@inheritDoc} */
    @Override
    public void keyReleased(KeyEvent e) {
        pressedKeys.remove(e.getKeyCode());
    }

    /**
     * Checks whether a specific key is currently being pressed.
     *
     * @param keyCode the key code (from {@link KeyEvent}) to check
     * @return {@code true} if the key is pressed, {@code false} otherwise
     */
    public boolean isKeyPressed(int keyCode) {
        return pressedKeys.contains(keyCode);
    }
}
