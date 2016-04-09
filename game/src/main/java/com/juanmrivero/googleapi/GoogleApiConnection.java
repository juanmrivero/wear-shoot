package com.juanmrivero.googleapi;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.juanmrivero.utils.logging.Log;

import java.util.Locale;

public abstract class GoogleApiConnection {

    private static final String TAG = GoogleApiConnection.class.getName();

    @NonNull private final Context context;
    @Nullable private GoogleApiClient googleApiClient;

    public GoogleApiConnection(@NonNull Context context) {
        this.context = context.getApplicationContext();
    }

    @Nullable
    public GoogleApiClient getGoogleApiClient() {
        return googleApiClient;
    }

    public void start() {
        startGoogleApiClient();
    }

    public void stop() {
        if (googleApiClient != null) {
            googleApiClient.disconnect();
            googleApiClient = null;
        }
    }

    private void startGoogleApiClient() {
        GoogleApiClient.Builder builder = new GoogleApiClient.Builder(context);
        configure(builder);

        LogConnectionEventListener connectionListener = new LogConnectionEventListener();
        googleApiClient = builder.build();
        googleApiClient.registerConnectionCallbacks(connectionListener);
        googleApiClient.registerConnectionFailedListener(connectionListener);
        googleApiClient.connect();
    }

    protected abstract void configure(GoogleApiClient.Builder builder);

    protected abstract void onConnected(GoogleApiClient googleApiClient);

    private class LogConnectionEventListener implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

        @Override
        public void onConnected(@Nullable Bundle bundle) {
            Log.d(TAG, "onConnected");
            GoogleApiConnection.this.onConnected(googleApiClient);
        }

        @Override
        public void onConnectionSuspended(int cause) {
            Log.d(TAG, String.format(Locale.ENGLISH, "onConnectionSuspended %d", cause));
        }

        @Override
        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
            Log.d(TAG, String.format(Locale.ENGLISH, "onConnectionFailed %s", connectionResult));
        }
    }
}