package com.juanmrivero.wearshoot.game.util;

import com.juanmrivero.wearshoot.game.util.Algebra;

public class Momentum {

    public float x;
    public float y;
    private float limit;

    public void update(float newAcceleration, float newAngle) {
        float newX = getXOffset(newAcceleration, newAngle);
        float newY = getYOffset(newAcceleration, newAngle);

        if (checkLimit(newX, newY)) {
            x += newX;
            y += newY;
        }
    }

    private boolean checkLimit(float newX, float newY) {
        return limit <= 0 || limit >= Algebra.distance(0, 0, x + newX, y + newY);
    }

    //percent should be between 0 and 1
    public void reduce(float percent) {
        x -= x * percent;
        y -= y * percent;
    }

    public void stop() {
        x = 0f;
        y = 0f;
    }

    public void setLimit(float limit) {
        this.limit = limit;
    }

    private float getXOffset(float acceleration, float angle) {
        return (float) (acceleration * Math.cos(Math.toRadians(angle)));
    }

    public float getYOffset(float acceleration, float angle) {
        return (float) (acceleration * Math.sin(Math.toRadians(angle)));
    }


}
