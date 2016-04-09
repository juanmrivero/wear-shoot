package com.juanmrivero.wearshoot.game;

public class Lasers extends GameObjectPool<Laser> {

    public static final int MAX_LASERS = 5;

    public Lasers() {
        super(MAX_LASERS);
    }

    @Override
    protected Laser createObject() {
        return new Laser();
    }
}
