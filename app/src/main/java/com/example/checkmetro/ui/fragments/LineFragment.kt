package com.example.checkmetro.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.checkmetro.R
import com.example.checkmetro.adapter.LineFragmentAdapter
import com.example.checkmetro.data.AppDatabase
import com.example.checkmetro.data.LineDao
import com.example.checkmetro.model.Line
import com.example.checkmetro.model.LineTraffic
import com.example.checkmetro.model.StationsLine
import com.example.checkmetro.service.APIRATP
import com.example.checkmetro.utils.dao
import com.example.checkmetro.utils.retrofit
import kotlinx.coroutines.runBlocking

class LineFragment : Fragment() {

    private lateinit var lineDao: LineDao

    var lineTrafficList: MutableList<LineTraffic> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.line_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.action_map ->{
                val target=LineFragmentDirections.actionNavigationLinesToMapFragment()
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

       lineDao= dao().getLineDao()

        val root = inflater.inflate(R.layout.fragment_line, container, false)
        val recyclerView:RecyclerView=root.findViewById(R.id.lines_recyclerview)
        recyclerView.layoutManager= LinearLayoutManager(this.requireContext(), LinearLayoutManager.VERTICAL,false)
        runBlocking {
            val lines=lineDao.getLines()
            val service = retrofit("https://api-ratp.pierre-grimaud.fr").create(APIRATP::class.java)
            val result=service.getMetroTraffic()
            lineTrafficList= arrayListOf()
            lines.map{it ->
                result.result.metros.map { it2->
                    if(it.code==it2.line.toLowerCase()){
                        lineTrafficList.add(LineTraffic(it.code,it.name,it2.slug))
                    }
                }
                if(it.code=="Orv" || it.code=="Fun"){
                        lineTrafficList.add(LineTraffic(it.code,it.name,"normal"))
                }
            }

            recyclerView.adapter= LineFragmentAdapter(lineTrafficList.toList())
        }
        return root
    }
}
