package com.pds.chambitas.body

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.navigation.Navigation
import com.pds.chambitas.R
import com.pds.chambitas.ui.home.HomeFragment
import com.pds.chambitas.ui.slideshow.SlideshowFragment
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_pedirservicio.*
import kotlinx.android.synthetic.main.fragment_pedirservicio.view.*

class PedirservicioFragment : Fragment() {

    private var editText : EditText?=null
    private var editText2 : EditText?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_pedirservicio, container, false)


        val back = Navigation.createNavigateOnClickListener(R.id.action_pedirservicioFragment_to_nav_home)
        root.btnBack.setOnClickListener {
            back.onClick(it)
        }

        val nav = Navigation.createNavigateOnClickListener(R.id.action_pedirservicioFragment_to_elegirdestinoFragment)
        root.etxtIngresaDestino.setOnClickListener {
            nav.onClick(it)
        }

        val nav2 = Navigation.createNavigateOnClickListener(R.id.action_pedirservicioFragment_to_confirmarservicioFragment)
        root.btnConfirmarpedirservicio.setOnClickListener {
            nav2.onClick(it)
        }



//        editText = root.findViewById(R.id.etxtIngresaDestino)
//        editText!!.setOnClickListener(View.OnClickListener {
//
//            val fragmentManager = requireActivity().supportFragmentManager
//
//            fragmentManager.commit {
//                setReorderingAllowed(true)
//                replace<ElegirdestinoFragment>(R.id.fragmentElegirDestino)
//            }
//
//            //Ocultar botones
//            fragmentElegirDestino.setVisibility(View.VISIBLE)
//            btnBack.setVisibility(View.GONE)
//            lstTrabajos.setVisibility(View.GONE)
//            layoutPedirservicio.setVisibility(View.GONE)
//
//
////            Toast.makeText(
////                context,
////                "HOLAAAA",
////                Toast.LENGTH_LONG
////            ).show()
//        })

//        editText2 = root.findViewById(R.id.etxtIngresaTrabajo)
//        editText2!!.setOnClickListener(View.OnClickListener {
//
//            val fragmentManager = requireActivity().supportFragmentManager
//
//            fragmentManager.commit {
//                setReorderingAllowed(true)
//                replace<ConfirmarservicioFragment>(R.id.fragmentConfirmarServicio)
//            }
//
//            //Ocultar botones
//            fragmentConfirmarServicio.setVisibility(View.VISIBLE)
//            nav_pedirServicio.setVisibility(View.GONE)
////            btnBack.setVisibility(View.GONE)
////            textView15.setVisibility(View.GONE)
//            //layoutPedirservicio.setVisibility(View.GONE)
//
//
////            Toast.makeText(
////                context,
////                "HOLAAAA",
////                Toast.LENGTH_LONG
////            ).show()
//        })

        return root
    }


}