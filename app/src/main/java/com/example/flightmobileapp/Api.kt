import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Json
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
data class CommandObject(
    @Json(name = "aileron") var aileron: Double,
    @Json(name = "rudder") var rudder: Double,
    @Json(name = "elevator") var elevator: Double,
    @Json(name = "throttle") var throttle: Double
)
val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
val retrofit = Retrofit.Builder().addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl("http://10.0.2.2:59669/").build()
object TodoApi {
    val retrofitService: Api by lazy {
        retrofit.create(Api ::class.java)
    }
}
var viewModelJob = Job()
val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
interface Api {
    @GET("/screenshot")
    fun getImg(): Call<ResponseBody>
    @POST()
    fun setJoystickValues(@Body updateCommand: CommandObject): Deferred<ResponseBody>
}