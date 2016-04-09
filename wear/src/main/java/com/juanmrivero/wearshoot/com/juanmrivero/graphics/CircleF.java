package com.juanmrivero.wearshoot.com.juanmrivero.graphics;

import android.graphics.Canvas;
import android.graphics.Paint;

public class CircleF {

    public float x;
    public float y;
    public float radius;

    public void set(float x, float y, float radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    public void draw(Canvas canvas, Paint paint) {
        canvas.drawCircle(x, y, radius, paint);
    }

}
