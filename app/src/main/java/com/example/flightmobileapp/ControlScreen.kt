package com.example.flightmobileapp

import Api
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.gson.GsonBuilder
import com.jackandphantom.joystickview.JoyStickView
import io.github.controlwear.virtual.joystick.android.JoystickView;
import kotlinx.android.synthetic.main.activity_control_screen.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


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
        showImage()
//        url = "http://10.0.2.2:5000/screenshot";
//        Glide.with(simulator_img.context).load(url).into(simulator_img);
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
    }
    fun showImage() {
        val gson = GsonBuilder().setLenient().create()
        val retrofit = Retrofit.Builder().baseUrl("http://10.0.2.2:59669/")
            .addConverterFactory(GsonConverterFactory.create(gson)).build()
        val api = retrofit.create(Api::class.java)
        val body = api.getImg().enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>)
            {
                val I = response?.body()?.byteStream()
                val B = BitmapFactory.decodeStream(I)
                runOnUiThread {
                    simulator_img.setImageBitmap(B)
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
            }
        })
    }

}