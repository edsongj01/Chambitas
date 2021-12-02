package com.pds.chambitas.body

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.pds.chambitas.R
import kotlinx.android.synthetic.main.fragment_aceptacionservicio.view.*
import kotlinx.android.synthetic.main.fragment_confirmarservicio.view.*

class AceptacionservicioFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var root = inflater.inflate(R.layout.fragment_aceptacionservicio, container, false)

        val cancelar = Navigation.createNavigateOnClickListener(R.id.action_aceptacionservicioFragment_to_nav_home)
        root.btnCancelarServicio.setOnClickListener {
            root.Cargando.setVisibility(View.GONE)
            root.linearEstado.setVisibility(View.VISIBLE)
            //cancelar.onClick(it)
        }

        val chat = Navigation.createNavigateOnClickListener(R.id.action_aceptacionservicioFragment_to_chatFragment)
        root.btnChat.setOnClickListener {
            chat.onClick(it)
        }

        val finalizar = Navigation.createNavigateOnClickListener(R.id.action_aceptacionservicioFragment_to_finalizarservicioFragment)
        root.imageView7.setOnClickListener {
            finalizar.onClick(it)
        }

        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapaceptarservicio) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        val sydney = LatLng(-34.0, 151.0)
        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

}