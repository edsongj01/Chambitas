package com.pds.chambitas.body

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.navigation.NavDeepLinkBuilder
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.pds.chambitas.MainActivity
import com.pds.chambitas.MenuActivity
import com.pds.chambitas.R
import com.pds.chambitas.util.Constants
import com.pds.chambitas.util.Constants.Companion.SERVICE_CANCELADO
import com.pds.chambitas.util.Constants.Companion.SERVICE_EN_CAMINO
import com.pds.chambitas.util.Constants.Companion.SERVICE_EN_ESPERA
import com.pds.chambitas.util.Constants.Companion.SERVICE_FINALIZADO
import com.pds.chambitas.util.Constants.Companion.SERVICE_PENDIENTE
import com.pds.chambitas.util.Constants.Companion.SERVICE_TERMINADO
import com.pds.chambitas.util.ForegroundLocationService
import com.pds.chambitas.util.services.APIService
import com.pds.chambitas.util.services.ApiUtils
import kotlinx.android.synthetic.main.fragment_aceptacionservicio.*
import kotlinx.android.synthetic.main.fragment_aceptacionservicio.view.*
import kotlinx.android.synthetic.main.fragment_confirmarservicio.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class AceptacionservicioFragment : Fragment() {

    private lateinit var nMap: GoogleMap
    private lateinit var auth: FirebaseAuth
    var db = Firebase.firestore
    private lateinit var root: View
    private lateinit var registration: ListenerRegistration
    private lateinit var prestadorRegistration: ListenerRegistration
    var idService = ""

    private lateinit var markerOptions: MarkerOptions
    var mapMarker: Marker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_aceptacionservicio, container, false)


        root.btnCancelarServicio.setOnClickListener { view ->
            //root.Cargando.setVisibility(View.GONE)
            //root.linearEstado.setVisibility(View.VISIBLE)


            val updates = hashMapOf<String, Any>(
                "estado" to SERVICE_CANCELADO
            )

            val user = auth.currentUser

            db.collection("servicios")
                .whereEqualTo("user_regular", user!!.uid)
                .whereEqualTo("servicioActivo", true)
                .limit(1)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        idService = document.id
                    }

                    db.collection("servicios").document(idService).update(updates)
                }
        }


        root.btnChat.setOnClickListener {
            if (idService.isNotEmpty()) {
                val chat = Navigation.createNavigateOnClickListener(R.id.action_aceptacionservicioFragment_to_chatFragment,
                    bundleOf("idService" to idService)
                )
                chat.onClick(it)
            }
        }
