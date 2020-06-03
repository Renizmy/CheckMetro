package com.example.checkmetro.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.checkmetro.model.LinkLineStation

@Dao
interface LinkLineStationDao {

    @Insert
    suspend fun addLinkLineStation(linkLineStation: LinkLineStation)

    @Query("SELECT * from linklinestation where slugStation=:slugStation")
    suspend fun getAllLinesFromStation(slugStation:String):List<LinkLineStation>

    @Query("SELECT * from linklinestation where codeLine=:codeLine")
    suspend fun getAllStationsFromLine(codeLine:String):List<LinkLineStation>

    @Query("DELETE FROM linklinestation")
    suspend fun deleteLinklinestation()

  }
