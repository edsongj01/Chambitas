package com.pds.chambitas.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.pds.chambitas.MenuActivity
import com.pds.chambitas.R
import com.pds.chambitas.util.LocationService
import kotlinx.android.synthetic.main.activity_contrasenia_usuario.*

class ContraseniaUsuarioActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contrasenia_usuario)

        btnSiguienteInCon.setOnClickListener {
            startActivity(Intent(this,MenuActivity::class.java))
            startService(Intent(this, LocationService::class.java))
        }

        btnBack.setOnClickListener {
            super.onBackPressed()
        }

    }
}