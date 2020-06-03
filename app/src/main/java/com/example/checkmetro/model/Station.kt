package com.example.checkmetro.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "station")
data class Station(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    val slug: String,
    val favorite:Boolean
) : Parcelable



data class StationsLine(
    val name: String,
    val slug: String,
    val line:String,
    val related_lines:List<String>
)
