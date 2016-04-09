package com.juanmrivero.wearshoot.ui.drawers;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.juanmrivero.wearshoot.game.TeleportGameObject;

public class TeleportGameObjectDrawer<T extends TeleportGameObject> extends GameObjectDrawer<T> {

    @Override
    protected final void onDraw(Canvas canvas, Paint paint, T drawObject) {
        onDraw(canvas, paint, drawObject, drawObject.x, drawObject.y);
        if (drawObject.needSecondDraw) {
            onDraw(canvas, paint, drawObject, drawObject.secondDrawX, drawObject.secondDrawY);
        }
    }

    protected void onDraw(Canvas canvas, Paint paint, T drawObject, float x, float y) {
    }
}
