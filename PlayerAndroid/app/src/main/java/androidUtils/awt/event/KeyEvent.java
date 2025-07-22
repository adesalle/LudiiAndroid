package androidUtils.awt.event;

public class KeyEvent {
    // Constantes d'action
    public static final int KEY_PRESSED = android.view.KeyEvent.ACTION_DOWN;
    public static final int KEY_RELEASED = android.view.KeyEvent.ACTION_UP;
    public static final int KEY_TYPED = -1;

    // Constantes de touches
    public static final int VK_ENTER = android.view.KeyEvent.KEYCODE_ENTER;
    public static final int VK_BACK_SPACE = android.view.KeyEvent.KEYCODE_DEL;
    public static final int VK_TAB = android.view.KeyEvent.KEYCODE_TAB;
    public static final int VK_CANCEL = android.view.KeyEvent.KEYCODE_BREAK;
    public static final int VK_CLEAR = android.view.KeyEvent.KEYCODE_CLEAR;
    public static final int VK_SHIFT = android.view.KeyEvent.KEYCODE_SHIFT_LEFT;
    public static final int VK_CONTROL = android.view.KeyEvent.KEYCODE_CTRL_LEFT;
    public static final int VK_ALT = android.view.KeyEvent.KEYCODE_ALT_LEFT;
    public static final int VK_PAUSE = android.view.KeyEvent.KEYCODE_MEDIA_PAUSE;
    public static final int VK_CAPS_LOCK = android.view.KeyEvent.KEYCODE_CAPS_LOCK;
    public static final int VK_ESCAPE = android.view.KeyEvent.KEYCODE_ESCAPE;
    public static final int VK_SPACE = android.view.KeyEvent.KEYCODE_SPACE;
    public static final int VK_PAGE_UP = android.view.KeyEvent.KEYCODE_PAGE_UP;
    public static final int VK_PAGE_DOWN = android.view.KeyEvent.KEYCODE_PAGE_DOWN;
    public static final int VK_END = android.view.KeyEvent.KEYCODE_MOVE_END;
    public static final int VK_HOME = android.view.KeyEvent.KEYCODE_HOME;
    public static final int VK_LEFT = android.view.KeyEvent.KEYCODE_DPAD_LEFT;
    public static final int VK_UP = android.view.KeyEvent.KEYCODE_DPAD_UP;
    public static final int VK_RIGHT = android.view.KeyEvent.KEYCODE_DPAD_RIGHT;
    public static final int VK_DOWN = android.view.KeyEvent.KEYCODE_DPAD_DOWN;
    public static final int VK_COMMA = android.view.KeyEvent.KEYCODE_COMMA;
    public static final int VK_PERIOD = android.view.KeyEvent.KEYCODE_PERIOD;
    public static final int VK_SLASH = android.view.KeyEvent.KEYCODE_SLASH;
    public static final int VK_0 = android.view.KeyEvent.KEYCODE_0;
    public static final int VK_1 = android.view.KeyEvent.KEYCODE_1;
    public static final int VK_9 = android.view.KeyEvent.KEYCODE_9;
    public static final int VK_SEMICOLON = android.view.KeyEvent.KEYCODE_SEMICOLON;
    public static final int VK_EQUALS = android.view.KeyEvent.KEYCODE_EQUALS;
    public static final int VK_A = android.view.KeyEvent.KEYCODE_A;
    public static final int VK_B = android.view.KeyEvent.KEYCODE_B;
    public static final int VK_C = android.view.KeyEvent.KEYCODE_C;
    public static final int VK_D = android.view.KeyEvent.KEYCODE_D;
    public static final int VK_E = android.view.KeyEvent.KEYCODE_E;
    public static final int VK_F = android.view.KeyEvent.KEYCODE_F;
    public static final int VK_Z = android.view.KeyEvent.KEYCODE_Z;
    public static final int VK_OPEN_BRACKET = android.view.KeyEvent.KEYCODE_LEFT_BRACKET;
    public static final int VK_BACK_SLASH = android.view.KeyEvent.KEYCODE_BACKSLASH;
    public static final int VK_CLOSE_BRACKET = android.view.KeyEvent.KEYCODE_RIGHT_BRACKET;
    public static final int VK_NUMPAD0 = android.view.KeyEvent.KEYCODE_NUMPAD_0;
    public static final int VK_NUMPAD1 = android.view.KeyEvent.KEYCODE_NUMPAD_1;
    public static final int VK_NUMPAD9 = android.view.KeyEvent.KEYCODE_NUMPAD_9;
    public static final int VK_MULTIPLY = android.view.KeyEvent.KEYCODE_NUMPAD_MULTIPLY;
    public static final int VK_ADD = android.view.KeyEvent.KEYCODE_NUMPAD_ADD;
    public static final int VK_SEPARATOR = android.view.KeyEvent.KEYCODE_NUMPAD_COMMA;
    public static final int VK_SUBTRACT = android.view.KeyEvent.KEYCODE_NUMPAD_SUBTRACT;
    public static final int VK_DECIMAL = android.view.KeyEvent.KEYCODE_NUMPAD_DOT;
    public static final int VK_DIVIDE = android.view.KeyEvent.KEYCODE_NUMPAD_DIVIDE;
    public static final int VK_DELETE = android.view.KeyEvent.KEYCODE_FORWARD_DEL;
    public static final int VK_NUM_LOCK = android.view.KeyEvent.KEYCODE_NUM_LOCK;
    public static final int VK_SCROLL_LOCK = android.view.KeyEvent.KEYCODE_SCROLL_LOCK;
    public static final int VK_F1 = android.view.KeyEvent.KEYCODE_F1;
    public static final int VK_F12 = android.view.KeyEvent.KEYCODE_F12;
    public static final int VK_PRINTSCREEN = android.view.KeyEvent.KEYCODE_SYSRQ;
    public static final int VK_INSERT = android.view.KeyEvent.KEYCODE_INSERT;
    public static final int VK_HELP = android.view.KeyEvent.KEYCODE_HELP;
    public static final int VK_META = android.view.KeyEvent.KEYCODE_META_LEFT;
    public static final int VK_BACK_QUOTE = android.view.KeyEvent.KEYCODE_GRAVE;
    public static final int VK_QUOTE = android.view.KeyEvent.KEYCODE_APOSTROPHE;

