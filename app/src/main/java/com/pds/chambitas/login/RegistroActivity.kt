package com.pds.chambitas.login

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.pds.chambitas.MenuActivity
import com.pds.chambitas.R
import com.pds.chambitas.util.Constants
import com.pds.chambitas.util.ForegroundLocationService
import kotlinx.android.synthetic.main.activity_registro.*

class RegistroActivity : AppCompatActivity() {

    var db: FirebaseFirestore = Firebase.firestore
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        auth = Firebase.auth
        initUI()

    }

    private fun initUI() {
        btnBack.setOnClickListener {
            super.onBackPressed()
        }

        btnSiguienteRegistro.setOnClickListener {
            if (etxtIngresaTelefono.text.isEmpty() ||
                etxtIngresaNombre.text.isEmpty()) {
                Toast.makeText(this, "Completar todos los campos requeridos", Toast.LENGTH_SHORT).show()
            } else {
                registerUser()
            }
        }
    }

    private fun registerUser() {
        val phone = etxtIngresaTelefono.text.toString()
        val name = etxtIngresaNombre.text.toString()
        val type = "regular"

        val user = auth.currentUser
        user?.let {
            val uid = user.uid
            val email = user.email
            val data = hashMapOf(
                "email" to email,
                "name" to name,
                "phone" to phone,
                "type" to type
            )
            db.collection("usuarios").document(uid).set(data)
                .addOnCompleteListener {
                    Log.d("RegistroUser", "Perfil del usuario ingresado correctamente")
                }
                .addOnFailureListener { e ->
                    Log.e("RegistroUser", "Error al registrar la informacion del usuario", e)
                }
            startActivity(Intent(this, MenuActivity::class.java))
            startLocationService()
            finish()
        }
    }

    private fun isLocationServiceRunning(): Boolean {
        val activityManager: ActivityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        if (activityManager != null) {
            for (service in activityManager.getRunningServices(Integer.MAX_VALUE)) {
                if (ForegroundLocationService::class.java.name.equals(service.service.className)) {
                    if (service.foreground) {
                        return true
                    }
                }
            }
            return false
        }
        return false
    }

    private fun startLocationService() {
        if (!isLocationServiceRunning()){
            val intent = Intent(applicationContext, ForegroundLocationService::class.java)
            intent.action = Constants.ACTION_START_LOCATION_SERVICE
            startService(intent)
        }
    }
}