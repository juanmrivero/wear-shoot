package com.juanmrivero.wearshoot.app;

import android.app.Application;

import java.util.concurrent.atomic.AtomicBoolean;

public class WearControlApp extends Application {

    private AtomicBoolean controlVisible = new AtomicBoolean(false);

    public void setControlVisible(boolean visible) {
        controlVisible.set(visible);
    }

    public boolean isControlVisible() {
        return controlVisible.get();
    }

}
