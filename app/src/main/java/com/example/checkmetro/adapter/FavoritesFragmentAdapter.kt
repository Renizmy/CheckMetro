package com.example.checkmetro.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.checkmetro.R
import com.example.checkmetro.model.StationsLine
import com.example.checkmetro.ui.fragments.FavoritesFragmentDirections
import kotlinx.android.synthetic.main.station_view.view.*

class FavoritesFragmentAdapter (val stations:List<StationsLine>) : RecyclerView.Adapter<FavoritesFragmentAdapter.FavoritesViewHolder>(){

    class FavoritesViewHolder(val favoriteview: View):RecyclerView.ViewHolder(favoriteview){
        val recyclerView:RecyclerView=favoriteview.rv_ic_lines
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.station_view, parent, false)
        return FavoritesViewHolder(view)
    }

    override fun getItemCount(): Int=stations.size

    override fun onBindViewHolder(holder: FavoritesViewHolder, position: Int) {

        val station=stations[position]
        holder.favoriteview.station_name_textview.text=station.name
        holder.favoriteview.setOnClickListener{
            val action= FavoritesFragmentDirections.actionNavigationFavoritesToStationDetailsFragment(station.slug,station.line)
                Navigation.findNavController(holder.favoriteview).navigate(action)
        }


        val viewPool = RecyclerView.RecycledViewPool()
        holder.recyclerView.apply{
            layoutManager= LinearLayoutManager(holder.recyclerView.context, LinearLayoutManager.HORIZONTAL, false)
            adapter=LineIcFragmentAdapter(station.related_lines)
            setRecycledViewPool(viewPool)
        }
        
       holder.favoriteview.rv_ic_lines.setOnClickListener {
           val action= FavoritesFragmentDirections.actionNavigationFavoritesToStationDetailsFragment(station.slug,station.line)
           Navigation.findNavController(holder.favoriteview).navigate(action)

       }
    }
}

