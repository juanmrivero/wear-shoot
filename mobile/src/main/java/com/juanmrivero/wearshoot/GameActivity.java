package com.juanmrivero.wearshoot;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.juanmrivero.utils.logging.Log;
import com.juanmrivero.wearshoot.game.GameMessage;
import com.juanmrivero.wearshoot.game.World;
import com.juanmrivero.wearshoot.game.WorldListener;
import com.juanmrivero.wearshoot.game.WorldUpdatePulse;
import com.juanmrivero.wearshoot.game.sound.GameSound;
import com.juanmrivero.wearshoot.googleapi.GameCommandsConnection;
import com.juanmrivero.wearshoot.googleapi.GameMessageConnection;
import com.juanmrivero.wearshoot.ui.GamePaintPulse;
import com.juanmrivero.wearshoot.ui.GameView;

import java.io.InputStream;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class GameActivity extends AppCompatActivity implements
        GameCommandsConnection.DeviceChannelListener,
        GameMessageConnection.GameMessageListener,
        WorldListener {

    private static final String TAG = GameActivity.class.getName();
    private Executor executor = Executors.newSingleThreadExecutor();

    private GameMessageConnection gameMessageConnection;
    private GameCommandsConnection gameCommandsConnection;
    private GameInput gameInput;

    private WorldUpdatePulse worldUpdatePulse;
    private GamePaintPulse gamePaintPulse;
    private GameView gameView;
    private World gameWorld;
    private View noWatchView;
    private Animation noWatchAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        gameCommandsConnection = new GameCommandsConnection(this);
        gameMessageConnection = new GameMessageConnection(this);

        createWorld();
        createGameView();
        createNoWatchView();

        worldUpdatePulse = new WorldUpdatePulse(gameWorld);
        gamePaintPulse = new GamePaintPulse(gameView);
    }

    private void createWorld() {
        GameSound gameSound = new GameSound();
        gameSound.initializeAsync(this);
        gameWorld = new World(gameSound, this);
    }

    private void createGameView() {
        gameView = (GameView) findViewById(R.id.game_view);
        if (gameView != null) {
            gameView.setGameWorld(gameWorld);
        }
    }

    private void createNoWatchView() {
        noWatchView = findViewById(R.id.no_watch);
        noWatchAnimation = AnimationUtils.loadAnimation(this, R.anim.blink);
        noWatchView.startAnimation(noWatchAnimation);
    }

    private void setFullScreen() {
        if (gameView != null) {
            gameView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setFullScreen();
        gameMessageConnection.start(this);
        gameCommandsConnection.start(this);
        worldUpdatePulse.start();
        gamePaintPulse.start();

    }

    @Override
    protected void onPause() {
        gamePaintPulse.stop();
        worldUpdatePulse.stop();
        gameMessageConnection.stop();
        gameCommandsConnection.stop();
        stopGameInput();
        super.onPause();
    }

    private void stopGameInput() {
        if (gameInput != null) {
            gameInput.cancel(false);
            gameInput = null;
        }
        noWatchView.setVisibility(View.VISIBLE);
        noWatchView.startAnimation(noWatchAnimation);
    }

    @Override
    public void onStreamAvailable(InputStream inputStream) {
        Log.d(TAG, "onStreamAvailable");
        startInput(inputStream);
    }

    @Override
    public void onStreamNotAvailable() {
        Log.d(TAG, "onStreamNotAvailable");
        stopGameInput();
    }

    private void startInput(InputStream inputStream) {
        noWatchView.clearAnimation();
        noWatchView.setVisibility(View.GONE);
        gameInput = new GameInput();
        gameInput.executeOnExecutor(executor, inputStream);
    }

    @Override
    public void onGameMessageReady() {
        gameMessageConnection.sendMessage(GameMessage.START_WEAR_APP);
    }

    @Override
    public void onMeteorDestroyed(byte count) {
        gameMessageConnection.sendMessage(GameMessage.METEORITE_COUNT, new byte[]{count});
    }

    @Override
    public void onGameOver() {
        gameMessageConnection.sendMessage(GameMessage.GAME_OVER);
    }

    private class GameInput extends DataAsyncTask {

        @Override
        protected void onProgressUpdate(Integer... values) {
            Log.d(TAG, String.format(Locale.ENGLISH, "onProgressUpdate %d", values[0]));
            gameWorld.onGameCommandValue(values[0]);
        }

    }


}
