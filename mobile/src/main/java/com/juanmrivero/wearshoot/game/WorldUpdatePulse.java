package com.juanmrivero.wearshoot.game;


import com.juanmrivero.wearshoot.util.Pulse;

import java.util.concurrent.TimeUnit;

public class WorldUpdatePulse extends Pulse {

    private World gameWorld;

    public WorldUpdatePulse(World gameWorld) {
        super(newLooper("Game World update"), 16, TimeUnit.MILLISECONDS);
        this.gameWorld = gameWorld;
    }

    @Override
    protected void beat() {
        gameWorld.onUpdate();
    }
}
