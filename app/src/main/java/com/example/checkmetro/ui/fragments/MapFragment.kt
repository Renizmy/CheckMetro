package com.example.checkmetro.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.checkmetro.R
import com.github.chrisbanes.photoview.PhotoView


/**
 * A simple [Fragment] subclass.
 */
class MapFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_map, container, false)
        val photoView = root.findViewById(R.id.photo_view) as PhotoView
        photoView.setImageResource(R.drawable.map)
        return root
    }

}
