package com.example.checkmetro.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
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

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.line_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.action_map -> {
                  val target=FavoritesFragmentDirections.actionNavigationFavoritesToMapFragment()
                     Navigation.findNavController(requireView()).navigate(target)
            }
        }
        return super.onOptionsItemSelected(item)
    }

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
