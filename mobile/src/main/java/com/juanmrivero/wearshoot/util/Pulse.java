package com.juanmrivero.wearshoot.util;


import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import java.util.concurrent.TimeUnit;

public abstract class Pulse {

    private static final int ACTION = 1;
    private static final int STOP = 2;

    private Handler handler;
    private long rhythm;

    public Pulse(Looper looper, long rhythmTime, TimeUnit rhythmTimeUnit) {
        handler = new Handler(looper, new BeatCallback());
        rhythm = rhythmTimeUnit.toMillis(rhythmTime);
    }

    public void start() {
        handler.sendEmptyMessage(ACTION);
    }

    public void stop() {
        handler.sendEmptyMessage(STOP);
    }

    protected abstract void beat();

    public static Looper uiLooper() {
        return Looper.getMainLooper();
    }

    public static Looper newLooper(String threadName) {
        HandlerThread handlerThread = new HandlerThread(threadName);
        handlerThread.start();
        return handlerThread.getLooper();
    }

    private class BeatCallback implements Handler.Callback {

        @Override
        public boolean handleMessage(Message message) {
            if (message.what == ACTION) {
                beat();
                handler.sendEmptyMessageDelayed(ACTION, rhythm);
            } else {
                handler.removeMessages(ACTION);
            }

            return true;
        }
    }

}
