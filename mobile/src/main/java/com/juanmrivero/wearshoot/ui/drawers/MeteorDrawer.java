package com.juanmrivero.wearshoot.ui.drawers;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.juanmrivero.wearshoot.game.Meteor;


public class MeteorDrawer extends GameObjectDrawer<Meteor> {

    @Override
    protected void configurePaint(Paint paint) {
        paint.setColor(0xFF778899);
    }

    @Override
    protected void onDraw(Canvas canvas, Paint paint, Meteor meteor) {
        int x = toPixels(meteor.x);
        int y = toPixels(meteor.y);
        int radius = toPixels(meteor.width / 2);

        canvas.drawCircle(x, y, radius, paint);
    }

}
