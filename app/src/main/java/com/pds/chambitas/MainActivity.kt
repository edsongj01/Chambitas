package com.pds.chambitas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.pds.chambitas.login.IngresaUsuarioActivity
import com.pds.chambitas.splash.SplashActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnIngresar.setOnClickListener {
            startActivity(Intent(this,IngresaUsuarioActivity::class.java))
        }
    }

}