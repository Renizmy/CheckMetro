package com.example.checkmetro.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.checkmetro.R
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult

class ScanFragment : Fragment() {

    private lateinit var lineCode: String
    private lateinit var slugStation: String

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

       val root= inflater.inflate(R.layout.fragment_scan, container, false)
        run {
            Log.d("frt", "commence scan")
            //IntentIntegrator(requireActivity()).initiateScan();
            IntentIntegrator.forSupportFragment(this).initiateScan()
        }
        return root;
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result: IntentResult? =
            IntentIntegrator.parseActivityResult(requestCode, resultCode, data)

        if (result != null) {
            if (result.contents != null) {
                    val parts =result.contents.split("/")
                var i=0;
                parts.map{
                    if(i==0){
                         lineCode=it
                    }
                    if(i==1){
                         slugStation=it
                    }
                    i++
                }
               val target=ScanFragmentDirections.actionNavigationSearchToStationDetailsFragment(slugStation,lineCode)
                Navigation.findNavController(requireView()).navigate(target)

            } else {
                Toast.makeText(
                    requireContext(),
                    "Scan failed", //Insert Product in database
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}
