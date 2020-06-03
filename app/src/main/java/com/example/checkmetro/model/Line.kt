package com.example.checkmetro.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "line")
data class Line(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val code: String,
    val name: String,
    val directions: String,
    val id_ratp: Int
) : Parcelable


data class LineTraffic(
    val code: String,
    val name: String,
    val slug : String
)


