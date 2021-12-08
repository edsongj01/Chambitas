package com.pds.chambitas.body

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.pds.chambitas.R
import com.pds.chambitas.util.Constants
import com.pds.chambitas.util.Constants.Companion.SERVICE_FINALIZADO
import com.pds.chambitas.util.Constants.Companion.SERVICE_TERMINADO
import kotlinx.android.synthetic.main.fragment_aceptacionservicio.view.*
import kotlinx.android.synthetic.main.fragment_finalizarservicio.view.*


class FinalizarservicioFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_finalizarservicio, container, false)

        root.btnFinalizarServicio.setOnClickListener { view ->

            var idService = ""
            val updates = hashMapOf<String, Any>(
                "estado" to SERVICE_FINALIZADO,
                "calificacion" to 5,
                "servicioActivo" to false
            )

            val user = auth.currentUser

            db.collection("servicios")
                .whereEqualTo("user_regular", user!!.uid)
                .whereEqualTo("servicioActivo", true)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        idService = document.id
                    }

                    db.collection("servicios").document(idService).update(updates).addOnSuccessListener {
                        val calificar = Navigation.createNavigateOnClickListener(R.id.action_finalizarservicioFragment_to_nav_home)
                        calificar.onClick(view)
                    }
                }
        }

        return root
    }

}