package com.example.flightmobileapp

import Api
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View.OnFocusChangeListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.URL


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val dbHandler = AppDatabase(this)
        var initialData = dbHandler.localHostDataBaseDao().readData()
        if ((initialData.size) >= 5) {
            local1.setText(initialData.get(0).url);
            local2.setText(initialData.get(1).url);
            local3.setText(initialData.get(2).url);
            local4.setText(initialData.get(3).url);
            local5.setText(initialData.get(4).url);
        } else if ((initialData.size) == 4) {
            local1.setText(initialData.get(0).url);
            local2.setText(initialData.get(1).url);
            local3.setText(initialData.get(2).url);
            local4.setText(initialData.get(3).url);
            local5.setText("no url");
        } else if ((initialData.size) == 3) {
            local1.setText(initialData.get(0).url);
            local2.setText(initialData.get(1).url);
            local3.setText(initialData.get(2).url);
            local4.setText("no url");
            local5.setText("no url");

        } else if ((initialData.size) == 2) {
            local1.setText(initialData.get(0).url);
            local2.setText(initialData.get(1).url);
            local3.setText("no url");
            local4.setText("no url");
            local5.setText("no url");
        } else if ((initialData.size) == 1) {
            local1.setText(initialData.get(0).url);
            local2.setText("no url");
            local3.setText("no url");
            local4.setText("no url");
            local5.setText("no url");
        }
        else{
            local1.text = "no url"
            local2.text = "no url"
            local3.text = "no url"
            local4.text = "no url"
            local5.text ="no url"
        }
        editTextTextPersonName10.setText("Type URL")

        editTextTextPersonName10.setOnFocusChangeListener(OnFocusChangeListener { v, hasFocus -> editTextTextPersonName10.setText("") })

        //clicking on existing url will appear in the type url field
        local1.setOnClickListener {
            if (!local1.text.toString().isEmpty()) {
                editTextTextPersonName10.setText(local1.text)
            }
        }
        local2.setOnClickListener {
            if (!local2.text.toString().isEmpty()) {
                editTextTextPersonName10.setText(local2.text)
            }
        }
        local3.setOnClickListener {
            if (!local3.text.toString().isEmpty()) {
                editTextTextPersonName10.setText(local3.text)
            }
        }
        local4.setOnClickListener {
            if (!local4.text.toString().isEmpty()) {
                editTextTextPersonName10.setText(local4.text)
            }
        }
        local5.setOnClickListener {
            if (!local5.text.toString().isEmpty()) {
                editTextTextPersonName10.setText(local5.text)
            }
        }

        button2.setOnClickListener {
            if (!editTextTextPersonName10.text.toString().isEmpty()) {
                //check if this URL is not already exist in DB
                var local = dbHandler?.localHostDataBaseDao().getLocalhostByUrl(this.editTextTextPersonName10.text.toString())
                if(local!=null){
                    dbHandler?.localHostDataBaseDao().delete(local)
                    updateListOfLocals(dbHandler)
                }
                dbHandler?.localHostDataBaseDao().insert(LocalHostObject(this.editTextTextPersonName10.text.toString()))
                val context = this.baseContext;
                val givenUrl = editTextTextPersonName10.text.toString();
                val port = findPort(editTextTextPersonName10.text.toString())
                editTextTextPersonName10.text.clear()

                //checking connection with server(given URL)
                val url = "http://10.0.2.2:".plus(port).plus("/")
                val gson = GsonBuilder().setLenient().create()
                val retrofit = Retrofit.Builder().baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create(gson)).build()
                val api = retrofit.create(Api::class.java)
                val body = api.getImg().enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>)
                    {
                        //check if the response is success
                        if(response.isSuccessful) {
                            openActivity(context, givenUrl)
                        }
                        else{
                            Toast.makeText(context, "Failed to load image",
                                Toast.LENGTH_LONG).show()
                        }
                    }
                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

                        Toast.makeText(context, givenUrl + " Invalid URL, try again",
                        Toast.LENGTH_LONG).show()
                    }
            })
            }
            updateListOfLocals(dbHandler)
        }
    }
    fun openActivity(context: Context, url: String) {
        var port: String = findPort(url)
        val intent = Intent(context, ControlScreen::class.java).apply {
            putExtra("givenPort", port);
        }
        startActivity(intent)
    }

    fun findPort(givenUrl: String): String {
        //if the url contains http, we can extract the port
        //using the URL object
        if(givenUrl.contains("http"))
        {
            var url = URL(givenUrl)
            val _port: Int = url.getPort()
            Toast.makeText(this, _port.toString() + " found port",
                    Toast.LENGTH_LONG
                ).show()
            return  _port.toString()
        }
        else{
            //concut the http to given url
            val updatedUrl = "http://".plus(givenUrl)
            var url = URL(updatedUrl)
            val _port: Int = url.getPort()
            Toast.makeText(this, _port.toString() + " found port",
                Toast.LENGTH_LONG
            ).show()
            return  _port.toString()
        }
    }


    private fun updateListOfLocals(dbHandler: AppDatabase) {
        var data = dbHandler?.localHostDataBaseDao().readData()
        if ((data?.size) >= 5) {
            local1.setText(data.get(0).url);
            local2.setText(data.get(1).url);
            local3.setText(data.get(2).url);
            local4.setText(data.get(3).url);
            local5.setText(data.get(4).url);
        } else if ((data.size) == 4) {
            local1.setText(data.get(0).url);
            local2.setText(data.get(1).url);
            local3.setText(data.get(2).url);
            local4.setText(data.get(3).url);
        } else if ((data.size) == 3) {
            local1.setText(data.get(0).url);
            local2.setText(data.get(1).url);
            local3.setText(data.get(2).url);
        } else if ((data.size) == 2) {
            local1.setText(data.get(0).url);
            local2.setText(data.get(1).url);
        } else if ((data.size) == 1) {
            local1.setText(data.get(0).url);
        }
    }
}