package com.juanmrivero.wearshoot.game;

public class TeleportGameObject extends GameObject {

    public boolean needSecondDraw;
    public float secondDrawX;
    public float secondDrawY;

    @Override
    protected void afterUpdate(World world) {
        checkTeleportEffects(world);
    }

    private void checkTeleportEffects(World world) {
        float threshold = Math.max(width, height) / 2;
        checkTeleport(world, threshold);
        checkSecondDraw(world, threshold);
    }

    private void checkTeleport(World world, float threshold) {
        if (x - threshold > world.width) {
            x = secondDrawX;
        } else if (x + threshold < 0) {
            x = secondDrawX;
        }

        if (y - threshold > world.height) {
            y = -threshold;
            y = secondDrawY;
        } else if (y + threshold < 0) {
            y = world.height + threshold;
            y = secondDrawY;
        }
    }

    private void checkSecondDraw(World world, float threshold) {
        needSecondDraw = false;

        if (x - threshold < 0) {
            secondDrawX = world.width + x;
            needSecondDraw = true;
        } else if (x + threshold > world.width) {
            secondDrawX = x - world.width;
            needSecondDraw = true;
        } else {
            secondDrawX = x;
        }

        if (y - threshold < 0) {
            secondDrawY = world.height + y;
            needSecondDraw = true;
        } else if (y + threshold > world.height) {
            secondDrawY = y - world.height;
            needSecondDraw = true;
        } else {
            secondDrawY = y;
        }
    }

}
