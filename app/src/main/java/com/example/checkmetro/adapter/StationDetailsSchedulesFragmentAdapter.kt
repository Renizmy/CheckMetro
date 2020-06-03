package com.example.checkmetro.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.checkmetro.R
import kotlinx.android.synthetic.main.schedule_view.view.*

class StationDetailsSchedulesFragmentAdapter(val horaires:List<String>):RecyclerView.Adapter<StationDetailsSchedulesFragmentAdapter.StationDetailsSchedulesViewHolder>() {
    class StationDetailsSchedulesViewHolder(val scheduleview: View):RecyclerView.ViewHolder(scheduleview)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): StationDetailsSchedulesViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.schedule_view, parent, false)
        return StationDetailsSchedulesFragmentAdapter.StationDetailsSchedulesViewHolder(view)
    }

    override fun getItemCount(): Int =horaires.size


    override fun onBindViewHolder(holder: StationDetailsSchedulesViewHolder, position: Int) {

        val horaire=horaires[position]
        holder.scheduleview.schedule_view_message_textview.text=horaire
    }
}

