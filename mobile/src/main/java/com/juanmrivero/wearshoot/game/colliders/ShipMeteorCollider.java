package com.juanmrivero.wearshoot.game.colliders;

import com.juanmrivero.wearshoot.game.Meteor;
import com.juanmrivero.wearshoot.game.PlayerShip;

public class ShipMeteorCollider extends RadiusCollider<PlayerShip, Meteor> {

    @Override
    protected float getRadioSum(PlayerShip playerShip, Meteor meteor) {
        return meteor.width / 2 + Math.min(playerShip.width, playerShip.height) / 2;
    }

}
