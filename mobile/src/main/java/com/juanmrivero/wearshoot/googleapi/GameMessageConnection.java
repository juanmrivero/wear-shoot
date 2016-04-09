package com.juanmrivero.wearshoot.googleapi;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;
import com.juanmrivero.googleapi.WearNodeConnection;
import com.juanmrivero.utils.logging.Log;
import com.juanmrivero.wearshoot.game.GameMessage;

import java.util.Locale;


public class GameMessageConnection extends WearNodeConnection {

    private static final String TAG = GameMessageConnection.class.getName();

    private static final String GAME_CAPABILITY = "juanmrivero_wear_shoot";

    @Nullable private GameMessageListener gameMessageListener;

    public GameMessageConnection(@NonNull Context context) {
        super(context, GAME_CAPABILITY);
    }

    public void start(GameMessageListener gameMessageListener) {
        this.gameMessageListener = gameMessageListener;
        super.start();
    }

    public void stop() {
        gameMessageListener = null;
        super.stop();
    }

    @Override
    protected void onNodeFound(@NonNull Node node) {
        if (gameMessageListener != null) {
            gameMessageListener.onGameMessageReady();
        }
    }

    public void sendMessage(GameMessage gameMessage) {
        sendMessage(gameMessage, null);
    }

    public void sendMessage(GameMessage gameMessage, byte[] data) {
        GoogleApiClient googleApiClient = getGoogleApiClient();
        String nodeId = getNodeId();

        if (googleApiClient != null && nodeId != null) {
            PendingResult<MessageApi.SendMessageResult> pendingResult =
                    Wearable.MessageApi.sendMessage(
                            googleApiClient,
                            nodeId,
                            gameMessage.getMessagePath(),
                            data
                    );

            pendingResult.setResultCallback(new LogSendMessage());
        }
    }

    private class LogSendMessage implements ResultCallback<MessageApi.SendMessageResult> {
        @Override
        public void onResult(@NonNull MessageApi.SendMessageResult sendMessageResult) {
            Log.d(TAG, String.format(Locale.ENGLISH, "onResult %s", sendMessageResult.getStatus()));
        }
    }

    public interface GameMessageListener {
        void onGameMessageReady();
    }

}
