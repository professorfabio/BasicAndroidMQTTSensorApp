package com.example.basicandroidmqttclient;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.basicandroidmqttclient.MESSAGE";
    //public static String brokerURI = "100.27.31.71";

    Activity thisActivity;
    TextView subMsgTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        thisActivity = this;
        subMsgTextView = (TextView) findViewById(R.id.editTextMultiLineSubMsg);
    }

    /** Called when the user taps the Send button */
    public void publishMessage(View view) {
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText topicName = (EditText) findViewById(R.id.editTextTopicName);
        EditText value = (EditText) findViewById(R.id.editTextValue);
        EditText brokerIP = findViewById(R.id.editTextTextIPAddr);

        Mqtt5BlockingClient client = Mqtt5Client.builder()
                .identifier(UUID.randomUUID().toString())
                .serverHost(brokerIP.getText().toString())
                .buildBlocking();

        client.connect();
        client.publishWith()
                .topic(topicName.getText().toString())
                .qos(MqttQos.AT_LEAST_ONCE)
                .payload(value.getText().toString().getBytes())
                .send();
        client.disconnect();

        String message = topicName.getText().toString() + " " + value.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    public void sendSubscription(View view) {
        EditText topicName = (EditText) findViewById(R.id.editTextTopicNameSub);
        EditText brokerIP = (EditText) findViewById(R.id.editTextTextIPAddr);

        Mqtt5BlockingClient client = Mqtt5Client.builder()
                .identifier(UUID.randomUUID().toString())
                .serverHost(brokerIP.getText().toString())
                .buildBlocking();

        client.connect();

        // Use a callback lambda function to show the message on the screen
        client.toAsync().subscribeWith()
                .topicFilter(topicName.getText().toString())
                .qos(MqttQos.AT_LEAST_ONCE)
                .callback(msg -> {
                    thisActivity.runOnUiThread(new Runnable() {
                        public void run() {
                            subMsgTextView.setText(new String(msg.getPayloadAsBytes(), StandardCharsets.UTF_8));
                        }
                    });
                })
                .send();
    }

    public void switchToSensorsActivity(View view) {
        EditText brokerIP = (EditText) findViewById(R.id.editTextTextIPAddr);
        Intent intent = new Intent(this, AccessSensorsActivity.class);
        intent.putExtra("IPAddr", brokerIP.getText().toString());
        startActivity(intent);
    }


}