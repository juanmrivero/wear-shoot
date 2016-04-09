package com.juanmrivero.wearshoot.com.juanmrivero.graphics;

import android.graphics.Canvas;
import android.graphics.Paint;

public class LineF {

    public float x1;
    public float y1;
    public float x2;
    public float y2;

    public void setHorizontal(float x1, float x2,float y) {
        this.x1 = x1;
        this.x2 = x2;
        y1 = y;
        y2 = y;
    }

    public void setVertical(float y1, float y2, float x) {
        this.y1 = y1;
        this.y2 = y2;
        x1 = x;
        x2 = x;
    }

    public void draw(Canvas canvas, Paint paint) {
        canvas.drawLine(x1, y1, x2, y2, paint);
    }
}
