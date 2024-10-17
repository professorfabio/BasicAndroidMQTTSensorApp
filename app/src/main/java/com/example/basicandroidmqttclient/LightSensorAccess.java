package com.example.basicandroidmqttclient;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.TextView;

import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;

import java.util.UUID;

public class LightSensorAccess implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor mLight;
    private int state = 0; // 0-undefined; 1-light; 2-dark
    private TextView sensorField, alertField;
    private String topicName;
    private String iPAddr;
    private Mqtt5BlockingClient client;

    public LightSensorAccess(SensorManager sm, TextView tv, TextView tvAlert, String ip){
        sensorManager = sm;
        sensorField = tv;
        alertField = tvAlert;
        iPAddr = ip;
        topicName = "lux";
        client = Mqtt5Client.builder()
                .identifier(UUID.randomUUID().toString())
                .serverHost(iPAddr)
                .buildBlocking();
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
        float newLux = event.values[0];
        // Show luminosity value on the text field
        sensorField.setText(String.valueOf(newLux));
        String msg;

        if (newLux < 30 && state != 2) {
            msg = "Ficou escuro";
            alertField.setText(msg);
            client.connect();
            client.publishWith()
                    .topic(topicName)
                    .qos(MqttQos.AT_LEAST_ONCE)
                    .payload(msg.getBytes())
                    .send();
            client.disconnect();
            state = 2;
        } else if (newLux > 120 && state != 1) {
            msg = "Ficou claro";
            alertField.setText(msg);
            client.connect();
            client.publishWith()
                    .topic(topicName)
                    .qos(MqttQos.AT_LEAST_ONCE)
                    .payload(msg.getBytes())
                    .send();
            client.disconnect();
            state = 1;
        }

    }

    @Override
    protected void finalize() {
        sensorManager.unregisterListener(this);
    }
}