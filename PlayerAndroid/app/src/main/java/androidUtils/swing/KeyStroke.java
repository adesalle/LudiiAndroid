package androidUtils.swing;

import static androidUtils.awt.event.KeyEvent.CHAR_UNDEFINED;

import androidUtils.awt.event.KeyEvent;

public class KeyStroke {
    private final int keyCode;
    private final int modifiers;
    private final char keyChar;

    private KeyStroke(int keyCode, int modifiers, char keyChar) {
        this.keyCode = keyCode;
        this.modifiers = modifiers;
        this.keyChar = keyChar;
    }

    public static KeyStroke getKeyStroke(char keyChar, int modifiers) {
        int keyCode = KeyEvent.getExtendedKeyCode(keyChar);
        return new KeyStroke(keyCode, modifiers, keyChar);
    }

    public static KeyStroke getKeyStroke(int keyCode, int modifiers) {
        return new KeyStroke(keyCode, modifiers, CHAR_UNDEFINED);
    }

    public static KeyStroke getKeyStroke(String keyString) {
        if (keyString == null || keyString.trim().isEmpty()) {
            throw new IllegalArgumentException("Key string cannot be null or empty");
        }

        String[] parts = keyString.trim().split("\\s*\\+\\s*");
        int modifiers = 0;
        int keyCode = KeyEvent.KEYCODE_UNKNOWN;
        char keyChar = CHAR_UNDEFINED;

        for (String part : parts) {
            switch (part.toUpperCase()) {
                case "SPACE":
                    keyCode = KeyEvent.VK_SPACE;
                    keyChar = ' ';
                    break;
                case "LEFT":
                    keyCode = KeyEvent.VK_LEFT;
                    break;
                case "RIGHT":
                    keyCode = KeyEvent.VK_RIGHT;
                    break;
                case "UP":
                    keyCode = KeyEvent.VK_UP;
                    break;
                case "DOWN":
                    keyCode = KeyEvent.VK_DOWN;
                    break;
                case "ENTER":
                    keyCode = KeyEvent.VK_ENTER;
                    keyChar = '\n';
                    break;
                case "TAB":
                    keyCode = KeyEvent.VK_TAB;
                    keyChar = '\t';
                    break;
                case "ESCAPE":
                    keyCode = KeyEvent.VK_ESCAPE;
                    break;
                case "P":
                    keyCode = KeyEvent.VK_P;
                    keyChar = 'p';
                    break;
                default:
                    if (part.length() == 1) {
                        keyChar = part.charAt(0);
                        keyCode = KeyEvent.getExtendedKeyCode(keyChar);
                    } else {
                        throw new IllegalArgumentException("Unrecognized key: " + part);
                    }

            }
        }

        if (keyCode == KeyEvent.KEYCODE_UNKNOWN) {
            throw new IllegalArgumentException("Invalid key specification: " + keyString);
        }

        return new KeyStroke(keyCode, modifiers, keyChar);
    }

    public int getKeyCode() {
        return keyCode;
    }

    public int getModifiers() {
        return modifiers;
    }

    public char getKeyChar() {
        return keyChar;
    }



    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        if (keyChar != CHAR_UNDEFINED) {
            sb.append(keyChar);
        } else {
            sb.append(KeyEvent.getKeyText(keyCode));
        }

        return sb.toString();
    }
}