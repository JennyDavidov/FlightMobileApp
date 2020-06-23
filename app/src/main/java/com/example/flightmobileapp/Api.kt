import com.example.flightmobileapp.CommandObject
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Json
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
var BASE_URL = "http://10.0.2.2:"
object TodoApi {
    val retrofitService: Api by lazy {
        val retrofit1 = Retrofit.Builder().addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .baseUrl(BASE_URL).build()
        retrofit1.create(Api ::class.java)
    }
    fun setUrl(port: String) {
        BASE_URL = BASE_URL.plus(port).plus("/")
    }
}
val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

var viewModelJob = Job()
val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
interface Api {
    @GET("/screenshot")
    fun getImg(): Call<ResponseBody>
    @POST("/api/command")
    fun setJoystickValues(@Body updateCommand: CommandObject): Deferred<ResponseBody>
}