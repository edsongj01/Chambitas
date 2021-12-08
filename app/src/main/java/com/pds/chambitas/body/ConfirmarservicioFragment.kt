package com.pds.chambitas.body

import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.pds.chambitas.R
import com.pds.chambitas.util.Constants.Companion.SERVICE_EN_ESPERA
import kotlinx.android.synthetic.main.fragment_aceptacionservicio.view.*
import kotlinx.android.synthetic.main.fragment_confirmarservicio.*
import kotlinx.android.synthetic.main.fragment_confirmarservicio.view.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import java.sql.Date
import java.util.*


class ConfirmarservicioFragment : Fragment() {

    private lateinit var serviceDetail: ServiceDetail
    private lateinit var auth: FirebaseAuth
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        arguments?.let { bundle ->
            serviceDetail = bundle.getParcelable("serviceDetail")!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var root = inflater.inflate(R.layout.fragment_confirmarservicio, container, false)

        val confirmar = Navigation.createNavigateOnClickListener(R.id.action_confirmarservicioFragment_to_aceptacionservicioFragment)
        root.btnConfirmarservicio.setOnClickListener { view ->

            val user = auth.currentUser
            val data = hashMapOf(
                "estado" to SERVICE_EN_ESPERA,
                "servicioActivo" to true,
                "calificacion" to 0,
                "fecha_servicio" to Timestamp(Date()),
                "user_regular" to user!!.uid,
                "lat" to serviceDetail.lat,
                "lng" to serviceDetail.lng,
                "service" to serviceDetail.service,
                "direction" to serviceDetail.direction
            )

            db.collection("servicios").add(data)
                .addOnSuccessListener { document ->
                    Log.d("ConfirmarServicio", "ID: ${document.id}")
                    confirmar.onClick(view)
                }
                .addOnFailureListener { e ->
                    Log.w("ConfirmarservicioFragment", "Error in service confirmation", e)
                    Toast.makeText(activity, "No se pudo completar el servicio", Toast.LENGTH_SHORT).show()
                }


        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etxtDestinoConfi.setText(serviceDetail.direction)
        etxtTrabajoConfi.setText(serviceDetail.service)
    }
}