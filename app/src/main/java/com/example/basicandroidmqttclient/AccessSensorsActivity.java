package com.example.basicandroidmqttclient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.EditText;

import java.util.List;

public class AccessSensorsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SensorManager sensorManager;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access_sensors);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> deviceSensors = sensorManager.getSensorList(Sensor.TYPE_ALL);
        EditText sensorInfoField = (EditText) findViewById(R.id.editTextSensorInfo);
        sensorInfoField.setText(deviceSensors.toString());

    }
}