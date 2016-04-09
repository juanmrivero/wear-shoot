package com.juanmrivero.wearshoot.game;

import android.os.SystemClock;

import java.util.Random;

public class GameRandom {

    private final Random random = new Random(SystemClock.elapsedRealtime());

    int onRange(int start, int end) {
        return start + random.nextInt(end - start);
    }

    <V> V pickOne(V... values) {
        return values[random.nextInt(values.length)];
    }

    float pickOne(float... values) {
        return values[random.nextInt(values.length)];
    }
}
