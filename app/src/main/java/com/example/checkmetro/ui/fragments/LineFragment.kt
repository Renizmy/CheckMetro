package com.example.checkmetro.ui.fragments

import android.os.Bundle
import android.view.*
import android.util.Log
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.MergeAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.checkmetro.R
import com.example.checkmetro.adapter.LineFragmentAdapter
import com.example.checkmetro.adapter.LineStationFragmentAdapter
import com.example.checkmetro.data.LineDao
import com.example.checkmetro.data.LinkLineStationDao
import com.example.checkmetro.data.StationDao
import com.example.checkmetro.model.LineTraffic
import com.example.checkmetro.model.StationsLine
import com.example.checkmetro.service.APIRATP
import com.example.checkmetro.utils.dao
import com.example.checkmetro.utils.retrofit
import kotlinx.coroutines.runBlocking


class LineFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var lineDao: LineDao
    private lateinit var adapter: LineFragmentAdapter

    private lateinit var adapterStation: LineStationFragmentAdapter

    private lateinit var linkLineStationDao: LinkLineStationDao
    private lateinit var stationDao: StationDao

    private lateinit var lineTrafficList: MutableList<LineTraffic>
    private lateinit var lineTrafficListCopy: MutableList<LineTraffic>

    private lateinit var lineStationList: MutableList<StationsLine>
    private lateinit var lineStationListCopy: MutableList<StationsLine>
    private lateinit var mergeAdapter: MergeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.line_menu, menu)

        val searchItem = menu.findItem(R.id.action_bar_search)
        val searchView: SearchView = searchItem.actionView as SearchView
        searchView.setOnCloseListener {
            lineStationList.clear()
            adapterStation.notifyDataSetChanged()
            false
        }
        
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                updateData()
                adapter.filter(query)
                adapterStation.filter(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                updateData()
                adapter.filter(newText)
                adapterStation.filter(newText)
                return true
            }
        })

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.action_map -> {
                val target = LineFragmentDirections.actionNavigationLinesToMapFragment()
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

        lineDao = dao().getLineDao()
        linkLineStationDao = dao().getLinkLineStationDao()
        stationDao = dao().getStationDao()

        val root = inflater.inflate(R.layout.fragment_line, container, false)
        recyclerView = root.findViewById(R.id.lines_recyclerview)
        recyclerView.layoutManager =   LinearLayoutManager(this.requireContext(), LinearLayoutManager.VERTICAL, false)
        lineTrafficList = arrayListOf()
        getAllLines()

        lineTrafficListCopy = arrayListOf()
        lineTrafficListCopy.addAll(lineTrafficList)

        lineStationList= arrayListOf()

        adapter = LineFragmentAdapter(lineTrafficList)
        adapterStation= LineStationFragmentAdapter(lineStationList)

        mergeAdapter=MergeAdapter(adapter,adapterStation)

        recyclerView.adapter = mergeAdapter

        getAllStations()
        lineStationListCopy= arrayListOf()
        lineStationListCopy.addAll(lineStationList)

        return root
    }

    private fun getAllStations() {
        runBlocking {
            lineStationList.clear()
            val stations = stationDao.getStations()
           val liste= stations.distinctBy { it.slug }
            var listlink = mutableListOf<String>()
            liste.map {
                listlink = arrayListOf()
                val link = linkLineStationDao.getAllLinesFromStation(it.slug)
                val codeLine = link[0].codeLine
                link.map { it2 ->
                    if (codeLine != it2.codeLine) {
                        listlink.add(it2.codeLine)
                    }
                }
                lineStationList.add(StationsLine(it.name, it.slug, codeLine, listlink))
            }
        }
    }

    private fun getAllLines() {
        runBlocking {
            val lines = lineDao.getLines()
            val service = retrofit("https://api-ratp.pierre-grimaud.fr").create(APIRATP::class.java)
            val result = service.getMetroTraffic()
            lineTrafficList.clear()
            lines.map { it ->
                result.result.metros.map { it2 ->
                    if (it.code == it2.line.toLowerCase()) {
                        lineTrafficList.add(LineTraffic(it.code, it.name, it2.slug))
                    }
                }
                if (it.code == "Orv" || it.code == "Fun") {
                    lineTrafficList.add(LineTraffic(it.code, it.name, "normal"))
                }
            }
        }
    }

    private fun updateData() {
        lineTrafficList.clear()
        lineTrafficList.addAll(lineTrafficListCopy)

        lineStationList.clear()
        lineStationList.addAll(lineStationListCopy)

        mergeAdapter.notifyDataSetChanged()
    }
}
