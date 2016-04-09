package com.juanmrivero.wearshoot.ui.drawers;


import android.graphics.Canvas;
import android.graphics.Paint;

import com.juanmrivero.wearshoot.game.GameObject;

public class GameObjectDrawer<T extends GameObject> implements Drawer<T> {

    private final Paint paint;
    private float toPixelsScale = 1f;

    public GameObjectDrawer() {
        this.paint = new Paint();
        configurePaint(paint);
    }

    protected void configurePaint(Paint paint) {
    }

    public final void setToPixelsScale(float toPixelsScale) {
        if (this.toPixelsScale != toPixelsScale) {
            this.toPixelsScale = toPixelsScale;
            onPixelScaleChange();
        }
    }

    protected void onPixelScaleChange() {
    }

    protected int toPixels(float gameValue) {
        return (int) (gameValue * toPixelsScale);
    }

    @Override
    public final void onDraw(Canvas canvas, T drawObject) {
        if (drawObject.active) {
            onDraw(canvas, paint, drawObject);
        }
    }

    protected void onDraw(Canvas canvas, Paint paint, T drawObject) {
    }

}
