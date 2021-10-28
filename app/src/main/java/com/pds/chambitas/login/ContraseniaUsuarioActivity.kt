package com.pds.chambitas.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.pds.chambitas.R
import kotlinx.android.synthetic.main.activity_contrasenia_usuario.*

class ContraseniaUsuarioActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contrasenia_usuario)

            btnBack.setOnClickListener {
                super.onBackPressed()
            }

    }
}