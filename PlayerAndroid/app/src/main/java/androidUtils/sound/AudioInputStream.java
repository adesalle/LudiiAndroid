package androidUtils.sound;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;

import java.io.BufferedInputStream;

import androidUtils.awt.image.BufferedImage;

public class AudioInputStream {

    private SoundPool soundPool;
    private int soundId;

    public AudioInputStream(BufferedInputStream inputStream)
    {

    }
    public AudioInputStream(Context context, int resId) {
        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        soundPool = new SoundPool.Builder()
                .setAudioAttributes(attributes)
                .setMaxStreams(1)
                .build();

        soundId = soundPool.load(context, resId, 1);

    }

    public SoundPool getSoundPool() {
        return soundPool;
    }

    public int getSoundId() {
        return soundId;
    }

    // Release resources
    public void close() {
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
        }
    }
}
