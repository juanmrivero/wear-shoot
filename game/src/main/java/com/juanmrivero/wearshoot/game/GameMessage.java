package com.juanmrivero.wearshoot.game;

public enum GameMessage {
    START_WEAR_APP("startwearapp"),
    METEORITE_COUNT("meteoritecount"),
    GAME_OVER("gameover");

    private static final String BASE_PATH = "/juanmrivero/wearshoot/";

    private final String path;

    GameMessage(String path) {
        this.path = BASE_PATH + path;
    }

    public String getMessagePath() {
        return path;
    }

    public boolean is(String path) {
        return this.path.equals(path);
    }
}
