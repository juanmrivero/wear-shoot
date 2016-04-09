package com.juanmrivero.wearshoot.ui.drawers;


import android.graphics.Canvas;

import com.juanmrivero.wearshoot.game.Explosions;
import com.juanmrivero.wearshoot.game.Lasers;
import com.juanmrivero.wearshoot.game.Meteors;
import com.juanmrivero.wearshoot.game.PlayerShip;
import com.juanmrivero.wearshoot.game.World;

public class WorldDrawer implements Drawer<World> {

    private PlayerShipDrawer playerShipDrawer;
    private MeteorDrawer meteorDrawer;
    private LaserDrawer laserDrawer;
    private ExplosionDrawer explosionDrawer;

    public WorldDrawer(World world) {
        laserDrawer = new LaserDrawer();
        playerShipDrawer = new PlayerShipDrawer(world.getPlayerShip());
        meteorDrawer = new MeteorDrawer();
        explosionDrawer = new ExplosionDrawer();
    }

    public final void setToPixelsScale(float scale) {
        explosionDrawer.setToPixelsScale(scale);
        playerShipDrawer.setToPixelsScale(scale);
        meteorDrawer.setToPixelsScale(scale);
        laserDrawer.setToPixelsScale(scale);
    }

    @Override
    public void onDraw(Canvas canvas, World world) {
        drawExplosions(canvas, world.getExplosions());
        drawLasers(canvas, world.getLasers());
        drawPlayerShip(canvas, world.getPlayerShip());
        drawMeteors(canvas, world.getMeteors());
    }

    private void drawLasers(Canvas canvas, Lasers lasers) {
        for (int i = 0; i < lasers.getCount(); i++) {
            laserDrawer.onDraw(canvas, lasers.get(i));
        }
    }

    private void drawExplosions(Canvas canvas, Explosions explosions) {
        for (int i = 0; i < explosions.getCount(); i++) {
            explosionDrawer.onDraw(canvas, explosions.get(i));
        }
    }

    private void drawPlayerShip(Canvas canvas, PlayerShip playerShip) {
        playerShipDrawer.onDraw(canvas, playerShip);
    }

    private void drawMeteors(Canvas canvas, Meteors meteors) {
        for (int i = 0; i < meteors.getCount(); i++) {
            meteorDrawer.onDraw(canvas, meteors.get(i));
        }
    }


}
