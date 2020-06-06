package com.example.checkmetro.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.checkmetro.R
import kotlinx.android.synthetic.main.ic_line_view.view.*

class LineIcFragmentAdapter(val ic:List<String>): RecyclerView.Adapter<LineIcFragmentAdapter.LineIcViewHolder>() {

    class LineIcViewHolder(val ic_line_view:View):RecyclerView.ViewHolder(ic_line_view) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LineIcViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.ic_line_view, parent, false)
        return LineIcFragmentAdapter.LineIcViewHolder(view)
    }

    override fun getItemCount(): Int = ic.size

    override fun onBindViewHolder(holder: LineIcViewHolder, position: Int) {
        val ic=ic[position]

        val uri="ic_m"+ic.toLowerCase()+"genrvb"
        val id=holder.ic_line_view.context.resources.getIdentifier(uri,"drawable",holder.ic_line_view.context.packageName)

        Glide.with(holder.ic_line_view).load(id).centerCrop().placeholder(R.drawable.ic_error_outline).into(holder.ic_line_view.iv_ic_line)

        holder.ic_line_view.setOnClickListener { v ->
            val parent = v.parent as View
            parent.performClick()
        }
    }
}
