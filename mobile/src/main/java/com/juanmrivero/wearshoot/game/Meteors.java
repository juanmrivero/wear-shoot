package com.juanmrivero.wearshoot.game;

import android.os.SystemClock;

import java.util.Random;

public class Meteors extends GameObjectPool<Meteor> {

    private static final int MAX_METEORS = 8;

    private final static Random RANDOM = new Random(SystemClock.elapsedRealtime());

    public Meteors() {
        super(MAX_METEORS);
    }

    @Override
    protected Meteor createObject() {
        return new Meteor(randomizeCircumference());
    }


    private float randomizeCircumference() {return 12f + RANDOM.nextInt(12);}


    @Override
    public void onUpdate(World world) {
        randomizeMeteorActivation(world);
        super.onUpdate(world);
    }

    private void randomizeMeteorActivation(World world) {
        if (isTimeToActivate()) {
            Meteor meteor = getFirstInactive();
            if (meteor != null) {
                activateMeteor(world, meteor);
            }
        }
    }

    private boolean isTimeToActivate() {
        return RANDOM.nextFloat() < 0.05;
    }

    private void activateMeteor(World world, Meteor meteor) {
        meteor.stopMomentum();
        meteor.width = randomizeCircumference();
        meteor.height = meteor.width;
        randomizeStartEdge(world, meteor);

        meteor.active = true;
    }

    private void randomizeStartEdge(World world, Meteor meteor) {
        switch (RANDOM.nextInt(4)) {
            case 0:
                startLeft(world, meteor);
                break;
            case 1:
                startRight(world, meteor);
                break;
            case 2:
                startTop(world, meteor);
                break;
            case 3:
                startBottom(world, meteor);
                break;
        }
    }

    private void startLeft(World world, Meteor meteor) {
        meteor.x = -meteor.width / 2;
        meteor.y = RANDOM.nextFloat() * world.height;
        randomizeMeteorMovement(meteor, -45);
    }

    private void startRight(World world, Meteor meteor) {
        meteor.x = world.width + meteor.width / 2;
        meteor.y = RANDOM.nextFloat() * world.height;
        randomizeMeteorMovement(meteor, 135);
    }

    private void startTop(World world, Meteor meteor) {
        meteor.x = RANDOM.nextFloat() * world.width;
        meteor.y = -meteor.height / 2;
        randomizeMeteorMovement(meteor, 225);
    }

    private void startBottom(World world, Meteor meteor) {
        meteor.x = RANDOM.nextFloat() * world.width;
        meteor.y = world.height + meteor.height / 2;
        randomizeMeteorMovement(meteor, 45);
    }

    private void randomizeMeteorMovement(Meteor meteor, int startAngle) {
        meteor.applyLinearForce(0.5f + (RANDOM.nextInt(4) / 10), startAngle + RANDOM.nextInt(90));
    }

}
