package com.pds.chambitas.ui.configuracion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.navigation.Navigation
import com.pds.chambitas.R
import kotlinx.android.synthetic.main.fragment_aceptacionservicio.view.*
import kotlinx.android.synthetic.main.fragment_configuracion.*
import kotlinx.android.synthetic.main.fragment_configuracion.view.*


class ConfiguracionFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root: View = inflater.inflate(R.layout.fragment_configuracion, container, false)

        val cambiocontra = Navigation.createNavigateOnClickListener(R.id.action_nav_configuracion_to_cambiocontraFragment)
        root.btnCambiocontra.setOnClickListener {
            cambiocontra.onClick(it)
        }


        return root
    }

}