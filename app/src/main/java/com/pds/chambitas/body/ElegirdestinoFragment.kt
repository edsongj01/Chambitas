package com.pds.chambitas.body

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.navigation.Navigation
import com.pds.chambitas.R
import com.pds.chambitas.ui.home.HomeFragment
import kotlinx.android.synthetic.main.fragment_confirmarservicio.view.*
import kotlinx.android.synthetic.main.fragment_elegirdestino.*
import kotlinx.android.synthetic.main.fragment_elegirdestino.view.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_pedirservicio.view.*
import kotlinx.android.synthetic.main.fragment_pedirservicio.view.btnBack

class ElegirdestinoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_elegirdestino, container, false)

        val destino = Navigation.createNavigateOnClickListener(R.id.action_elegirdestinoFragment_to_pedirservicioFragment)
        root.btnConfirmardestino.setOnClickListener {
            destino.onClick(it)
        }

        val back = Navigation.createNavigateOnClickListener(R.id.action_elegirdestinoFragment_to_pedirservicioFragment)
        root.btnBack.setOnClickListener {
            back.onClick(it)
        }

        return root
    }

}