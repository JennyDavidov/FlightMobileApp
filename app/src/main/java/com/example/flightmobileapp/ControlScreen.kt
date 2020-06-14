package com.example.flightmobileapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_control_screen.*

class ControlScreen : AppCompatActivity() {
    private lateinit var simulatorImg: ImageView;
    private lateinit var url: String;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_control_screen)
        simulatorImg = findViewById<ImageView>(R.id.simulator_img);
        url = "https://www.google.com/search?q=flightgear&tbm=isch&ved=2ahUKEwi87YKbrIHqAhVQhqQKHc1vBD4Q2-cCegQIABAA&oq=flightgear&gs_lcp=CgNpbWcQAzIECAAQEzIECAAQEzIECAAQEzIECAAQEzIECAAQEzIECAAQEzIECAAQEzIECAAQEzIECAAQEzIECAAQEzoECCMQJzoFCAAQsQM6BAgAEEM6BwgAELEDEEM6AggAOgYIABAKEAE6BAgAEAE6BggAEAoQGDoHCCMQ6gIQJzoECAAQHlD4VljojAFg_I0BaAlwAHgAgAGbAYgBthOSAQQwLjE5mAEAoAEBqgELZ3dzLXdpei1pbWewAQo&sclient=img&ei=OB3mXrzxI9CMkgXN35HwAw&bih=610&biw=1280&rlz=1C1CHZL_enIL818IL818#imgrc=yMUdOI1mZLQ9LM";
        Picasso.get().load(url).into(simulatorImg);
    }

}
