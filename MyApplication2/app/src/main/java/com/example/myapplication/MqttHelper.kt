package com.example.mqttapp

import android.content.Context
import android.util.Log
import org.eclipse.paho.client.mqttv3.*
import org.eclipse.paho.android.service.MqttAndroidClient

class MqttHelper(private val context: Context, private val serverUri: String, private val clientId: String) {

    private val mqttClient: MqttAndroidClient by lazy {
        MqttAndroidClient(context.applicationContext, serverUri, clientId)
    }

    private var messageListener: ((String, String) -> Unit)? = null

    fun connect(callback: (() -> Unit)? = null) {
        val options = MqttConnectOptions().apply {
            isAutomaticReconnect = true
            isCleanSession = true
        }

        if (mqttClient.isConnected) {
            Log.d("MQTT", "Already connected")
            callback?.invoke()
            return
        }

        mqttClient.connect(options, null, object : IMqttActionListener {
            override fun onSuccess(asyncActionToken: IMqttToken?) {
                Log.d("MQTT", "Connected successfully")
                callback?.invoke()
            }

            override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                Log.e("MQTT", "Connection failed: ${exception?.message}")
            }
        })

        mqttClient.setCallback(object : MqttCallback {
            override fun connectionLost(cause: Throwable?) {
                Log.e("MQTT", "Connection lost: ${cause?.message}")
            }

            override fun messageArrived(topic: String, message: MqttMessage) {
                val payload = message.toString()
                Log.d("MQTT", "Message received from $topic: $payload")
                messageListener?.invoke(topic, payload)
            }

            override fun deliveryComplete(token: IMqttDeliveryToken?) {
                Log.d("MQTT", "Message delivered")
            }
        })
    }

    fun subscribe(topic: String) {
        mqttClient.subscribe(topic, 0, null, object : IMqttActionListener {
            override fun onSuccess(asyncActionToken: IMqttToken?) {
                Log.d("MQTT", "Subscribed to $topic")
            }

            override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                Log.e("MQTT", "Subscription failed for $topic: ${exception?.message}")
            }
        })
    }

    fun publish(topic: String, message: String) {
        if (!mqttClient.isConnected) {
            Log.e("MQTT", "MQTT Client not connected. Cannot publish.")
            return
        }

        try {
            val mqttMessage = MqttMessage(message.toByteArray())
            mqttClient.publish(topic, mqttMessage)
            Log.d("MQTT", "Message published to $topic: $message")
        } catch (e: Exception) {
            Log.e("MQTT", "Publish error: ${e.message}")
        }
    }

    fun setMessageListener(listener: (String, String) -> Unit) {
        this.messageListener = listener
    }

    fun disconnect() {
        if (mqttClient.isConnected) {
            mqttClient.disconnect(null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    Log.d("MQTT", "Disconnected successfully")
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    Log.e("MQTT", "Disconnection failed: ${exception?.message}")
                }
            })
        }
    }
}
