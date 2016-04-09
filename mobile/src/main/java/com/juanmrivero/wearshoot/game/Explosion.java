package com.juanmrivero.wearshoot.game;


public class Explosion extends GameObject {

    public float particleRadius = 1.5f;
    public float distance = 0f;

    public Explosion() {
        active = false;
    }

    @Override
    protected void afterUpdate(World world) {
        distance += (1f - distance) * 0.07;

        if (distance > 0.85f) {
            particleRadius -= 0.05;
        }

        if (particleRadius < 0.2f) {
            active = false;
        }
    }

    public void reset() {
        particleRadius = 1.5f;
        distance = 0f;
    }
}
