package com.example.checkmetro.ui.fragments

import android.os.Bundle
import android.view.*
import android.widget.Switch
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.checkmetro.R
import com.example.checkmetro.adapter.StationDetailsFragmentAdapter
import com.example.checkmetro.data.LinkLineStationDao
import com.example.checkmetro.data.StationDao
import com.example.checkmetro.data.StationFavoritesDao
import com.example.checkmetro.model.Schedule
import com.example.checkmetro.model.Station
import com.example.checkmetro.model.StationFavorites
import com.example.checkmetro.service.APIRATP
import com.example.checkmetro.utils.dao
import com.example.checkmetro.utils.retrofit
import kotlinx.android.synthetic.main.fragment_station_details.view.*
import kotlinx.coroutines.runBlocking

class StationDetailsFragment : Fragment() {
    private lateinit var stationDao: StationDao
    private lateinit var stationFavoritesDao: StationFavoritesDao
    private lateinit var linkLineStationDao: LinkLineStationDao
    private lateinit var station: Station
    private lateinit var stationSlug: String
    private var stationFavorites: StationFavorites? = null
    private lateinit var lineCode: String
    private val service = retrofit("https://api-ratp.pierre-grimaud.fr").create(APIRATP::class.java)
    private lateinit var recyclerView: RecyclerView
    private lateinit var schedule: MutableList<Schedule>

    override fun onCreate(savedInstanceState: Bundle?) {

        stationDao = dao().getStationDao()
        linkLineStationDao = dao().getLinkLineStationDao()
        stationFavoritesDao = dao().getFavoritesStationDao()

        arguments?.let {
            val safeArgs = StationDetailsFragmentArgs.fromBundle(it)
            stationSlug = safeArgs.stationSlug
            lineCode = safeArgs.lineCode
            runBlocking {
                station = stationDao.getStationWithSlug(stationSlug)
                stationFavorites = stationFavoritesDao.getStationFavoritesWithSlug(stationSlug)
            }
        }
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)

        if (stationFavorites == null) {
            menu.findItem(R.id.action_favorites).icon = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.outline_star_border_black_18dp
            )
        } else {
            menu.findItem(R.id.action_favorites).icon = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.outline_star_black_18dp
            )
        }

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.action_favorites -> {
                if (stationFavorites != null) {
                    item.icon = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.outline_star_border_black_18dp
                    )
                    runBlocking { stationFavoritesDao.deleteStationFavoritesWithSlug(station.slug) }
                    stationFavorites = null

                    Toast.makeText(
                        requireContext(),
                        "Station : " + station.name + " supprimée des favoris",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    runBlocking {
                        stationFavorites = StationFavorites(0, station.name, station.slug, lineCode)
                        stationFavoritesDao.addStationFavorite(stationFavorites!!)
                    }
                    item.icon = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.outline_star_black_18dp
                    )
                    Toast.makeText(
                        requireContext(),
                        "Station : " + station.name + " ajoutée aux favoris",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            R.id.action_refresh -> {

                val switch: Switch = requireView().findViewById(R.id.switch_horraire)

                if(switch.isChecked) {

                    getAllScheduleOfStation()
                    recyclerView.adapter?.notifyDataSetChanged()
                    recyclerView.adapter = StationDetailsFragmentAdapter(schedule)

                }else{
                    getScheduleOfStation()
                    recyclerView.adapter?.notifyDataSetChanged()
                    recyclerView.adapter = StationDetailsFragmentAdapter(schedule)
                }
            }
        }


        return super.onOptionsItemSelected(item)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_station_details, container, false)
        recyclerView = root.findViewById(R.id.stations_details_recyclerview)
        recyclerView.layoutManager =
            LinearLayoutManager(this.requireContext(), LinearLayoutManager.VERTICAL, false)

        val switch: Switch = root.findViewById(R.id.switch_horraire)
        switch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Toast.makeText(this.requireContext(), "show", Toast.LENGTH_SHORT).show()
                getAllScheduleOfStation()
                recyclerView.adapter?.notifyDataSetChanged()
                recyclerView.adapter = StationDetailsFragmentAdapter(schedule)


            } else {
                getScheduleOfStation()
                Toast.makeText(this.requireContext(), "hide", Toast.LENGTH_SHORT).show()
                recyclerView.adapter?.notifyDataSetChanged()
                recyclerView.adapter = StationDetailsFragmentAdapter(schedule)

            }

        }

        getScheduleOfStation()
        recyclerView.adapter = StationDetailsFragmentAdapter(schedule)

        root.refresh_layout.setOnRefreshListener{
            if(root.switch_horraire.isChecked){
                getAllScheduleOfStation()
                recyclerView.adapter = StationDetailsFragmentAdapter(schedule)
                recyclerView.adapter?.notifyDataSetChanged()

                root.refresh_layout.isRefreshing=false
                 }else{
                getScheduleOfStation()
                recyclerView.adapter = StationDetailsFragmentAdapter(schedule)
                recyclerView.adapter?.notifyDataSetChanged()

                root.refresh_layout.isRefreshing=false
            }

        }
        return root
    }


    private fun getScheduleOfStation() {
        schedule = arrayListOf()
        runBlocking {
            val mess: MutableList<String> = arrayListOf()
            val result = service.getSchedulesTrains(lineCode, station.slug)
            val destinations = result.result.schedules.distinctBy { it.destination }

            destinations.map { b ->
                mess.clear()
                result.result.schedules.map { a ->
                    if (b.destination == a.destination) {
                        mess.add(a.message)
                    }
                }
                schedule.add(Schedule(b.destination, mess.toList(),lineCode))
            }
        }
    }

    private fun getAllScheduleOfStation() {

        val listLinecode: MutableList<String> = arrayListOf()
        schedule = arrayListOf()
        runBlocking {
            val linklist = linkLineStationDao.getAllLinesFromStation(station.slug)
            linklist.map {
                listLinecode.add(it.codeLine)
            }

            listLinecode.map { it ->

                val mess: MutableList<String> = arrayListOf()
                val result = service.getSchedulesTrains(it, station.slug)
                val destinations = result.result.schedules.distinctBy { it.destination }

                destinations.map { b ->
                    mess.clear()
                    result.result.schedules.map { a ->
                        if (b.destination == a.destination) {
                            mess.add(a.message)
                        }
                    }
                    schedule.add(Schedule(b.destination, mess.toList(),it))
                }

            }
        }
    }
}

