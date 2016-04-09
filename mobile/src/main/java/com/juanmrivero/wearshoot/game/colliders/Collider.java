package com.juanmrivero.wearshoot.game.colliders;

import com.juanmrivero.wearshoot.game.GameObject;


public abstract class Collider<T1 extends GameObject, T2 extends GameObject> {

    public final boolean collides(T1 gameObject1, T2 gameObject2) {
        return isActive(gameObject1) && isActive(gameObject2)
                && detectCollision(gameObject1, gameObject2);
    }

    protected abstract boolean detectCollision(T1 gameObject1, T2 gameObject2);

    private boolean isActive(GameObject gameObject) {
        return gameObject != null && gameObject.active;
    }
}
