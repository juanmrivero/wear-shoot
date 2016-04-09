package com.juanmrivero.wearshoot.googleapi;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.wearable.Channel;
import com.google.android.gms.wearable.ChannelApi;
import com.google.android.gms.wearable.Wearable;
import com.juanmrivero.googleapi.GoogleApiConnection;
import com.juanmrivero.utils.logging.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;


public class GameCommandsConnection extends GoogleApiConnection {

    private static final String TAG = GameCommandsConnection.class.getName();

    @Nullable private DeviceChannelListener deviceChannelListener;
    @Nullable private Channel channel;
    @Nullable private InputStream inputStream;
    @NonNull private final OpenStreamChannelListener openStreamChannelListener;

    public GameCommandsConnection(@NonNull Context context) {
        super(context);
        openStreamChannelListener = new OpenStreamChannelListener();
    }

    @Override
    protected void configure(GoogleApiClient.Builder builder) {
        builder.addApi(Wearable.API);
    }

    public void start(DeviceChannelListener deviceChannelListener) {
        this.deviceChannelListener = deviceChannelListener;
        super.start();
    }

    @Override
    public void stop() {
        removeChannelListener();
        this.deviceChannelListener = null;
        closeStream();
        closeChannel();
        super.stop();
    }

    private void removeChannelListener() {
        GoogleApiClient googleApiClient = getGoogleApiClient();
        if (googleApiClient != null) {
            Wearable.ChannelApi.removeListener(googleApiClient, openStreamChannelListener);
        }
    }

    private void closeStream() {
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                Log.d(TAG, "closeStream error", e);
            }
        }
    }

    private void closeChannel() {
        if (channel != null) {
            PendingResult<Status> pendingResult = channel.close(getGoogleApiClient());
            pendingResult.setResultCallback(new LogCloseChannel());
        }
    }

    @Override
    protected void onConnected(GoogleApiClient googleApiClient) {
        Wearable.ChannelApi.addListener(googleApiClient, openStreamChannelListener);
    }

    private void openStream(Channel channel) {
        closeChannel();
        GameCommandsConnection.this.channel = channel;

        Log.d(TAG, "openStream");
        PendingResult<Channel.GetInputStreamResult> inputStream = channel.getInputStream(getGoogleApiClient());
        inputStream.setResultCallback(new InputStreamListener());
    }

    private void notifyStreamAvailable(InputStream inputStream) {
        this.inputStream = inputStream;
        Log.d(TAG, String.format(Locale.ENGLISH, "notifyStreamAvailable %s", deviceChannelListener));
        if (deviceChannelListener != null) {
            deviceChannelListener.onStreamAvailable(inputStream);
        }
    }

    private void notifyStreamNotAvailable() {
        this.inputStream = null;
        Log.d(TAG, "notifyStreamNotAvailable");
        if (deviceChannelListener != null) {
            deviceChannelListener.onStreamNotAvailable();
        }
    }


    private class OpenStreamChannelListener implements ChannelApi.ChannelListener {

        @Override
        public void onChannelOpened(Channel channel) {
            Log.d(TAG, String.format(Locale.ENGLISH, "onChannelOpened %s", channel));
            openStream(channel);
        }

        @Override
        public void onChannelClosed(Channel channel, int closeReason, int appSpecificErrorCode) {
            Log.d(TAG, String.format(Locale.ENGLISH, "onChannelClosed %s, %d, %d", channel, closeReason, appSpecificErrorCode));
        }

        @Override
        public void onInputClosed(Channel channel, int closeReason, int appSpecificErrorCode) {
            Log.d(TAG, String.format(Locale.ENGLISH, "onInputClosed %s, %d, %d", channel, closeReason, appSpecificErrorCode));
            notifyStreamNotAvailable();
        }

        @Override
        public void onOutputClosed(Channel channel, int closeReason, int appSpecificErrorCode) {
            Log.d(TAG, String.format(Locale.ENGLISH, "onOutputClosed %s, %d, %d", channel, closeReason, appSpecificErrorCode));
        }
    }

    private class InputStreamListener implements ResultCallback<Channel.GetInputStreamResult> {

        @Override
        public void onResult(@NonNull Channel.GetInputStreamResult inputStreamResult) {
            Log.d(TAG, String.format(Locale.ENGLISH, "Stream result %s", inputStreamResult));

            if (inputStreamResult.getStatus().isSuccess()) {
                InputStream inputStream = inputStreamResult.getInputStream();
                if (inputStream != null) {
                    notifyStreamAvailable(inputStream);
                }

            }
        }
    }

    private class LogCloseChannel implements ResultCallback<Status> {
        @Override
        public void onResult(@NonNull Status status) {
            Log.d(TAG, String.format(Locale.ENGLISH, "Stream result %s", status));
        }
    }

    public interface DeviceChannelListener {
        void onStreamAvailable(InputStream inputStream);

        void onStreamNotAvailable();
    }

}
