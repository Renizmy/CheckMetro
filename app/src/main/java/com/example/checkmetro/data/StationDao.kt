package com.example.checkmetro.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.checkmetro.model.Line
import com.example.checkmetro.model.Station

@Dao
interface StationDao {

    @Query("SELECT * from station")
    suspend fun getStations():List<Station>

    @Insert
    suspend fun addStation(station: Station)

    @Delete
    suspend fun deleteStation(station: Station)

    @Query("DELETE FROM station")
    suspend fun deleteStations()

    @Query("SELECT * from station where id=:idStation")
    suspend fun getStation(idStation: Int): Station

    @Query("SELECT * from station where slug=:slugStation")
    suspend fun getStationWithSlug(slugStation: String): Station


    @Query("UPDATE station Set favorite=:fav WHERE slug=:slugStation ")
    suspend fun UpdateStationFavorite(fav:Boolean,slugStation: String)


}