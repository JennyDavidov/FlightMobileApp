package com.example.flightmobileapp

import DataBaseHandler
import Localhost
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editTextTextPersonName10.setOnClickListener {
            val dbHandler = DataBaseHandler(this, null)
            val localHost = Localhost(editTextTextPersonName10.text.toString())
            dbHandler.insertData(localHost)
            Toast.makeText(this, editTextTextPersonName10.text.toString() + "Added to database", Toast.LENGTH_LONG).show()
        }
        local1.setOnClickListener {
            val dbHandler = DataBaseHandler(this, null)
            val cursor = dbHandler.getAllName()
            cursor!!.moveToFirst()
            local1.append((cursor.getString(cursor.getColumnIndex("url"))))
            while (cursor.moveToNext()) {
                local1.append((cursor.getString(cursor.getColumnIndex("url"))))
                local1.append("\n")
            }
            cursor.close()
            //test
        }





    }
}