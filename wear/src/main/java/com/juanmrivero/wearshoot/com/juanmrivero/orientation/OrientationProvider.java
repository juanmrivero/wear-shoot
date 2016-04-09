package com.juanmrivero.wearshoot.com.juanmrivero.orientation;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


public class OrientationProvider {

    @NonNull private final float[] accelerometerValues = new float[3];
    @NonNull private final float[] magneticValues = new float[3];
    @NonNull private final float[] rotationMatrix = new float[9];
    @NonNull private final float[] orientationValues = new float[3];

    private final SensorValueProvider accelerometer;
    private final SensorValueProvider magnetometer;
    private final SensorValueProvider.SensorValueListener sensorValueListener;

    @Nullable private OrientationListener orientationListener;

    public OrientationProvider(@NonNull SensorManager sensorManager,
                               int sensorDelay) {
        sensorValueListener = new SensorValue();
        accelerometer = new SensorValueProvider(sensorManager, Sensor.TYPE_ACCELEROMETER, sensorDelay, accelerometerValues, sensorValueListener);
        magnetometer = new SensorValueProvider(sensorManager, Sensor.TYPE_MAGNETIC_FIELD, sensorDelay, magneticValues, sensorValueListener);
    }

    public void start(OrientationListener orientationListener) {
        this.orientationListener = orientationListener;
        accelerometer.start();
        magnetometer.start();
    }

    public void stop() {
        this.orientationListener = null;
        accelerometer.stop();
        magnetometer.stop();
    }

    private void notifyOrientation() {
        SensorManager.getRotationMatrix(rotationMatrix, null, accelerometerValues, magneticValues);
        SensorManager.getOrientation(rotationMatrix, orientationValues);

        if (orientationListener != null) {
            orientationListener.onOrientation(new Orientation(
                    orientationValues[0],
                    orientationValues[1],
                    orientationValues[2]
            ));
        }

    }

    private class SensorValue implements SensorValueProvider.SensorValueListener {
        @Override
        public void onSensorValueUpdate() {
            notifyOrientation();
        }
    }

    public interface OrientationListener {
        void onOrientation(Orientation orientation);
    }

}
