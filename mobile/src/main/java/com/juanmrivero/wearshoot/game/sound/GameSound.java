package com.juanmrivero.wearshoot.game.sound;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import com.juanmrivero.utils.logging.Log;
import com.juanmrivero.wearshoot.R;

public class GameSound {

    private static final String TAG = GameSound.class.getName();

    private final SoundPool soundPool;
    private int laserId;
    private int meteorDestroyId;
    private int shipExplosionId;
    private int rocketId;
    private int rocketStream;

    public GameSound() {
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
    }

    public void initializeAsync(final Context context) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                loadSounds(context.getApplicationContext());
            }
        });
        thread.start();
    }

    private void loadSounds(Context context) {
        laserId = soundPool.load(context, R.raw.laser, 1);
        meteorDestroyId = soundPool.load(context, R.raw.meteor_destroy, 1);
        shipExplosionId = soundPool.load(context, R.raw.ship_explosion, 1);
        rocketId = soundPool.load(context, R.raw.rocket, 1);
    }

    public void playLaser() {
        playSound(laserId);
    }

    public void playMeteorDestroy() {
        playSound(meteorDestroyId);
    }

    public void playShipExplosion() {
        playSound(shipExplosionId);
    }

    public void startRocket() {
        if (rocketStream == 0) {
            rocketStream = soundPool.play(rocketId, 1.0f, 1.0f, 0, -1, 1.0f);
        }
    }

    public void stopRocket() {
        if (rocketStream != 0) {
            soundPool.stop(rocketStream);
            rocketStream = 0;
        }
    }

    private void playSound(int soundId) {
        int streamId = soundPool.play(soundId, 1.0f, 1.0f, 0, 0, 1.0f);

        if (streamId == 0) {
            Log.d(TAG, "playSound failed");
        }
    }

}
