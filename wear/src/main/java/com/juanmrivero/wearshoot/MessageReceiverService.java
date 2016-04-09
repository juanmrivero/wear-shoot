package com.juanmrivero.wearshoot;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;
import com.juanmrivero.utils.logging.Log;
import com.juanmrivero.wearshoot.app.WearControlApp;
import com.juanmrivero.wearshoot.game.GameMessage;

import java.util.Locale;


public class MessageReceiverService extends WearableListenerService {

    private static final String TAG = MessageReceiverService.class.getName();

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d(TAG, String.format(Locale.ENGLISH, "onMessageReceived %s", messageEvent));

        String path = messageEvent.getPath();

        if (GameMessage.START_WEAR_APP.is(path)) {
            notifyStartControl();
        } else if (GameMessage.METEORITE_COUNT.is(path)) {
            notifyMeteorCount(messageEvent.getData());
        } else if (GameMessage.GAME_OVER.is(path)) {
            notifyAction(BroadcastProtocol.GAME_OVER_ACTION);
        }

    }

    private void notifyStartControl() {
        boolean isControlVisible = ((WearControlApp) getApplicationContext()).isControlVisible();
        if (isControlVisible) {
            notifyAction(BroadcastProtocol.GAME_ACTIVE_ACTION);
        } else {
            startApp();
        }
    }

    private void startApp() {
        Log.d(TAG, "startApp");
        Intent intent = new Intent(getApplicationContext(), GameControlActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void notifyMeteorCount(byte[] data) {
        Log.d(TAG, String.format(Locale.ENGLISH, "notifyMeteorCount %s", data));
        Intent intent = new Intent(BroadcastProtocol.METEOR_COUNT_ACTION);
        intent.putExtra(BroadcastProtocol.KEY_METEOR_COUNT, data);

        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void notifyAction(String action) {
        Log.d(TAG, String.format(Locale.ENGLISH, "notifyAction %s", action));
        Intent intent = new Intent(action);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
