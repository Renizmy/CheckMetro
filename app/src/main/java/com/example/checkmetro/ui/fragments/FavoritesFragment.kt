package com.example.checkmetro.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.checkmetro.R
import com.example.checkmetro.adapter.FavoritesFragmentAdapter
import com.example.checkmetro.data.LinkLineStationDao
import com.example.checkmetro.data.StationFavoritesDao
import com.example.checkmetro.model.StationsLine
import com.example.checkmetro.utils.dao
import kotlinx.coroutines.runBlocking

class FavoritesFragment : Fragment() {

    private  lateinit var stationFavoritesDao: StationFavoritesDao
    private lateinit var linkLineStationDao: LinkLineStationDao
    private var linksLineIc: MutableList<String> = arrayListOf()


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_favorites, container, false)
        val recyclerView:RecyclerView=root.findViewById(R.id.favorites_recyclerview)
        recyclerView.layoutManager= LinearLayoutManager(this.requireContext(), LinearLayoutManager.VERTICAL,false)
        stationFavoritesDao=dao().getFavoritesStationDao()
        linkLineStationDao=dao().getLinkLineStationDao()

        var linksLineIc: MutableList<String> = arrayListOf()
        runBlocking {
           val favs=stationFavoritesDao.getStationFavorites()
            val stations:MutableList<StationsLine> = arrayListOf()

            favs.map{
                val linksline=linkLineStationDao.getAllLinesFromStation(it.slug)
                linksLineIc= arrayListOf()
                linksline.map { it ->
                    linksLineIc.add(it.codeLine)
                }
                val station:StationsLine=StationsLine(it.name,it.slug,it.lineCode,linksLineIc)
                 stations.add(station)
            }
               recyclerView.adapter=FavoritesFragmentAdapter(stations.toList())
        }


        return root
    }
}
