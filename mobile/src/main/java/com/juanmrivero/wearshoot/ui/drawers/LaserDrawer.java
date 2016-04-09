package com.juanmrivero.wearshoot.ui.drawers;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.juanmrivero.wearshoot.game.Laser;

public class LaserDrawer extends GameObjectDrawer<Laser> {

    @Override
    protected void configurePaint(Paint paint) {
        paint.setColor(0xFFDDDDDD);
    }

    @Override
    protected void onDraw(Canvas canvas, Paint paint, Laser laser) {
        int x = toPixels(laser.x);
        int y = toPixels(laser.y);
        int width = toPixels(laser.width);
        int height = toPixels(laser.height);

        canvas.save();
        canvas.rotate(laser.rotation, x, y);
        canvas.drawRect(
                x - width / 2,
                y - height / 2,
                x + width / 2,
                y + height / 2,
                paint
        );

        canvas.restore();

    }
}
