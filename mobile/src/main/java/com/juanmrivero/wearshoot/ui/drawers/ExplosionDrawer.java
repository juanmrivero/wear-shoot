package com.juanmrivero.wearshoot.ui.drawers;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.juanmrivero.wearshoot.game.Explosion;

import static com.juanmrivero.wearshoot.game.util.Algebra.getX;
import static com.juanmrivero.wearshoot.game.util.Algebra.getY;

public class ExplosionDrawer extends GameObjectDrawer<Explosion> {

    @Override
    protected void configurePaint(Paint paint) {
        paint.setColor(Color.WHITE);
    }

    @Override
    protected void onDraw(Canvas canvas, Paint paint, Explosion explosion) {
        int x = toPixels(explosion.x);
        int y = toPixels(explosion.y);
        int particleRadius = toPixels(explosion.particleRadius);
        float distance = explosion.distance * toPixels(explosion.width / 2);

        for (int angle = 0; angle < 360; angle += 45) {
            canvas.drawCircle(x + getX(distance, angle), y + getY(distance, angle), particleRadius, paint);
        }

    }

}
