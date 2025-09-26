package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Set;

public class KeyboardManager implements KeyListener {

    private static volatile KeyboardManager keyboardManager;

    private final Set<Integer> pressedKeys = new HashSet<>();

    // Private constructor for singleton
    private KeyboardManager() {}

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

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        pressedKeys.add(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        pressedKeys.remove(e.getKeyCode());
    }

    public boolean isKeyPressed(int keyCode) {
        return pressedKeys.contains(keyCode);
    }
}
