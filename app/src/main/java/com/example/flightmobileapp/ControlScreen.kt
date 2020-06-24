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
import com.google.gson.GsonBuilder
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
import kotlin.math.abs


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
        val mainHandler = Handler(Looper.getMainLooper())
        mainHandler.post(object : Runnable {
            override fun run() {
                showImage()
                mainHandler.postDelayed(this, 3000)
            }
        })
        seekBar1.max = ((maxRudder - minRudder) / stepRudder).toInt();
        seekBar2.max = ((maxThrottle - minThrottle) / stepThrottle).toInt();
        seekBar1.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                val value: Double = (minRudder + (i * stepRudder)).toDouble()
                val rudderString = String.format("%.1f", value)
                command.rudder = rudderString.toDouble()
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
                val throttleString = String.format("%.1f", value)
                command.throttle = throttleString.toDouble()
                // Display the current progress of SeekBar
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
                aileron = (((aileron/100)*2)-1)*(-1)
                if (aileron == -0.0) {
                    aileron = 0.0
                }
                elevator = joystick.normalizedY.toDouble()
                elevator = (((elevator/100)*2)-1)*(-1)
                if (elevator == -0.0) {
                    elevator = 0.0
                }
                aileronString = String.format("%.1f", aileron)
                command.aileron = aileronString.toDouble()
                elevatorString = String.format("%.1f", elevator)
                command.elevator = elevatorString.toDouble()
                textView3.text = "Aileron: " + aileronString
                textView4.text = "Elevator: " + elevatorString
                if (abs(elevator - prevElevator) >= 0.02) {
                    postfunction()
                } else if (abs(aileron - prevAileron) >= 0.02) {
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
                Toast.makeText(this@ControlScreen, "Connection error - Image broken",
                    Toast.LENGTH_LONG).show()
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
            } catch (e: Exception) {
                println("Exception: " + e.message)
                Toast.makeText(this@ControlScreen, "Error in server, try to connect again",
                    Toast.LENGTH_LONG).show()
            }
        }
    }
}