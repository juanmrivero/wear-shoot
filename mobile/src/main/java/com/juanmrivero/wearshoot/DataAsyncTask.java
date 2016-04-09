package com.juanmrivero.wearshoot;

import android.os.AsyncTask;

import com.juanmrivero.utils.logging.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

public class DataAsyncTask extends AsyncTask<InputStream, Integer, Void> {

    private static final String TAG = DataAsyncTask.class.getName();

    @Override
    protected Void doInBackground(InputStream... params) {
        Log.d(TAG, "doInBackground " + params[0]);
        try (InputStream inputStream = params[0]) {

            int value;
            do {
                Log.d(TAG, "reading stream " + inputStream);
                value = inputStream.read();
                Log.d(TAG, String.format(Locale.ENGLISH, "value read %d", value));

                if (value == -1) {
                    break;
                } else {
                    publishProgress(value);
                }

            } while (!isCancelled());

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}
