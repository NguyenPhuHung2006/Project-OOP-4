package provider;

import java.awt.event.KeyEvent;
import input.KeyboardManager;

public class InputProvider {
    private final KeyboardManager keyboardManager;

    public InputProvider(KeyboardManager keyboardManager) {
        this.keyboardManager = keyboardManager;
    }

    public boolean isLeftPressed() {
        return keyboardManager.isKeyPressed(KeyEvent.VK_LEFT);
    }

    public boolean isRightPressed() {
        return keyboardManager.isKeyPressed(KeyEvent.VK_RIGHT);
    }

}
