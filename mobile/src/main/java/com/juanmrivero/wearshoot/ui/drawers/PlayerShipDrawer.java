package com.juanmrivero.wearshoot.ui.drawers;


import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

import com.juanmrivero.wearshoot.game.PlayerShip;

public class PlayerShipDrawer extends TeleportGameObjectDrawer<PlayerShip> {

    private static final int SHIP_COLOR = 0xFFFFA500;
    private static final int ROCKET_COLOR = 0xFFEE2C2C;
    private static final int MAX_FIRE_OFFSET = -3;

    private final PlayerShip playerShip;

    private final Path shipShape;
    private final Path drawShipShape;

    private final Path rocketShape;
    private final Path drawRocketShape;
    private float rocketFireOffset;
    private float rocketFireOffsetChange = 1;

    public PlayerShipDrawer(PlayerShip playerShip) {
        this.playerShip = playerShip;

        shipShape = new Path();
        shipShape.setFillType(Path.FillType.EVEN_ODD);

        rocketShape = new Path();
        rocketShape.setFillType(Path.FillType.EVEN_ODD);

        drawShipShape = new Path();
        drawRocketShape = new Path();
    }

    @Override
    protected void onPixelScaleChange() {
        createShipShape(playerShip);
    }

    private void createShipShape(PlayerShip playerShip) {
        shipShape.reset();
        rocketShape.reset();

        float halfHeight = toPixels(playerShip.height / 2);
        float halfWidth = toPixels(playerShip.width / 2);

        shipShape.moveTo(-halfWidth, halfHeight);
        shipShape.lineTo(halfWidth, halfHeight);
        shipShape.lineTo(0f, -halfHeight);
        shipShape.close();

        rocketShape.moveTo(0, halfHeight * 2.5f);
        rocketShape.lineTo(-halfWidth * 0.6f, halfHeight);
        rocketShape.lineTo(halfWidth * 0.6f, halfHeight);
        rocketShape.close();

    }

    @Override
    protected void onDraw(Canvas canvas, Paint paint, PlayerShip drawObject, float x, float y) {
        int shipX = toPixels(x);
        int shipY = toPixels(y);

        canvas.save();
        canvas.rotate(playerShip.rotation, shipX, shipY);

        if (playerShip.rocketOn) {
            paint.setColor(ROCKET_COLOR);
            calculateRocketOffset();
            rocketShape.offset(shipX, shipY + rocketFireOffset, drawRocketShape);
            canvas.drawPath(drawRocketShape, paint);
        }


        paint.setColor(SHIP_COLOR);
        shipShape.offset(shipX, shipY, drawShipShape);
        canvas.drawPath(drawShipShape, paint);

        canvas.restore();

    }

    private void calculateRocketOffset() {
        rocketFireOffset -= rocketFireOffsetChange;

        if (rocketFireOffset == MAX_FIRE_OFFSET || rocketFireOffset == 0) {
            rocketFireOffsetChange *= -1;
        }
    }

}
