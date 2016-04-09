package com.juanmrivero.wearshoot.game;


import android.os.SystemClock;

import com.juanmrivero.utils.logging.Log;
import com.juanmrivero.wearshoot.game.colliders.LaserMeteorCollider;
import com.juanmrivero.wearshoot.game.colliders.MeteorMeteorCollider;
import com.juanmrivero.wearshoot.game.colliders.ShipMeteorCollider;
import com.juanmrivero.wearshoot.game.sound.GameSound;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class World {

    private static final String TAG = World.class.getName();

    public float width = 240f;
    public float height = 320f;

    private final GameSound gameSound;
    private final PlayerShip playerShip = new PlayerShip();
    private final Meteors meteors = new Meteors();
    private final Lasers lasers = new Lasers();
    private final Explosions explosions = new Explosions();

    private final MeteorMeteorCollider meteorMeteorCollider = new MeteorMeteorCollider();
    private final ShipMeteorCollider shipMeteorCollider = new ShipMeteorCollider();
    private final LaserMeteorCollider laserMeteorCollider = new LaserMeteorCollider();

    private byte meteorDestroyed;
    private WorldListener worldListener;

    private long gameOverTime;

    public World(GameSound gameSound,
                 WorldListener worldListener) {
        this.worldListener = worldListener;
        this.gameSound = gameSound;
        startGame();
    }

    public void onUpdate() {
        playerShip.onUpdate(this);
        meteors.onUpdate(this);
        lasers.onUpdate(this);
        explosions.onUpdate(this);
        checkCollisions();
    }

    private void checkCollisions() {
        checkMeteorsCollisions();
        checkLaserCollisions();
        checkShipCollision();
    }

    public void onGameCommandValue(int value) {
        Log.d(TAG, String.format(Locale.ENGLISH, "onGameCommandValue %d", value));

        if (playerShip.active) {
            playerShip.onGameCommandValue(value);
            checkShoot(value);
        } else if (GameCommand.SHOOT.is(value)) {
            startGame();
        }
    }

    private void checkShoot(int value) {
        if (GameCommand.SHOOT.is(value)) {
            Laser laser = lasers.getFirstInactive();
            if (laser != null) {
                laser.rotation = playerShip.rotation;
                laser.x = playerShip.x;
                laser.y = playerShip.y;
                laser.applyLinearForce(7f, laser.rotation - 90);
                laser.active = true;
                gameSound.playLaser();
            }
        }
    }

    private void startGame() {
        if (SystemClock.elapsedRealtime() - gameOverTime > TimeUnit.SECONDS.toMillis(3)) {

            playerShip.x = width / 2;
            playerShip.y = height / 2;
            playerShip.rotation = 0;
            playerShip.active = true;
            playerShip.stopMomentum();


            meteors.reset();
            explosions.reset();
            lasers.reset();

            meteorDestroyed = 0;
            worldListener.onMeteorDestroyed(meteorDestroyed);
        }
    }

    public PlayerShip getPlayerShip() {
        return playerShip;
    }

    public Meteors getMeteors() {
        return meteors;
    }

    public Lasers getLasers() {
        return lasers;
    }

    public GameSound getGameSound() {
        return gameSound;
    }

    public void updateHeight(float height) {
        this.height = height;
    }

    private void checkMeteorsCollisions() {
        Meteor meteor1;
        Meteor meteor2;

        for (int i = 0; i < meteors.getCount() - 1; i++) {
            meteor1 = meteors.get(i);
            for (int j = i + 1; j < meteors.getCount(); j++) {
                meteor2 = meteors.get(j);
                if (meteorMeteorCollider.collides(meteor1, meteor2)) {
                    meteor1.active = false;
                    meteor2.active = false;

                    explosions.makeExplosion(meteor1);
                    explosions.makeExplosion(meteor2);
                    gameSound.playMeteorDestroy();
                }
            }
        }
    }

    private void checkShipCollision() {
        Meteor meteor;

        for (int i = 0; i < meteors.getCount(); i++) {
            meteor = meteors.get(i);

            if (shipMeteorCollider.collides(playerShip, meteor)) {
                playerShip.active = false;
                explosions.makeExplosion(playerShip);
                gameSound.stopRocket();
                gameSound.playShipExplosion();
                gameOverTime = SystemClock.elapsedRealtime();
                worldListener.onGameOver();
            }
        }
    }

    private void checkLaserCollisions() {
        Laser laser;
        Meteor meteor;

        for (int i = 0; i < meteors.getCount(); i++) {
            meteor = meteors.get(i);
            for (int j = 0; j < lasers.getCount(); j++) {
                laser = lasers.get(j);

                if (laserMeteorCollider.collides(laser, meteor)) {
                    laser.reset();
                    meteor.active = false;
                    explosions.makeExplosion(meteor);
                    gameSound.playMeteorDestroy();
                    meteorDestroyed += 1;
                    worldListener.onMeteorDestroyed(meteorDestroyed);
                }
            }
        }
    }

    public Explosions getExplosions() {
        return explosions;
    }
}
