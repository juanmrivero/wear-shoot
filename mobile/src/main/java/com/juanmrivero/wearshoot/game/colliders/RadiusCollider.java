package com.juanmrivero.wearshoot.game.colliders;

import com.juanmrivero.wearshoot.game.GameObject;
import com.juanmrivero.wearshoot.game.util.Algebra;

public abstract class RadiusCollider<T1 extends GameObject, T2 extends GameObject> extends Collider<T1, T2> {

    @Override
    protected boolean detectCollision(T1 gameObject1, T2 gameObject2) {
        float distance = Algebra.distance(gameObject1.x, gameObject1.y, gameObject2.x, gameObject2.y);

        return distance < getRadioSum(gameObject1, gameObject2);
    }

    protected abstract float getRadioSum(T1 gameObject1, T2 gameObject2);


}
