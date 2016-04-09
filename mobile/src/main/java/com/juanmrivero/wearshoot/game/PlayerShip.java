package com.juanmrivero.wearshoot.game;


import com.juanmrivero.wearshoot.game.sound.GameSound;

public class PlayerShip extends TeleportGameObject {

    private static final float ACCELERATION_RATE = 0.02f;
    private static final float BREAK_RATE = 0.02f;
    private static final float ROTATION_RATE = 2.0f;

    public boolean rocketOn;
    public boolean breakOn;
    public boolean turnLeft;
    public boolean turnRight;

    private float rotationStep;

    public PlayerShip() {
        width = 15f;
        height = 8f;
        limitMomentum(3f);
    }

    void onGameCommandValue(int value) {

        rocketOn = GameCommand.UP.is(value);
        breakOn = GameCommand.DOWN.is(value);
        turnLeft = GameCommand.LEFT.is(value);
        turnRight = GameCommand.RIGHT.is(value);

    }

    @Override
    protected void beforeUpdate(World world) {
        updateRotation();
        updateRocketAction(world.getGameSound());
        updateBreakAction();
    }

    private void updateRotation() {
        if (turnLeft) {
            rotation -= ROTATION_RATE;
        } else if (turnRight) {
            rotation += ROTATION_RATE;
        }
    }

    private void updateRocketAction(GameSound gameSound) {
        if (rocketOn) {
            applyLinearForce(ACCELERATION_RATE, rotation - 90f);
            gameSound.startRocket();
        } else {
            gameSound.stopRocket();
        }
    }

    private void updateBreakAction() {
        if (breakOn) {
            reduceMomentum(BREAK_RATE);
        }
    }

}