    public static final int VK_Y = android.view.KeyEvent.KEYCODE_Y;
    public static final int VK_X = android.view.KeyEvent.KEYCODE_X;
    public static final int VK_V = android.view.KeyEvent.KEYCODE_V;
    public static final char CHAR_UNDEFINED = 'u';
    public static final int KEYCODE_UNKNOWN =android.view.KeyEvent.KEYCODE_UNKNOWN;
    public static final int VK_P = android.view.KeyEvent.KEYCODE_P;


    private final android.view.KeyEvent event;
    private final int id;
    private char keyChar;

    public KeyEvent(android.view.KeyEvent androidKeyEvent, int id) {
        this.event = androidKeyEvent;
        this.id = id;
        this.keyChar = (char) androidKeyEvent.getUnicodeChar();
    }

    public KeyEvent(android.view.KeyEvent event) {
        this(event, event.getAction());
    }

    public static String getKeyText(int keyCode) {
        switch (keyCode) {
            case VK_ENTER: return "Enter";
            case VK_BACK_SPACE: return "Backspace";
            case VK_TAB: return "Tab";
            case VK_CANCEL: return "Cancel";
            case VK_CLEAR: return "Clear";
            case VK_SHIFT: return "Shift";
            case VK_CONTROL: return "Control";
            case VK_ALT: return "Alt";
            case VK_PAUSE: return "Pause";
            case VK_CAPS_LOCK: return "Caps Lock";
            case VK_ESCAPE: return "Escape";
            case VK_SPACE: return "Space";
            case VK_PAGE_UP: return "Page Up";
            case VK_PAGE_DOWN: return "Page Down";
            case VK_END: return "End";
            case VK_HOME: return "Home";
            case VK_LEFT: return "Left";
            case VK_UP: return "Up";
            case VK_RIGHT: return "Right";
            case VK_DOWN: return "Down";
            case VK_COMMA: return ",";
            case VK_PERIOD: return ".";
            case VK_SLASH: return "/";
            case VK_0: return "0";
            case VK_1: return "1";
            case VK_9: return "9";
            case VK_SEMICOLON: return ";";
            case VK_EQUALS: return "=";
            case VK_A: return "A";
            case VK_B: return "B";
            case VK_C: return "C";
            case VK_D: return "D";
            case VK_E: return "E";
            case VK_F: return "F";
            case VK_X: return "X";
            case VK_Y: return "Y";
            case VK_V : return "V";
            case VK_Z: return "Z";
            case VK_OPEN_BRACKET: return "[";
            case VK_BACK_SLASH: return "\\";
            case VK_CLOSE_BRACKET: return "]";
            case VK_NUMPAD0: return "NumPad-0";
            case VK_NUMPAD1: return "NumPad-1";
            case VK_NUMPAD9: return "NumPad-9";
            case VK_MULTIPLY: return "NumPad *";
            case VK_ADD: return "NumPad +";
            case VK_SEPARATOR: return "Separator";
            case VK_SUBTRACT: return "NumPad -";
            case VK_DECIMAL: return "NumPad .";
            case VK_DIVIDE: return "NumPad /";
            case VK_DELETE: return "Delete";
            case VK_NUM_LOCK: return "Num Lock";
            case VK_SCROLL_LOCK: return "Scroll Lock";
            case VK_F1: return "F1";
            case VK_F12: return "F12";
            case VK_PRINTSCREEN: return "Print Screen";
            case VK_INSERT: return "Insert";
            case VK_HELP: return "Help";
            case VK_META: return "Meta";
            case VK_BACK_QUOTE: return "`";
            case VK_QUOTE: return "'";

            default:
                if (keyCode >= android.view.KeyEvent.KEYCODE_A && keyCode <= android.view.KeyEvent.KEYCODE_Z) {
                    return String.valueOf((char) ('A' + (keyCode - android.view.KeyEvent.KEYCODE_A)));
                }
                if (keyCode >= android.view.KeyEvent.KEYCODE_0 && keyCode <= android.view.KeyEvent.KEYCODE_9) {
                    return String.valueOf((char) ('0' + (keyCode - android.view.KeyEvent.KEYCODE_0)));
                }
                return "KeyCode: " + keyCode;
        }
    }

