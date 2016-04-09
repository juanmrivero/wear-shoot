package com.juanmrivero.wearshoot.game.colliders;

import com.juanmrivero.wearshoot.game.Meteor;

public class MeteorMeteorCollider extends RadiusCollider<Meteor, Meteor> {

    @Override
    protected float getRadioSum(Meteor meteor1, Meteor meteor2) {
        return meteor1.width / 2 + meteor2.width / 2;
    }
}
