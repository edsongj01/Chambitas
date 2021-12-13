package com.pds.chambitas.ui.serviciossolicitados

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.pds.chambitas.R
import com.pds.chambitas.ui.serviciossolicitados.historial_util.AdapterHistorial
import com.pds.chambitas.ui.serviciossolicitados.historial_util.HistorialModel
import com.pds.chambitas.util.Constants
import kotlinx.android.synthetic.main.fragment_serviciossolicitados.*
import kotlinx.android.synthetic.main.fragment_serviciossolicitados.view.*
import java.text.DateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ServiciossolicitadosFragment : Fragment() {

    private var auth: FirebaseAuth = Firebase.auth
    private var db = Firebase.firestore

    private var userList = ArrayList<HashMap<String, String>>()

    private lateinit var historialAdapter: AdapterHistorial

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        historialAdapter = AdapterHistorial(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root:View = inflater.inflate(R.layout.fragment_serviciossolicitados, container, false)

        val user = auth.currentUser

        root.serviciosSolicitadosList.adapter = historialAdapter

        db.collection("usuarios")
            .whereEqualTo("type","prestador")
            .get()
            .addOnSuccessListener { usuarios ->
                if (usuarios != null && !usuarios.isEmpty) {
                    usuarios.documents.forEach { usuario ->
                        userList.add(hashMapOf(
                            "id" to usuario.id,
                            "name" to usuario.data!!["name"].toString()
                        ))
                    }
                }
            }

        db.collection("servicios")
            .whereEqualTo("user_regular", user!!.uid)
            .whereEqualTo("servicioActivo", false)
            .whereIn("estado", listOf(Constants.SERVICE_FINALIZADO, Constants.SERVICE_CANCELADO))
            //.orderBy("fecha_servicio", Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { list ->
                if (list != null && !list.isEmpty) {
                    list.documents.forEach { obj ->
                        Log.d("Historial", "Id de servicio: ${obj.id} - ${obj.data!!["service"]} - ${obj.data!!["user_prestador"]}")
                        userList.forEach { u ->
                            if (obj.data!!["user_prestador"] != null) {
                                if (u["id"] == obj.data!!["user_prestador"].toString()) {
                                    Log.d("Historial", "Nombre del prestador: ${u["name"]}")

                                    val timestamp = obj.data!!["fecha_servicio"] as Timestamp
                                    val fecha = Date(timestamp.seconds * 1000)
                                    val dateFormat = android.text.format.DateFormat.format("yyyy-MM-dd", fecha)

                                    historialAdapter.addElemento(
                                        HistorialModel(
                                            obj.data!!["service"].toString(),
                                            dateFormat.toString(),
                                            obj.data!!["estado"].toString(),
                                            obj.data!!["direction"].toString(),
                                            u["name"].toString()
                                        )
                                    )
                                }
                            }
                        }
                    }
                    historialAdapter.notifyDataSetChanged()
                }
            }
            .addOnFailureListener {
                Log.d("Historial", "Hubo un error al traer el historial", it)
            }

        return root
    }
}