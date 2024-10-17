package com.example.basicandroidmqttclient;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.TextView;

public class LightSensorAccess implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor mLight;
    private TextView sensorField, alertField;

    public LightSensorAccess(SensorManager sm, TextView tv, TextView tvAlert){
        sensorManager = sm;
        sensorField = tv;
        alertField = tvAlert;
        mLight = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        sensorManager.registerListener(this, mLight, SensorManager.SENSOR_DELAY_NORMAL);
    }
    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        // The light sensor returns a single value.
        float lux = event.values[0];
        // Show luminosity value on the text field
        sensorField.setText(String.valueOf(lux));

        if (lux < 30) {
            alertField.setText("Ficou escuro");
        }
        else {
            alertField.setText("Ficou claro");
        }
    }

    @Override
    protected void finalize() {
        sensorManager.unregisterListener(this);
    }
}