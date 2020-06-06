package com.example.checkmetro.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.checkmetro.R
import com.example.checkmetro.model.LineTraffic
import com.example.checkmetro.ui.fragments.LineFragmentDirections
import kotlinx.android.synthetic.main.line_view.view.*


class LineFragmentAdapter(val lines: MutableList<LineTraffic>) : RecyclerView.Adapter<LineFragmentAdapter.LineViewHolder>() {
    private lateinit var  linesCopy : MutableList<LineTraffic>

    class LineViewHolder(val lineview: View) : RecyclerView.ViewHolder(lineview)

    fun filter(text: String) {
        linesCopy= arrayListOf()
        linesCopy.addAll(lines)
        lines.clear()
        if (text.isEmpty()) {
            lines.addAll(linesCopy)
        } else {

            for (item in linesCopy) {
                val a=item.name.replace("é","e").toLowerCase()
                val b=text.replace("é","e").toLowerCase()
                if (a.contains(b)) {
                    lines.add(item)
                }
            }
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LineViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.line_view, parent, false)

        return LineViewHolder(view)
    }

    override fun getItemCount(): Int = lines.size

    override fun onBindViewHolder(holder: LineViewHolder, position: Int) {
        val line = lines[position]
        holder.lineview.line_name_textview.text=line.name
        holder.lineview.setOnClickListener {

            val actionDetail =  LineFragmentDirections.actionNavigationLinesToStationFragment(line.code)
            Navigation.findNavController(holder.lineview).navigate(actionDetail)
        }

        val uri="ic_m"+line.code.toLowerCase()+"genrvb"
        val id=holder.lineview.context.resources.getIdentifier(uri,"drawable",holder.lineview.context.packageName)
        Glide.with(holder.lineview).load(id).centerCrop().placeholder(R.drawable.ic_error_outline).into(holder.lineview.line_imageview_ic)


        when (line.slug){
            "critical" -> Glide.with(holder.lineview).load(R.drawable.ic_warning_red).centerCrop().into(holder.lineview.line_imageview_traffic)
            "normal_trav"->  Glide.with(holder.lineview).load(R.drawable.ic_warning_orange).centerCrop().into(holder.lineview.line_imageview_traffic)
            "normal"-> holder.lineview.line_imageview_traffic.setImageDrawable(null)
            else -> holder.lineview.line_imageview_traffic.setImageDrawable(null)
        }


   }

}