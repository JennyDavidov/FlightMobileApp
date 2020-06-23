package com.example.flightmobileapp

import Api
import TodoApi.retrofitService
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.gson.GsonBuilder
import com.jackandphantom.joystickview.JoyStickView
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.github.controlwear.virtual.joystick.android.JoystickView;
import kotlinx.android.synthetic.main.activity_control_screen.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import moshi
import okhttp3.ResponseBody
//import retrofit1
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import uiScope
import java.util.concurrent.Executor


class ControlScreen : AppCompatActivity() {
    var minRudder = -1
    var minThrottle = 0
    var maxRudder = 1
    var maxThrottle = 1
    var stepRudder = 0.1
    var stepThrottle = 0.1
    val command = CommandObject(0.0, 0.0, 0.0, 0.0)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_control_screen)
        TodoApi.setUrl(intent.getStringExtra("givenPort").toString())
        showImage()
//        val mainHandler = Handler(Looper.getMainLooper())
//        mainHandler.post(object : Runnable {
//            override fun run() {
//
//                showImage()
//                mainHandler.postDelayed(this, 7000)
//            }
//        })

        //showImage()
//        url = "http://10.0.2.2:5000/screenshot";
//        Glide.with(simulator_img.context).load(url).into(simulator_img);
      
        seekBar1.max = ((maxRudder - minRudder) / stepRudder).toInt();
        seekBar2.max = ((maxThrottle - minThrottle) / stepThrottle).toInt();
        seekBar1.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                val value: Double = (minRudder + (i * stepRudder)).toDouble()
                command.rudder = value
                val rudderString = String.format("%.2f", value)
                // Display the current progress of SeekBar
                textView1.text = "Rudder: " + rudderString
                postfunction()
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }
            override fun onStopTrackingTouch(seekBar: SeekBar) {
            }
        })
        seekBar2.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                val value: Double = (minThrottle + (i * stepThrottle)).toDouble()
                command.throttle = value
                // Display the current progress of SeekBar
                val throttleString = String.format("%.2f", value)
                textView2.text = "Throttle: " + throttleString
                postfunction()
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }
            override fun onStopTrackingTouch(seekBar: SeekBar) {
            }
        })
        val joystick: JoystickView = findViewById(R.id.joyStickView) as JoystickView
        var aileron: Double
        var elevator: Double
        var prevAileron: Double
        var prevElevator: Double
        var newY: Int
        var aileronString: String
        var elevatorString: String
        joystick.setOnMoveListener(object : JoystickView.OnMoveListener {
            override fun onMove(angle: Int, strength: Int) {
                prevAileron = command.aileron
                prevElevator = command.elevator
                newY = 100 - joystick.normalizedX
                aileron = newY.toDouble()
                aileron = (((aileron/100)*2)-1)
                command.aileron = aileron
                elevator = joystick.normalizedY.toDouble()
                elevator = (((elevator/100)*2)-1)
                command.elevator = elevator
                aileronString = String.format("%.2f", aileron)
                elevatorString = String.format("%.2f", elevator)
                textView3.text = "Aileron: " + aileronString
                textView4.text = "Elevator: " + elevatorString
                if (elevator - prevElevator >= 0.02) {
                    postfunction()
                } else if (aileron - prevAileron >= 0.02) {
                    postfunction()
                }
            }
        })
    }

    fun showImage() {
        val port = intent.getStringExtra("givenPort")
        val url = "http://10.0.2.2:".plus(port).plus("/")
        val gson = GsonBuilder().setLenient().create()
        val retrofit = Retrofit.Builder().baseUrl(url)
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
    fun postfunction() {
        uiScope.launch {
            var deferredResults = TodoApi.retrofitService.setJoystickValues(command)
            println(command.aileron.toString() + ", " + command.rudder.toString() + ", "
            + command.elevator.toString() + ", " + command.throttle.toString())
            try {
                var response: ResponseBody= deferredResults.await()
                println("response:" + response)
                Toast.makeText(this@ControlScreen, "Set successful",
                    Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                println("Exception: " + e.message)
                Toast.makeText(this@ControlScreen, "Error in server",
                    Toast.LENGTH_LONG).show()
            }
        }
    }
}