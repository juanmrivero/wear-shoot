package com.juanmrivero.wearshoot.game;

public enum GameCommand {
    NONE(0),
    UP(1),
    DOWN(2),
    LEFT(4),
    RIGHT(8),
    SHOOT(16),
    SHIELD(32);

    private final int value;

    GameCommand(int value) {
        this.value = value;
    }

    public int getCommandValue() {
        return value;
    }

    public boolean is(int value) {
        return this.value == (value & this.value);
    }

}
