package com.example.checkmetro.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.checkmetro.model.Line

@Dao
interface LineDao {

    @Query("select * from line")
    suspend fun getLines(): List<Line>

    @Insert
    suspend fun addLine(line: Line)


    @Delete
    suspend fun deleteLine(line: Line)

    @Query("DELETE FROM line")
    suspend fun deleteLines()

    @Query("SELECT * from line where id=:idLine")
    suspend fun getLine(idLine: Int): Line

    @Query("SELECT * from line where code=:codeLine")
    suspend fun getLineWithCode(codeLine: String): Line

    @Query("SELECT * from line where code=:code")
    suspend fun getLineFromCodeRATP(code: String): Line

}