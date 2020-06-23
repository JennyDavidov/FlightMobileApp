package com.example.flightmobileapp
import com.squareup.moshi.Json

data class CommandObject(
    @Json(name = "aileron") var aileron: Double,
    @Json(name = "rudder") var rudder: Double,
    @Json(name = "elevator") var elevator: Double,
    @Json(name = "throttle") var throttle: Double
){
    constructor():
            this(0.0,0.0,0.0,0.0)
}