package com.juanmrivero.wearshoot.game;

public class Meteor extends GameObject {

    public Meteor(float circumference) {
        width = circumference;
        height = circumference;
    }

    @Override
    protected void afterUpdate(World world) {
        if (isOutOfTheWorld(world)) {
            active = false;
            stopMomentum();
        }
    }

}
