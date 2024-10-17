package com.example.basicandroidmqttclient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

public class AccessSensorsActivity extends AppCompatActivity {

    private SensorManager sensorManager;
    LightSensorAccess lightSensorAccess;
    //private Sensor mLight, mTemperature;
    private String iPAddr = "0.0.0.0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access_sensors);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        TextView textView = (TextView) findViewById(R.id.textViewLuminosity);
        TextView textViewAlert = (TextView) findViewById(R.id.textViewAlert);

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            iPAddr = extras.getString("IPAddr");
        }

        //mTemperature = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        //List<Sensor> deviceSensors = sensorManager.getSensorList(Sensor.TYPE_ALL);
        //EditText sensorInfoField = (EditText) findViewById(R.id.editTextSensorInfo);
        //sensorInfoField.setText(deviceSensors.toString());

        lightSensorAccess = new LightSensorAccess(sensorManager, textView, textViewAlert, iPAddr);
    }

    @Override
    protected void onStop(){
        super.onStop();
        lightSensorAccess.finalize();
    }
}

