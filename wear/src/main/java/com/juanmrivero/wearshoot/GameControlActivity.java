package com.juanmrivero.wearshoot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.v4.content.LocalBroadcastManager;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.TextView;

import com.juanmrivero.utils.logging.Log;
import com.juanmrivero.wearshoot.app.WearControlApp;
import com.juanmrivero.wearshoot.com.juanmrivero.orientation.Orientation;
import com.juanmrivero.wearshoot.com.juanmrivero.orientation.OrientationProvider;
import com.juanmrivero.wearshoot.com.juanmrivero.wear.channel.WearChannel;
import com.juanmrivero.wearshoot.game.GameCommand;

import java.util.Locale;
import java.util.concurrent.TimeUnit;


public class GameControlActivity extends WearableActivity {

    private static final String TAG = GameControlActivity.class.getName();

    private static final float SPEED_THRESHOLD = 0.2f;
    private static final float TURN_THRESHOLD = 0.2f;

    private ProcessMessagesReceiver processMessagesReceiver = new ProcessMessagesReceiver();

    private View shootView;
    private TextView meteorCountView;
    private View gameOverView;

    private long gameOverTime;

    private OrientationProvider orientationProvider;
    private WearChannel wearChannel;
    private int previousValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setControlVisible(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_control_activity);

        shootView = findViewById(R.id.shoot_view);
        shootView.setOnClickListener(new SendShootOnClickListener());

        gameOverView = findViewById(R.id.game_over);
        gameOverView.setOnClickListener(new RestartGameListener());

        meteorCountView = (TextView) findViewById(R.id.meteor_count);


        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        orientationProvider = new OrientationProvider(sensorManager, SensorManager.SENSOR_DELAY_UI);

        wearChannel = new WearChannel(this);
    }

    private void setControlVisible(boolean visible) {
        ((WearControlApp) getApplicationContext()).setControlVisible(visible);
    }

    @Override
    protected void onResume() {
        super.onResume();
        wearChannel.start();
        orientationProvider.start(new ProcessOrientationListener());
        registerReceiver();
    }

    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BroadcastProtocol.METEOR_COUNT_ACTION);
        intentFilter.addAction(BroadcastProtocol.GAME_ACTIVE_ACTION);
        intentFilter.addAction(BroadcastProtocol.GAME_OVER_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(processMessagesReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        setControlVisible(false);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(processMessagesReceiver);
        wearChannel.stop();
        orientationProvider.stop();
        super.onPause();
    }

    public void processOrientation(Orientation orientation) {
        int value = getCommandValue(orientation.pitch, GameCommand.UP, GameCommand.DOWN, SPEED_THRESHOLD)
                | getCommandValue(orientation.roll, GameCommand.RIGHT, GameCommand.LEFT, TURN_THRESHOLD);

        sendValue((byte) value);
    }


    private int getCommandValue(float value,
                                GameCommand positiveCommand,
                                GameCommand negativeCommand,
                                float threshold) {
        GameCommand gameCommand;

        if (value > threshold) {
            gameCommand = positiveCommand;
        } else if (value < -threshold) {
            gameCommand = negativeCommand;
        } else {
            gameCommand = GameCommand.NONE;
        }

        return gameCommand.getCommandValue();
    }


    private void sendShoot() {
        sendValue((byte) (previousValue | GameCommand.SHOOT.getCommandValue()));
    }

    private void sendValue(byte value) {
        if (value != previousValue) {
            wearChannel.sendData(value);
            previousValue = value;
        }
    }

    private void gameOver(int meteorCount) {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(500);

        gameOverTime = SystemClock.elapsedRealtime();
        shootView.setVisibility(View.INVISIBLE);
        gameOverView.setVisibility(View.VISIBLE);
    }


    private class SendShootOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            sendShoot();
        }
    }

    private class ProcessOrientationListener implements OrientationProvider.OrientationListener {
        @Override
        public void onOrientation(Orientation orientation) {
            processOrientation(orientation);
        }
    }

    private class ProcessMessagesReceiver extends BroadcastReceiver {

        private int meteorCount;

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d(TAG, String.format(Locale.ENGLISH, "onMessageReceived %s", action));

            if (BroadcastProtocol.METEOR_COUNT_ACTION.equals(action)) {
                byte[] value = intent.getByteArrayExtra(BroadcastProtocol.KEY_METEOR_COUNT);
                meteorCount = value[0];
                meteorCountView.setText(String.format(Locale.ENGLISH, "%d", meteorCount));
            }

            if (BroadcastProtocol.GAME_ACTIVE_ACTION.equals(action)) {
                wearChannel.restart();
            }

            if (BroadcastProtocol.GAME_OVER_ACTION.equals(action)) {
                gameOver(meteorCount);
            }
        }

    }

    private class RestartGameListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (SystemClock.elapsedRealtime() - gameOverTime > TimeUnit.SECONDS.toMillis(3)) {
                shootView.setVisibility(View.VISIBLE);
                gameOverView.setVisibility(View.GONE);
                sendShoot();
            }
        }
    }
}