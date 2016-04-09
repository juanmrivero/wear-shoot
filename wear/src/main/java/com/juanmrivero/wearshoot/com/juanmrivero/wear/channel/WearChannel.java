package com.juanmrivero.wearshoot.com.juanmrivero.wear.channel;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.wearable.Channel;
import com.google.android.gms.wearable.ChannelApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;
import com.juanmrivero.googleapi.WearNodeConnection;
import com.juanmrivero.utils.logging.Log;
import com.juanmrivero.wearshoot.game.GameCommand;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Locale;

public class WearChannel extends WearNodeConnection {

    private static final String TAG = WearChannel.class.getName();

    private static final String GAME_CAPABILITY = "juanmrivero_wear_shoot";
    private static final String CHANNEL_PATH = "/juanmrivero_wear_shoot";

    private Channel channel;
    private OutputStream outputStream;

    public WearChannel(Context context) {
        super(context, GAME_CAPABILITY);
    }

    public void stop() {
        closeOutputStream();
        closeChannel();
        super.stop();
    }

    public void restart() {
        Log.d(TAG, "restart");
        stop();
        start();
    }

    private void closeOutputStream() {
        if (outputStream != null) {
            try {
                outputStream.close();
            } catch (IOException e) {
                Log.d(TAG, "close stream error", e);
            }
        }
    }

    private void closeChannel() {
        if (channel != null) {
            PendingResult<Status> pendingResult = channel.close(getGoogleApiClient());
            pendingResult.setResultCallback(new LogCloseChannel());
        }
    }

    public void sendData(byte data) {
        try {
            if (outputStream != null) {
                Log.d(TAG, String.format(Locale.ENGLISH, "sending %d", data));
                outputStream.write(data);
                outputStream.flush();
            } else {
                Log.d(TAG, "No output stream");
                if (GameCommand.SHOOT.is(data)) {
                    restart();
                }
            }
        } catch (IOException e) {
            Log.d(TAG, "error sending data", e);
        }
    }

    @Override
    protected void onNodeFound(@NonNull Node node) {
        openChannel();
    }

    private void openChannel() {
        Wearable.ChannelApi.addListener(getGoogleApiClient(), new WearChannelListener());

        PendingResult<ChannelApi.OpenChannelResult> openChannel =
                Wearable.ChannelApi.openChannel(
                        getGoogleApiClient(),
                        getNodeId(),
                        CHANNEL_PATH);

        openChannel.setResultCallback(new OpenChannelListener());
    }

    private void openStream() {
        PendingResult<Channel.GetOutputStreamResult> outputStreamResult = channel.getOutputStream(getGoogleApiClient());
        outputStreamResult.setResultCallback(new OpenOutputStreamListener());
    }

    private class OpenChannelListener implements ResultCallback<ChannelApi.OpenChannelResult> {
        @Override
        public void onResult(@NonNull ChannelApi.OpenChannelResult openChannelResult) {
            if (openChannelResult.getStatus().isSuccess()) {
                channel = openChannelResult.getChannel();
                Log.d(TAG, String.format(Locale.ENGLISH, "Channel %s", channel));
                openStream();
            } else {
                Log.d(TAG, "open channel error");
            }
        }
    }

    private class OpenOutputStreamListener implements ResultCallback<Channel.GetOutputStreamResult> {
        @Override
        public void onResult(@NonNull Channel.GetOutputStreamResult outputStreamResult) {
            if (outputStreamResult.getStatus().isSuccess()) {
                outputStream = outputStreamResult.getOutputStream();
                Log.d(TAG, String.format(Locale.ENGLISH, "Out created  %s", outputStream));
            } else {
                Log.d(TAG, "open output stream failed");
            }
        }
    }

    private class LogCloseChannel implements ResultCallback<Status> {
        @Override
        public void onResult(@NonNull Status status) {
            Log.d(TAG, String.format(Locale.ENGLISH, "Stream result %s", status));
        }
    }

    private class WearChannelListener implements ChannelApi.ChannelListener {

        @Override
        public void onChannelOpened(Channel channel) {
            Log.d(TAG, String.format(Locale.ENGLISH, "onChannelOpened %s", channel));
        }

        @Override
        public void onChannelClosed(Channel channel, int closeReason, int appSpecificErrorCode) {
            Log.d(TAG, String.format(Locale.ENGLISH, "onChannelClosed %s, %d, %d", channel, closeReason, appSpecificErrorCode));
        }

        @Override
        public void onInputClosed(Channel channel, int closeReason, int appSpecificErrorCode) {
            Log.d(TAG, String.format(Locale.ENGLISH, "onInputClosed %s, %d, %d", channel, closeReason, appSpecificErrorCode));
        }

        @Override
        public void onOutputClosed(Channel channel, int closeReason, int appSpecificErrorCode) {
            Log.d(TAG, String.format(Locale.ENGLISH, "onOutputClosed %s, %d, %d", channel, closeReason, appSpecificErrorCode));
            outputStream = null;
        }
    }
}
