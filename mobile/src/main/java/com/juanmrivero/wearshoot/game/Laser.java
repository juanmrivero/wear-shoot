package com.juanmrivero.wearshoot.game;

public class Laser extends GameObject {

    public Laser() {
        width = 3f;
        height = 8f;
        active = false;
    }

    @Override
    protected void afterUpdate(World world) {
        if (isOutOfTheWorld(world)) {
            active = false;
            stopMomentum();
        }
    }

    public void reset() {
        active = false;
        stopMomentum();
    }
}
