package com.pds.chambitas.body

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.Navigation
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
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

            var idService = ""
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

    override fun onDestroyView() {
        super.onDestroyView()
        registration.remove()
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

                            // Manda notificacion
                            apiService.sendNotification(
                                POSTData(
                                    "d5PkbDtBQ2mJ905dS-28r5:APA91bHd9GMh2fu2JbQZjG4w4g2O-pZy6hE3gYDAx43Ga9IykpR59cfOzWZxEI3hHFAyIlyoJN8PyP1JsDZwAnKxqcbzWAQyM456IjD_VJ4-NYPN8CP8E-8ePshWqXDO5D0Q9Nk2hwNx",
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
                                        TODO("Not yet implemented")
                                    }
                                }
                            )
                        }
                        SERVICE_PENDIENTE -> {
                            Log.d("ServiceStatus", "Pendiente")
                        }
                        SERVICE_EN_CAMINO -> {



                            Log.d("ServiceStatus", "En camino")
                            Cargando.visibility = View.GONE
                        }
                        SERVICE_TERMINADO -> {
                            Log.d("ServiceStatus", "Terminado")
                            Navigation.findNavController(root).navigate(R.id.action_aceptacionservicioFragment_to_finalizarservicioFragment)
                        }
                        SERVICE_CANCELADO -> {
                            Log.d("ServiceStatus", "Cancelado")
                            val updates = hashMapOf<String, Any>(
                                "servicioActivo" to false
                            )
                            db.collection("servicios").document(doc.id).update(updates)
                            Navigation.findNavController(root).navigate(R.id.action_aceptacionservicioFragment_to_nav_home)
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