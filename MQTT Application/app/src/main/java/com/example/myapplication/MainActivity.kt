package com.example.myapplication

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.mqttapp.MqttHelper
import com.example.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mqttHelper: MqttHelper

    private val mqttBroker = "tcp://broker.hivemq.com:1883"
    private val topicPin1 = "ab/ba/d1"
    private val topicPin2 = "ab/ba/d2"
    private val topicPin1Subscribe = "testnik/status/D1"
    private val topicPin2Subscribe = "testnik/status/D2"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initially disable buttons
        enableButtons(false)

        mqttHelper = MqttHelper(this, mqttBroker, "AndroidClient")

        mqttHelper.connect {
            runOnUiThread {
                binding.tvConnectionStatus.text = "Connected"
                binding.tvConnectionStatus.setTextColor(Color.GREEN)

                // Enable buttons only after connection is established
                enableButtons(true)

                // Subscribe to topics and fetch the latest pin status
                mqttHelper.subscribe(topicPin1Subscribe)
                mqttHelper.subscribe(topicPin2Subscribe)

                // Request the latest pin status after connection
                mqttHelper.publish(topicPin1, "STATUS")
                mqttHelper.publish(topicPin2, "STATUS")
            }
        }

        mqttHelper.setMessageListener { topic, message ->
            runOnUiThread {
                when (topic) {
                    topicPin1Subscribe -> updatePinStatus(
                        binding.tvPin1Status, message,
                        binding.btnPin1On, binding.btnPin1Off,
                        binding.progressPin1
                    )
                    topicPin2Subscribe -> updatePinStatus(
                        binding.tvPin2Status, message,
                        binding.btnPin2On, binding.btnPin2Off,
                        binding.progressPin2
                    )
                }
            }
        }

        binding.btnPin1On.setOnClickListener { sendCommand(topicPin1, "1", binding.progressPin1) }
        binding.btnPin1Off.setOnClickListener { sendCommand(topicPin1, "0", binding.progressPin1) }
        binding.btnPin2On.setOnClickListener { sendCommand(topicPin2, "1", binding.progressPin2) }
        binding.btnPin2Off.setOnClickListener { sendCommand(topicPin2, "0", binding.progressPin2) }
    }

    private fun sendCommand(topic: String, command: String, progressBar: ProgressBar) {
        progressBar.visibility = View.VISIBLE
        mqttHelper.publish(topic, command)
    }

    private fun updatePinStatus(statusView: TextView, message: String, btnOn: Button, btnOff: Button, progress: ProgressBar) {
        val gateName = when (statusView) {
            binding.tvPin1Status -> "Main Gate"
            binding.tvPin2Status -> "Second Gate"
            else -> "Unknown Gate"
        }

        val isOn = message.equals("on", ignoreCase = true)

        statusView.text = "$gateName: ${if (isOn) "ON" else "OFF"}"

        // Enable ON button if status is OFF, and vice versa
        btnOn.isEnabled = !isOn
        btnOff.isEnabled = isOn

        btnOn.setBackgroundColor(if (btnOn.isEnabled) Color.GREEN else Color.GRAY)
        btnOff.setBackgroundColor(if (btnOff.isEnabled) Color.RED else Color.GRAY)

        // Hide progress bar once status update is received
        progress.visibility = View.GONE
    }

    private fun enableButtons(enable: Boolean) {
        listOf(binding.btnPin1On, binding.btnPin1Off, binding.btnPin2On, binding.btnPin2Off).forEach { button ->
            button.isEnabled = enable
            button.setBackgroundColor(if (enable) Color.DKGRAY else Color.GRAY)
        }
    }
}
