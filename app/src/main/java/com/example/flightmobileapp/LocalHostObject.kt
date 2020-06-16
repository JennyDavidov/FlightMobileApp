package com.example.flightmobileapp

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "localHosts")
data class LocalHostObject (
    @PrimaryKey(autoGenerate = true)
    var tblIndex : Int = 0,
    @ColumnInfo(name = "url")
    var url : String = ""

){
    constructor(string: String) :
            this(0, url = string)
}