package androidUtils.sound;

import android.media.SoundPool;

public class Clip {

    private SoundPool soundPool;
    private int soundId;

    public Clip() {

    }

    public void open(AudioInputStream inputStream)
    {
        soundPool = inputStream.getSoundPool();
        soundId = inputStream.getSoundId();
    }


    public void start() {
        if (soundPool != null) {
            soundPool.play(soundId, 1.0f, 1.0f, 1, 0, 1.0f);
        }
    }

    // Stop is not directly supported by SoundPool, but we can stop all sounds
    public void stop() {
        if (soundPool != null) {
            soundPool.autoPause(); // Pauses all streams
        }
    }

    // Release resources
    public void close() {
        if (soundPool != null) {
            soundPool.release();
        }
    }

    public void addLineListener(LineListener listener)
    {

    }
}