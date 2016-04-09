package com.juanmrivero.wearshoot.game.util;

public class Algebra {

    public static float distance(float x1, float y1, float x2, float y2) {
        double x = Math.pow(x2 - x1, 2);
        double y = Math.pow(y2 - y1, 2);

        return (float) Math.sqrt(x + y);
    }

    public static float getX(float distance, float angle) {
        return (float) (distance * Math.cos(Math.toRadians(angle)));
    }

    public static float getY(float distance, float angle) {
        return (float) (distance * Math.sin(Math.toRadians(angle)));
    }
}
