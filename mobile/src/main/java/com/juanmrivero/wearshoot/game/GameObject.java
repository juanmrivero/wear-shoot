package com.juanmrivero.wearshoot.game;


import com.juanmrivero.wearshoot.game.util.Momentum;

public class GameObject {

    public float x;
    public float y;
    public float width = 15f;
    public float height = 8f;
    public float rotation;
    public boolean active = true;

    private Momentum momentum = new Momentum();

    final void onUpdate(World world) {
        if (active) {
            beforeUpdate(world);
            update(world);
            afterUpdate(world);
        }
    }

    protected void beforeUpdate(World world) {
    }

    private void update(World world) {
        applyMomentum();
    }

    protected void afterUpdate(World world) {
    }

    private void applyMomentum() {
        x += momentum.x;
        y += momentum.y;
    }

    public void applyLinearForce(float acceleration, float angle) {
        momentum.update(acceleration, angle);
    }

    public void reduceMomentum(float percentage) {
        momentum.reduce(percentage);
    }

    public void stopMomentum() {
        momentum.stop();
    }

    public void limitMomentum(float limit) {
        momentum.setLimit(limit);
    }

    public boolean isOutOfTheWorld(World world) {
        float threshold = Math.max(width, height) / 2;

        return (x - threshold > world.width)
                || (x + threshold < 0)
                || (y - threshold > world.height)
                || (y + threshold < 0);
    }
}
