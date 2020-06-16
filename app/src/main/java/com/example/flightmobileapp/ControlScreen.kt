package com.example.flightmobileapp

import android.os.Bundle
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.jackandphantom.joystickview.JoyStickView
import io.github.controlwear.virtual.joystick.android.JoystickView;
import kotlinx.android.synthetic.main.activity_control_screen.*


class ControlScreen : AppCompatActivity() {
    var minRudder = -1
    var minThrottle = 0
    var maxRudder = 1
    var maxThrottle = 1
    var stepRudder = 0.1
    var stepThrottle = 0.1
    private lateinit var url: String;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_control_screen)
        url = "http://10.0.2.2:5000/screenshot";
        Glide.with(simulator_img.context).load(url).into(simulator_img);
        seekBar1.max = ((maxRudder - minRudder) / stepRudder).toInt();
        seekBar2.max = ((maxThrottle - minThrottle) / stepThrottle).toInt();
        seekBar1.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                val value: Float = (minRudder + (i * stepRudder)).toFloat()
                // Display the current progress of SeekBar
                textView1.text = "Rudder: $value"
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }
            override fun onStopTrackingTouch(seekBar: SeekBar) {
            }
        })
        seekBar2.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                val value: Float = (minThrottle + (i * stepThrottle)).toFloat()
                // Display the current progress of SeekBar
                textView2.text = "Throttle: $value"
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }
            override fun onStopTrackingTouch(seekBar: SeekBar) {
            }
        })
        val joystick: JoystickView = findViewById(R.id.joyStickView) as JoystickView
        joystick.setOnMoveListener(object : JoystickView.OnMoveListener {
            override fun onMove(angle: Int, strength: Int) {
            }
        })
//        val joystick: JoyStickView = findViewById(R.id.joyStickView) as JoyStickView
//        joystick.setOnMoveListener(object : JoyStickView.OnMoveListener {
//            override fun onMove(angle: Double, strength: Float) {
//            }
//        })
    }

}