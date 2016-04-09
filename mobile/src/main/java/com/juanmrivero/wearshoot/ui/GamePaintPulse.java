package com.juanmrivero.wearshoot.ui;

import android.os.Looper;

import com.juanmrivero.wearshoot.util.Pulse;

import java.util.concurrent.TimeUnit;


public class GamePaintPulse extends Pulse {

    private GameView gameView;

    public GamePaintPulse(GameView gameView) {
        super(uiLooper(), 16, TimeUnit.MILLISECONDS);
        this.gameView = gameView;
    }

    @Override
    protected void beat() {
        gameView.invalidate();
    }

}
