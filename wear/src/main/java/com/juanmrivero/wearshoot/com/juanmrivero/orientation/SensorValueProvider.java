package com.juanmrivero.wearshoot.com.juanmrivero.orientation;

import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

class SensorValueProvider implements SensorEventListener {

    private final SensorManager sensorManager;
    private final float[] outValues;
    private final int sensorDelay;
    private final int sensorType;
    private final SensorValueListener sensorValueListener;

    SensorValueProvider(SensorManager sensorManager,
                        int sensorType,
                        int sensorDelay,
                        float[] outValues,
                        SensorValueListener sensorValueListener) {
        this.sensorManager = sensorManager;
        this.sensorType = sensorType;
        this.sensorDelay = sensorDelay;
        this.outValues = outValues;
        this.sensorValueListener = sensorValueListener;
    }

    void start() {
        android.hardware.Sensor sensor = sensorManager.getDefaultSensor(sensorType);
        if (sensor != null) {
            sensorManager.registerListener(this, sensor, sensorDelay);
        }
    }

    void stop() {
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        System.arraycopy(event.values, 0, outValues, 0, event.values.length);
        sensorValueListener.onSensorValueUpdate();
    }

    @Override
    public void onAccuracyChanged(android.hardware.Sensor sensor, int accuracy) {
    }

    interface SensorValueListener {
        void onSensorValueUpdate();
    }
}
