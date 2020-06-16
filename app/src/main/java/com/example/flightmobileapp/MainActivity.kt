package com.example.flightmobileapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.OnFocusChangeListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


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
                Toast.makeText(this, editTextTextPersonName10.text.toString() + " Added to database",
                    Toast.LENGTH_LONG
                ).show()
                editTextTextPersonName10.text.clear()
            }
            updateListOfLocals(dbHandler)
        }
    }
    fun openActivity(view: View) {
        val intent = Intent(this, ControlScreen::class.java)
        startActivity(intent)
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