package com.pds.chambitas.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.pds.chambitas.R
import kotlinx.android.synthetic.main.activity_registro.*

class RegistroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        btnBack.setOnClickListener {
            super.onBackPressed()
        }

    }
}