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
import com.google.firebase.ktx.Firebase
import com.pds.chambitas.MenuActivity
import com.pds.chambitas.R
import com.pds.chambitas.util.Constants
import com.pds.chambitas.util.ForegroundLocationService
import kotlinx.android.synthetic.main.activity_ingresa_usuario.*

class IngresaUsuarioActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ingresa_usuario)
        auth = Firebase.auth
        startUI()
    }

    private fun startUI() {
        btnBack.setOnClickListener {
            super.onBackPressed()
        }

        btnSiguienteInUs.setOnClickListener {
            if (etxtIngresaUsuario.text.isEmpty() || etxtIngresaContrasenia2.text.isEmpty()) {
                Toast.makeText(this, "Completar los campos requeridos", Toast.LENGTH_SHORT).show()
            } else {
                login(etxtIngresaUsuario.text.toString(), etxtIngresaContrasenia2.text.toString())
            }
        }
    }

    private fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                startActivity(Intent(this, MenuActivity::class.java))
                startLocationService()
                finish()
            } else {
                Log.e("Error al inicar sesion", task.exception.toString())
                registerUser(email, password)
            }
        }
    }

    private fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                startActivity(Intent(this, RegistroActivity::class.java))
                finish()
            } else {
                Log.e("RegistroUser", "Error al registrar el usuario", task.exception)
                Toast.makeText(this, "No se pudo registrar el usuario", Toast.LENGTH_SHORT).show()
            }
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