package com.pds.chambitas.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.pds.chambitas.R
import com.pds.chambitas.body.PedirservicioFragment
import com.pds.chambitas.databinding.FragmentHomeBinding
import com.pds.chambitas.ui.slideshow.CambiocontraFragment
import com.pds.chambitas.util.LocationService
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.fragment_pedirservicio.view.*
import kotlinx.android.synthetic.main.fragment_slideshow.*

class HomeFragment : Fragment() {

    private lateinit var mMap: GoogleMap
    private var button: Button? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        root.btnCentrarUbi.setOnClickListener {

            val punto = LatLng(LocationService.loc.latitude, LocationService.loc.longitude)
            mMap.addMarker(MarkerOptions().position(punto).title("Yo"))
            mMap.moveCamera(CameraUpdateFactory.newLatLng(punto))
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(punto,16.0f))
            println("ENTRO")
        }

        val nav = Navigation.createNavigateOnClickListener(R.id.action_nav_home_to_pedirservicioFragment)
        root.btnBusquedaHome.setOnClickListener {
            nav.onClick(it)
        }
//
//
//        button = root.findViewById(R.id.btnBusquedaHome)
//        button!!.setOnClickListener(View.OnClickListener {
//
//            val fragmentManager = requireActivity().supportFragmentManager
//
//            fragmentManager.commit {
//                setReorderingAllowed(true)
//                replace<PedirservicioFragment>(R.id.fragmentPedirservicio)
//            }
//
//            //Ocultar botones
//            fragmentPedirservicio.setVisibility(View.VISIBLE)
//            btnCentrarUbi.setVisibility(View.GONE)
//            btnBusquedaHome.setVisibility(View.GONE)
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
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