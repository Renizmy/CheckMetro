package com.example.checkmetro.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.checkmetro.model.Line
import com.example.checkmetro.model.LinkLineStation
import com.example.checkmetro.model.Station
import com.example.checkmetro.model.StationFavorites

@Database(entities = [Line::class, Station::class, LinkLineStation::class,StationFavorites::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getLineDao(): LineDao
    abstract fun getStationDao(): StationDao
    abstract fun getLinkLineStationDao():LinkLineStationDao
    abstract fun getFavoritesStationDao():StationFavoritesDao
}
