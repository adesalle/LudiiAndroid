package androidUtils.sound;

public class LineEvent {

    public enum Type {
        START,
        STOP
    }

    public Type getType()
    {
        return Type.START;
    }
}