/*
        val finalizar = Navigation.createNavigateOnClickListener(R.id.action_aceptacionservicioFragment_to_finalizarservicioFragment)
        root.imageView7.setOnClickListener {
            finalizar.onClick(it)
        }
 */

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapaceptarservicio) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        if(mapMarker != null) mapMarker!!.remove()
        if (::registration.isInitialized) registration.remove()
        if (::prestadorRegistration.isInitialized) prestadorRegistration.remove()
    }

    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->
        nMap = googleMap
        nMap.isMyLocationEnabled = true

        moveMyCamera(LatLng(
            ForegroundLocationService.myLocation!!.latitude,
            ForegroundLocationService.myLocation!!.longitude))

        getServiceStatus()
    }

    private fun getServiceStatus() {
        val user = auth.currentUser



        registration = db.collection("servicios")
            .whereEqualTo("user_regular", user!!.uid)
            .whereEqualTo("servicioActivo", true)
            .limit(1)
            .addSnapshotListener { document, error ->
                if (error != null) {
                    Log.d("ServiceStatus", "Error al escuchar el documento", error)
                }
                for (doc in document!!) {
                    when (doc.data["estado"]) {
                        SERVICE_EN_ESPERA -> {
                            Log.d("ServiceStatus", "En espera")

                            // Creacion de la api para su uso
                            var apiService: APIService? = null
                            apiService = ApiUtils.apiService

                            db.collection("usuarios")
                                .whereEqualTo("type", "prestador")
                                .get()
                                .addOnSuccessListener { usuarios ->
                                    if (usuarios != null && !usuarios.isEmpty) {
                                        usuarios.documents.forEach { usuario ->

                                            Log.d("ServiceStatus", "Usuario: ${usuario.data!!["name"].toString()} - Token: ${usuario.data!!["token"].toString()}")

                                            // Manda notificacion
                                            apiService.sendNotification(
                                                POSTData(
                                                    usuario.data!!["token"].toString(),
                                                    Data("request", doc.id)
                                                )
                                            ).enqueue(
                                                object: Callback<Void> {
                                                    override fun onResponse(call: Call<Void>,response: Response<Void>) {
                                                        if (response.isSuccessful) {
                                                            // Actualiza estado
                                                            val estado = hashMapOf<String, Any>(
                                                                "estado" to SERVICE_PENDIENTE
                                                            )
                                                            db.collection("servicios").document(doc.id).update(estado)
                                                        }
                                                    }

                                                    override fun onFailure(call: Call<Void>, t: Throwable) {
                                                        Log.d("ServiceStatus", "No se pudo mandar la notificacion al usuario", t)
                                                    }
                                                }
                                            )
                                        }
                                    }
                                }
                                .addOnFailureListener {
                                    Log.d("ServiceStatus", "No se pudieron obtener los usuarios", it)
                                }


                        }
                        SERVICE_PENDIENTE -> {
                            Log.d("ServiceStatus", "Pendiente")
                        }
                        SERVICE_EN_CAMINO -> {

                            Log.d("ServiceStatus", "En camino")
                            Cargando.visibility = View.GONE

                            idService = doc.id

                            prestadorRegistration = db.collection("usuarios").document(doc.data["user_prestador"].toString())
                                .addSnapshotListener { user, u_error ->
                                    if (user != null) {
                                        Log.d("ServiceStatus", "Location Prestador: (${user.data!!["latitude"].toString()} , ${user.data!!["longitude"].toString()})")

                                        textView23.setText(user.data!!["name"].toString())
                                        textView29.setText(doc.data["direction"].toString())
                                        textView30.setText(doc.data["service"].toString())
                                        textView25.setText(user.data!!["car"].toString())

                                        val prestador_location = LatLng(
                                            user.data!!["latitude"] as Double,
                                            user.data!!["longitude"] as Double,
                                        )

                                        markerOptions = MarkerOptions()
                                            .position(prestador_location)
                                            .title("Prestador")

                                        if (mapMarker == null) {
                                            mapMarker = nMap.addMarker(markerOptions)
                                        } else {
                                            mapMarker!!.remove()
                                            mapMarker = nMap.addMarker(markerOptions)
                                            mapMarker!!.position = prestador_location
                                        }
                                    }
                                }
                        }
                        SERVICE_TERMINADO -> {
                            Log.d("ServiceStatus", "Terminado")
                            //findNavController(requireActivity(), R.id.nav_host_fragment_content_menu).navigate(R.id.action_aceptacionservicioFragment_to_finalizarservicioFragment)
                            val pendingIntentFragment = NavDeepLinkBuilder(requireContext())
                                .setComponentName(MenuActivity::class.java)
                                .setGraph(R.navigation.mobile_navigation)
                                .setDestination(R.id.finalizarservicioFragment)
                                .createPendingIntent()
                            pendingIntentFragment.send()
                        }
                        SERVICE_CANCELADO -> {
                            Log.d("ServiceStatus", "Cancelado")
                            val updates = hashMapOf<String, Any>(
                                "servicioActivo" to false
                            )
                            db.collection("servicios").document(doc.id).update(updates)
                            //findNavController(requireActivity(), R.id.nav_host_fragment_content_menu).navigate(R.id.action_aceptacionservicioFragment_to_nav_home)
                            val pendingIntentFragment = NavDeepLinkBuilder(requireContext())
                                .setComponentName(MenuActivity::class.java)
                                .setGraph(R.navigation.mobile_navigation)
                                .setDestination(R.id.nav_home)
                                .createPendingIntent()
                            pendingIntentFragment.send()
                        }
                    }
                }
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