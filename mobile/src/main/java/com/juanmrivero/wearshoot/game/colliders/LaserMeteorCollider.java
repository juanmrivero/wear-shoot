package com.juanmrivero.wearshoot.game.colliders;

import com.juanmrivero.wearshoot.game.Laser;
import com.juanmrivero.wearshoot.game.Meteor;

public class LaserMeteorCollider extends RadiusCollider<Laser, Meteor> {

    @Override
    protected float getRadioSum(Laser laser, Meteor meteor) {
        return (laser.width + meteor.width) / 2;
    }
}
