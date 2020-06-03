package com.example.checkmetro.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.checkmetro.model.StationFavorites

@Dao
interface StationFavoritesDao {

    @Query("SELECT * from stationfavorites")
    suspend fun getStationFavorites():List<StationFavorites>

    @Query("DELETE from stationfavorites")
    suspend fun deleteStationFavorites()

    @Query("SELECT * from stationfavorites where slug=:slugStation")
    suspend fun getStationFavoritesWithSlug(slugStation:String):StationFavorites

    @Query("DELETE from stationfavorites where slug=:slugStation")
    suspend fun deleteStationFavoritesWithSlug(slugStation:String)

    @Insert
    suspend fun addStationFavorite(stationFavorites: StationFavorites)
}
