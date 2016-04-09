package com.juanmrivero.wearshoot.game;


public class Explosions extends GameObjectPool<Explosion> {

    private static final int MAX_EXPLOSIONS = 20;

    public Explosions() {
        super(MAX_EXPLOSIONS);
    }

    @Override
    protected Explosion createObject() {
        return new Explosion();
    }

    public void makeExplosion(GameObject gameObject) {
        Explosion explosion = getFirstInactive();
        explosion.reset();
        explosion.x = gameObject.x;
        explosion.y = gameObject.y;
        explosion.width = gameObject.width;
        explosion.height = gameObject.height;
        explosion.active = true;
    }
}
