package com.example.checkmetro.adapter


import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.checkmetro.R
import com.example.checkmetro.model.Schedule
import kotlinx.android.synthetic.main.destination_view.view.*

class StationDetailsFragmentAdapter(val schedules: List<Schedule>):RecyclerView.Adapter<StationDetailsFragmentAdapter.StationDetailsViewHolder>()
{
    class StationDetailsViewHolder(val destination_view:View):RecyclerView.ViewHolder(destination_view){

        val recyclerView : RecyclerView = destination_view.destination_messages_recyclerview
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StationDetailsViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.destination_view, parent, false)
        return StationDetailsFragmentAdapter.StationDetailsViewHolder(view)
    }

    override fun getItemCount(): Int =schedules.size


    override fun onBindViewHolder(holder: StationDetailsViewHolder, position: Int) {
        val schedule=schedules[position]

        holder.destination_view.schedule_view_destination_textview.text=schedule.direction

        val uri="ic_m"+schedule.lineCode.toLowerCase()+"genrvb"
        val id=holder.destination_view.context.resources.getIdentifier(uri,"drawable",holder.destination_view.context.packageName)
        Log.d("blabla","blalbal")
        Glide.with(holder.destination_view).load(id).centerCrop().placeholder(R.drawable.ic_error_outline).into(holder.destination_view.iv_ic_line)

        val viewPool = RecyclerView.RecycledViewPool()
        holder.recyclerView.apply {
            layoutManager = LinearLayoutManager(holder.recyclerView.context, LinearLayoutManager.VERTICAL, false)
            adapter = StationDetailsSchedulesFragmentAdapter(schedule.message)
            setRecycledViewPool(viewPool)
        }

   }
}

