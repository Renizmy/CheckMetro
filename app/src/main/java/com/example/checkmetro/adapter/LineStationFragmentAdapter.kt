package com.example.checkmetro.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.checkmetro.R
import com.example.checkmetro.model.StationsLine
import com.example.checkmetro.ui.fragments.LineFragmentDirections
import kotlinx.android.synthetic.main.station_view.view.*

class LineStationFragmentAdapter (val stations: MutableList<StationsLine>) : RecyclerView.Adapter<LineStationFragmentAdapter.LineStationViewHolder>() {
    private lateinit var  stationCopy : MutableList<StationsLine>

    class LineStationViewHolder(val stationview: View) : RecyclerView.ViewHolder(stationview){
        val recyclerView:RecyclerView=stationview.rv_ic_lines
    }

    fun filter(text: String) {
        stationCopy= arrayListOf()
        stationCopy.addAll(stations)
        stations.clear()
        if (text.isEmpty()) {
            stations.addAll(stationCopy)
        } else {

            for (item in stationCopy) {
                val a=item.name.replace("é","e").toLowerCase()
                val b=text.replace("é","e").toLowerCase()
                if (a.contains(b)) {
                    stations.add(item)
                }
            }
        }
        notifyDataSetChanged()
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LineStationViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.station_view, parent, false)
        return LineStationViewHolder(view)
    }

    override fun getItemCount(): Int =stations.size


    override fun onBindViewHolder(holder: LineStationViewHolder, position: Int) {
        val station = stations[position]
        holder.stationview.station_name_textview.text=station.name

        holder.stationview.setOnClickListener{
            val action=LineFragmentDirections.actionNavigationLinesToStationDetailsFragment(station.slug,station.line)
            Navigation.findNavController(holder.stationview).navigate(action)
        }

        val viewPool = RecyclerView.RecycledViewPool()
        holder.recyclerView.apply{
            layoutManager=LinearLayoutManager(holder.recyclerView.context, LinearLayoutManager.HORIZONTAL, false)
            adapter=FavoritesFragmentIcAdapter(station.related_lines)
            setRecycledViewPool(viewPool)
        }

        holder.stationview.rv_ic_lines.setOnClickListener {
            val action=LineFragmentDirections.actionNavigationLinesToStationDetailsFragment(station.slug,station.line)
            Navigation.findNavController(holder.stationview).navigate(action)
        }

    }
}
