package com.example.checkmetro

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.room.Room
import com.example.checkmetro.data.AppDatabase
import com.example.checkmetro.model.Line
import com.example.checkmetro.model.LinkLineStation
import com.example.checkmetro.model.Station
import com.example.checkmetro.service.APIRATP
import com.example.checkmetro.utils.dao
import com.example.checkmetro.utils.retrofit
import kotlinx.coroutines.runBlocking

class CheckOutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_out)

        val pref = applicationContext.getSharedPreferences(
            "CheckMetroApp",
            Context.MODE_PRIVATE
        )

        val reset = pref.getBoolean("reset", true);

        Log.d("launcher",reset.toString())

        if (reset) {

            val linkLineDao =dao().getLinkLineStationDao()
            val lineDao = dao().getLineDao()
            val stationDao =dao().getStationDao()
            val service = retrofit("https://api-ratp.pierre-grimaud.fr").create(APIRATP::class.java)

            runBlocking {

                lineDao.deleteLines()
                stationDao.deleteStations()
                linkLineDao.deleteLinklinestation()

                val resultLine = service.getLines()
                resultLine.result.metros.map { it ->
                    val line = Line(0, it.code, it.name, it.directions, it.id)
                    lineDao.addLine(line)

                    val resultStations = service.getStations(it.code)
                    resultStations.result.stations.map { it2 ->

                        val station = Station(0, it2.name, it2.slug, false)
                        stationDao.addStation(station)

                        val link = LinkLineStation(0, it.code, it2.slug)
                        linkLineDao.addLinkLineStation(link)

                    }

                }

            }
            val editor: SharedPreferences.Editor = pref.edit()
            editor.putBoolean("reset",false)
            editor.apply()
        }
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)

    }

    override fun onResume() {
        super.onResume()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

}
