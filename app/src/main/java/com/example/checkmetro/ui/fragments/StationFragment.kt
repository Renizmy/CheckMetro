package com.example.checkmetro.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.checkmetro.R
import com.example.checkmetro.adapter.StationFragmentAdapter
import com.example.checkmetro.data.LineDao
import com.example.checkmetro.data.LinkLineStationDao
import com.example.checkmetro.data.StationDao
import com.example.checkmetro.model.StationsLine
import com.example.checkmetro.service.APIRATP
import com.example.checkmetro.utils.dao
import com.example.checkmetro.utils.retrofit
import kotlinx.android.synthetic.main.fragment_station.view.*
import kotlinx.coroutines.runBlocking


class StationFragment : Fragment() {

    private lateinit var linkLineStationDao: LinkLineStationDao
    private lateinit var stationDao: StationDao
    private lateinit var lineDao: LineDao
    private lateinit var line: String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        stationDao = dao().getStationDao()
        linkLineStationDao = dao().getLinkLineStationDao()
        lineDao=dao().getLineDao()

        val service = retrofit("https://api-ratp.pierre-grimaud.fr").create(APIRATP::class.java)

        val root = inflater.inflate(R.layout.fragment_station, container, false)
        val recyclerView: RecyclerView = root.findViewById(R.id.stations_recyclerview)

        val stationsLine: MutableList<StationsLine> = arrayListOf()
        var linksLineIc: MutableList<String> = arrayListOf()

        recyclerView.layoutManager =
            LinearLayoutManager(this.requireContext(), LinearLayoutManager.VERTICAL, false)

        arguments?.let { it ->

            val safeArgs = StationFragmentArgs.fromBundle(it)
            line = safeArgs.lineCode

            if (line != "Orv" && line != "Fun") {
                runBlocking {
                    val traffic = service.getLineTraffic(line)
                    Log.d("result", traffic.result.message)
                    root.tv_traffic.text = traffic.result.message
                    Log.d("result", traffic.result.slug)

                    when (traffic.result.slug) {
                        "critical" -> {
                            Glide.with(requireContext())
                                .load(R.drawable.ic_warning_red).centerCrop()
                                .into(root.iv_traffic_station)
                        }
                        "normal_trav" -> {
                            Glide.with(requireContext())
                                .load(R.drawable.ic_warning_orange).centerCrop()
                                .into(root.iv_traffic_station)
                        }
                        "normal" -> {
                            root.iv_traffic_station.setImageDrawable(null)
                        }
                        else -> {
                            root.iv_traffic_station.setImageDrawable(null)
                        }
                    }

                }
            } else {
                root.tv_traffic.text = "Aucune donnée disponible concernant le traffic sur cette ligne"
                root.iv_traffic_station.setImageDrawable(null)
            }


            runBlocking {
                val links = linkLineStationDao.getAllStationsFromLine(line)
                val currentLine=lineDao.getLineWithCode(line)

                val uri="ic_m"+line.toLowerCase()+"genrvb"
                val id=requireContext().resources.getIdentifier(uri,"drawable",requireContext().packageName)
                Glide.with(requireContext()).load(id).centerCrop().placeholder(R.drawable.ic_error_outline).into(root.iv_ic_line)
                root.tv_dest.text=currentLine.directions

                (activity as AppCompatActivity).supportActionBar!!.title = currentLine.name
0
                links.map { link ->
                    val linksline=linkLineStationDao.getAllLinesFromStation(link.slugStation)
                    linksLineIc= arrayListOf()
                    linksline.map{it->
                        if(it.codeLine!=line){
                            linksLineIc.add(it.codeLine)
                        }
                    }
                    val station = stationDao.getStationWithSlug(link.slugStation)
                    stationsLine.add(StationsLine(station.name, station.slug, line,linksLineIc.toList()))
                }
                recyclerView.adapter = StationFragmentAdapter(stationsLine.toList())
            }
        }
        return root

    }

}






