package com.example.checkmetro.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.checkmetro.R
import kotlinx.android.synthetic.main.ic_line_view.view.*

class FavoritesFragmentIcAdapter(val ic: List<String>) :
    RecyclerView.Adapter<FavoritesFragmentIcAdapter.FavoritesIcViewHolder>() {
    class FavoritesIcViewHolder(val favoriteicview: View) : RecyclerView.ViewHolder(favoriteicview)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesIcViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.ic_line_view, parent, false)
        return FavoritesIcViewHolder(view)
    }

    override fun getItemCount(): Int = ic.size

    override fun onBindViewHolder(holder: FavoritesIcViewHolder, position: Int) {
        val ic = ic[position]
        val uri = "ic_m" + ic.toLowerCase() + "genrvb"
        val id = holder.favoriteicview.context.resources.getIdentifier(
            uri,
            "drawable",
            holder.favoriteicview.context.packageName
        )
        Glide.with(holder.favoriteicview).load(id).centerCrop()
            .placeholder(R.drawable.ic_error_outline).into(holder.favoriteicview.iv_ic_line)

        holder.favoriteicview.setOnClickListener { v ->
            val parent = v.parent as View
            parent.performClick()
        }

    }
}
