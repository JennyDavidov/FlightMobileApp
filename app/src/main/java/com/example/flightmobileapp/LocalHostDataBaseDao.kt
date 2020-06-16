package com.example.flightmobileapp

import androidx.room.*
import java.nio.charset.CodingErrorAction.REPLACE


@Dao
interface LocalHostDataBaseDao {

    @Insert
    fun insert(localhost: LocalHostObject)

    @Delete
    fun delete(localhost: LocalHostObject)

    @Query("SELECT * FROM localHosts ORDER BY tblIndex DESC")
    fun readData(): List<LocalHostObject>

    @Update
    fun update(localhost: LocalHostObject)

    @Query("SELECT * FROM localHosts WHERE url = :theurl")
    fun getLocalhostByUrl(theurl: String): LocalHostObject
}