    public static int getExtendedKeyCode(char keyChar) {
        // Vérifier les caractères alphabétiques
        if (keyChar >= 'A' && keyChar <= 'Z') {
            return android.view.KeyEvent.KEYCODE_A + (keyChar - 'A');
        }
        if (keyChar >= 'a' && keyChar <= 'z') {
            return android.view.KeyEvent.KEYCODE_A + (keyChar - 'a');
        }

        // Vérifier les chiffres
        if (keyChar >= '0' && keyChar <= '9') {
            return android.view.KeyEvent.KEYCODE_0 + (keyChar - '0');
        }

        // Gérer les caractères spéciaux
        switch (keyChar) {
            case '\n': return VK_ENTER;
            case '\t': return VK_TAB;
            case ' ': return VK_SPACE;
            case ',': return VK_COMMA;
            case '.': return VK_PERIOD;
            case '/': return VK_SLASH;
            case ';': return VK_SEMICOLON;
            case '=': return VK_EQUALS;
            case '[': return VK_OPEN_BRACKET;
            case '\\': return VK_BACK_SLASH;
            case ']': return VK_CLOSE_BRACKET;
            case '`': return VK_BACK_QUOTE;
            case '\'': return VK_QUOTE;
            case '-': return android.view.KeyEvent.KEYCODE_MINUS;
            case '+': return android.view.KeyEvent.KEYCODE_PLUS;
            case '*': return VK_MULTIPLY;
            case '!': return android.view.KeyEvent.KEYCODE_1; // Avec shift
            case '@': return android.view.KeyEvent.KEYCODE_2; // Avec shift
            case '#': return android.view.KeyEvent.KEYCODE_3; // Avec shift
            case '$': return android.view.KeyEvent.KEYCODE_4; // Avec shift
            case '%': return android.view.KeyEvent.KEYCODE_5; // Avec shift
            case '^': return android.view.KeyEvent.KEYCODE_6; // Avec shift
            case '&': return android.view.KeyEvent.KEYCODE_7; // Avec shift
            case '(': return android.view.KeyEvent.KEYCODE_9; // Avec shift
            case ')': return android.view.KeyEvent.KEYCODE_0; // Avec shift
            case '_': return android.view.KeyEvent.KEYCODE_MINUS; // Avec shift
            case ':': return VK_SEMICOLON; // Avec shift
            case '"': return VK_QUOTE; // Avec shift
            case '<': return VK_COMMA; // Avec shift
            case '>': return VK_PERIOD; // Avec shift
            case '?': return VK_SLASH; // Avec shift
            case '{': return VK_OPEN_BRACKET; // Avec shift
            case '}': return VK_CLOSE_BRACKET; // Avec shift
            case '|': return VK_BACK_SLASH; // Avec shift
            case '~': return VK_BACK_QUOTE; // Avec shift
        }

        // Si le caractère n'est pas reconnu
        return KEYCODE_UNKNOWN;
    }

    public int getKeyCode() {
        return event != null ? event.getKeyCode() : 0;
    }

    public char getKeyChar() {
        return keyChar;
    }

    public int getID() {
        return id;
    }

    public boolean isActionKey() {
        switch (event.getKeyCode()) {
            case VK_UP:
            case VK_DOWN:
            case VK_LEFT:
            case VK_RIGHT:
            case VK_ENTER:
            case VK_ESCAPE:
            case VK_DELETE:
            case VK_TAB:
            case VK_PAGE_UP:
            case VK_PAGE_DOWN:
            case VK_HOME:
            case VK_END:
            case VK_F1:
            case VK_F12:
                return true;
            default:
                return false;
        }
    }

    public boolean isShiftDown() {
        return event != null && event.isShiftPressed();
    }

    public boolean isControlDown() {
        return event != null && event.isCtrlPressed();
    }

    public boolean isAltDown() {
        return event != null && event.isAltPressed();
    }

    public boolean isMetaDown() {
        return event != null && event.isMetaPressed();
    }

    public void setKeyChar(char c) {
        this.keyChar = c;
    }

    public android.view.KeyEvent getAndroidKeyEvent() {
        return event;
    }

    public int getModifiers() {
        return event.getModifiers();
    }
}