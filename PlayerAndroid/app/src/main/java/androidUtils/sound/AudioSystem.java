package androidUtils.sound;

import android.content.Context;

import java.io.BufferedInputStream;


public class AudioSystem {
    public static Clip getClip()
    {
        return new Clip();
    }

    public static AudioInputStream getAudioInputStream(BufferedInputStream bufferedIS)
    {
        return new AudioInputStream(bufferedIS);
    }
}