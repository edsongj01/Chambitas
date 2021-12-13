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
import kotlinx.android.synthetic.main.fragment_finalizarservicio.*
import kotlinx.android.synthetic.main.fragment_finalizarservicio.view.*


class FinalizarservicioFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private val db = Firebase.firestore
    private var calificacion = 0

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

        // 1 estrella
        root.imageView8.setOnClickListener {
            root.imageView8.setImageResource(R.drawable.ic_estrella_foreground)
            root.imageView9.setImageResource(R.drawable.ic_estrella_foreground_white)
            root.imageView10.setImageResource(R.drawable.ic_estrella_foreground_white)
            root.imageView11.setImageResource(R.drawable.ic_estrella_foreground_white)
            root.imageView12.setImageResource(R.drawable.ic_estrella_foreground_white)
            calificacion = 1
        }

        // 2 estrellas
        root.imageView9.setOnClickListener {
            root.imageView8.setImageResource(R.drawable.ic_estrella_foreground)
            root.imageView9.setImageResource(R.drawable.ic_estrella_foreground)
            root.imageView10.setImageResource(R.drawable.ic_estrella_foreground_white)
            root.imageView11.setImageResource(R.drawable.ic_estrella_foreground_white)
            root.imageView12.setImageResource(R.drawable.ic_estrella_foreground_white)
            calificacion = 2
        }

        // 3 estrellas
        root.imageView10.setOnClickListener {
            root.imageView8.setImageResource(R.drawable.ic_estrella_foreground)
            root.imageView9.setImageResource(R.drawable.ic_estrella_foreground)
            root.imageView10.setImageResource(R.drawable.ic_estrella_foreground)
            root.imageView11.setImageResource(R.drawable.ic_estrella_foreground_white)
            root.imageView12.setImageResource(R.drawable.ic_estrella_foreground_white)
            calificacion = 3
        }

        // 4 estrellas
        root.imageView11.setOnClickListener {
            root.imageView8.setImageResource(R.drawable.ic_estrella_foreground)
            root.imageView9.setImageResource(R.drawable.ic_estrella_foreground)
            root.imageView10.setImageResource(R.drawable.ic_estrella_foreground)
            root.imageView11.setImageResource(R.drawable.ic_estrella_foreground)
            root.imageView12.setImageResource(R.drawable.ic_estrella_foreground_white)
            calificacion = 4
        }

        // 5 estrellas
        root.imageView12.setOnClickListener {
            root.imageView8.setImageResource(R.drawable.ic_estrella_foreground)
            root.imageView9.setImageResource(R.drawable.ic_estrella_foreground)
            root.imageView10.setImageResource(R.drawable.ic_estrella_foreground)
            root.imageView11.setImageResource(R.drawable.ic_estrella_foreground)
            root.imageView12.setImageResource(R.drawable.ic_estrella_foreground)
            calificacion = 5
        }

        root.btnFinalizarServicio.setOnClickListener { view ->

            var idService = ""
            val updates = hashMapOf<String, Any>(
                "estado" to SERVICE_FINALIZADO,
                "calificacion" to calificacion,
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