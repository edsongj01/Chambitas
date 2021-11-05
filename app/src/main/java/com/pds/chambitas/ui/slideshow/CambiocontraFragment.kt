package com.pds.chambitas.ui.slideshow

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.pds.chambitas.R
import com.pds.chambitas.ui.home.HomeFragment
import kotlinx.android.synthetic.main.fragment_cambiocontra.*
import kotlinx.android.synthetic.main.fragment_cambiocontra.view.*
import kotlinx.android.synthetic.main.fragment_slideshow.*

class CambiocontraFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root:View = inflater.inflate(R.layout.fragment_cambiocontra, container, false)

        root.btnContraG.setOnClickListener {
            val fragmentManager = requireActivity().supportFragmentManager

            var home:Fragment = SlideshowFragment()

            val fragmentTransaction =  fragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.nav_regreso, home)
            fragmentTransaction?.commit()

            //constraintLayoutCC.setVisibility(View.GONE)

            Toast.makeText(context,"Saludos prro ", Toast.LENGTH_LONG).show()
        }

        return root
    }


}
