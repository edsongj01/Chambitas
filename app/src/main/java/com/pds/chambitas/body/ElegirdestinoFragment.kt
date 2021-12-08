package com.pds.chambitas.body

import android.annotation.SuppressLint
import android.location.Geocoder
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.pds.chambitas.R
import com.pds.chambitas.util.ForegroundLocationService
import kotlinx.android.synthetic.main.fragment_elegirdestino.*
import kotlinx.android.synthetic.main.fragment_elegirdestino.view.*
import java.lang.Exception
import java.util.*

class ElegirdestinoFragment : Fragment() {

    lateinit var nMap: GoogleMap
    var bundle_text: String = ""
    var bundle_lat: Double = 0.0
    var bundle_long: Double = 0.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_elegirdestino, container, false)


        root.btnConfirmardestino.setOnClickListener {
            if (bundle_text.isNotEmpty()) {
                val serviceDetails = ServiceDetail(bundle_text, bundle_lat, bundle_long, null)
                val bundle = bundleOf(
                    "serviceDetail" to serviceDetails
                )
                val destino = Navigation.createNavigateOnClickListener(
                    R.id.action_elegirdestinoFragment_to_pedirservicioFragment,
                    bundle
                )
                destino.onClick(it)
            }
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapelegirdestino) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->
        nMap = googleMap
        nMap.isMyLocationEnabled = true

        nMap.setOnCameraIdleListener {
            val lat = nMap.cameraPosition.target.latitude
            val long = nMap.cameraPosition.target.longitude
            encontrarDireccion(lat, long)
        }

        moveMyCamera(LatLng(
            ForegroundLocationService.myLocation!!.latitude,
            ForegroundLocationService.myLocation!!.longitude))
    }

    private fun moveMyCamera(myPosition: LatLng) {
        try {
            nMap.moveCamera(CameraUpdateFactory.newLatLng(myPosition))
            nMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myPosition, 19f))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun encontrarDireccion(latitud: Double, longitud: Double) {
        val geocoder = Geocoder(activity, Locale.getDefault())
        val address = geocoder.getFromLocation(latitud, longitud, 5)
        if (address.size > 0) {
            println("Coordenadas: ${address[0].getAddressLine(0)}")
            bundle_text = address[0].getAddressLine(0)
            bundle_lat = latitud
            bundle_long = longitud
            //Toast.makeText(activity, address[0].getAddressLine(0), Toast.LENGTH_LONG).show()
            etxtIngresaDestinoCambio.setText(address[0].getAddressLine(0))
        }

    }

}