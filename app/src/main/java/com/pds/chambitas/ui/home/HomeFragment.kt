package com.pds.chambitas.ui.home

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.pds.chambitas.R
import com.pds.chambitas.body.AceptacionservicioFragment
import com.pds.chambitas.util.Constants
import com.pds.chambitas.util.Constants.Companion.SERVICE_CANCELADO
import com.pds.chambitas.util.Constants.Companion.SERVICE_FINALIZADO
import com.pds.chambitas.util.Constants.Companion.SERVICE_TERMINADO
import com.pds.chambitas.util.ForegroundLocationService
import com.pds.chambitas.util.LocationService
import kotlinx.android.synthetic.main.fragment_home.view.*
import java.lang.Exception

class HomeFragment : Fragment() {

    lateinit var nMap: GoogleMap
    lateinit var receiver: BroadcastReceiver
    lateinit var auth: FirebaseAuth
    private val db = Firebase.firestore
    var isUSerMovingCamera: Boolean = false

    private inner class MyLocationReceiver: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            val location: Location? = intent.getParcelableExtra<Location>(Constants.EXTRA_LOCATION)
            if (location != null) {
                Log.d("Location receiver", "(${location.latitude}, ${location.longitude})")
                val myLocation = LatLng(location.latitude, location.longitude)
                if (::nMap.isInitialized) {
                    if (!isUSerMovingCamera) moveMyCamera(myLocation)
                    val updates = hashMapOf<String, Any>(
                        "latitude" to myLocation.latitude,
                        "longitude" to myLocation.longitude,
                    )
                    val user = auth.currentUser
                    user?.let {
                        val uid = it.uid
                        db.collection("usuarios").document(uid).update(updates)
                            .addOnCompleteListener {
                                Log.d("RegistroUser", "Localizacion actualizada")
                            }
                            .addOnFailureListener { e ->
                                Log.e("RegistroUser", "Error al cambiar la localizacion del usuario", e)
                            }
                    }
                }
            }
        }
    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        auth = Firebase.auth

        db.collection("servicios")
            .whereEqualTo("user_regular", auth.uid)
            .whereEqualTo("servicioActivo", true)
            .limit(1)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && !document.isEmpty) {
                    Log.d("ServicioActivo", "Se encontro un servicio en proceso")
                    Navigation.findNavController(root).navigate(R.id.action_nav_home_to_aceptacionservicioFragment)
                }
            }
            .addOnFailureListener {
                Log.d("ServicioActivo", "No se pudo encontrar el servicio solicitado", it)
            }

        root.btnCentrarUbi.setOnClickListener {
            println("Centrar")
            moveMyCamera(LatLng(
                ForegroundLocationService.myLocation!!.latitude,
                ForegroundLocationService.myLocation!!.longitude))
            isUSerMovingCamera = false
        }

        val nav = Navigation.createNavigateOnClickListener(R.id.action_nav_home_to_pedirservicioFragment)
        root.btnBusquedaHome.setOnClickListener {
            nav.onClick(it)
        }

        receiver = MyLocationReceiver()
        LocalBroadcastManager.getInstance(requireActivity().applicationContext)
            .registerReceiver(receiver, IntentFilter(Constants.ACTION_BROADCAST))

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->
        nMap = googleMap
        nMap.isMyLocationEnabled = true

        nMap.setOnCameraMoveListener {
            isUSerMovingCamera = true
        }

        if (ForegroundLocationService.myLocation != null) {
            moveMyCamera(LatLng(
                ForegroundLocationService.myLocation!!.latitude,
                ForegroundLocationService.myLocation!!.longitude))
        }

    }

    private fun moveMyCamera(myPosition: LatLng) {
        try {
            nMap.moveCamera(CameraUpdateFactory.newLatLng(myPosition))
            nMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myPosition, 19f))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}