package com.pds.chambitas.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.pds.chambitas.MenuActivity
import com.pds.chambitas.R
import kotlinx.android.synthetic.main.activity_ingresa_usuario.*

class IngresaUsuarioActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ingresa_usuario)

        btnBack.setOnClickListener {
            super.onBackPressed()
        }

        btnSiguienteInUs.setOnClickListener {
            startActivity(Intent(this, MenuActivity::class.java))
        }

        textView4.setOnClickListener {
            startActivity(Intent(this,RegistroActivity::class.java))
        }
    }
}