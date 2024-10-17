package com.example.basicandroidmqttclient;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.TextView;

public class LightSensorAccess implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor mLight;
    private TextView sensor_field, alert_field;

    public LightSensorAccess(SensorManager sm, TextView tv, TextView tvAlert){
        sensorManager = sm;
        sensor_field = tv;
        alert_field = tvAlert;
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
        sensor_field.setText(String.valueOf(lux));

        if (lux < 30) {
            alert_field.setText("Ficou escuro");
        }
        else {
            alert_field.setText("Ficou claro");
        }
    }

    @Override
    protected void finalize() {
        sensorManager.unregisterListener(this);
    }
}