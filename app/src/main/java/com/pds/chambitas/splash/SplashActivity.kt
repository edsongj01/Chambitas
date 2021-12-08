package com.pds.chambitas.splash

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.pds.chambitas.MainActivity
import com.pds.chambitas.MenuActivity
import com.pds.chambitas.R
import com.pds.chambitas.body.AceptacionservicioFragment
import com.pds.chambitas.util.Constants.Companion.ACTION_START_LOCATION_SERVICE
import com.pds.chambitas.util.Constants.Companion.SERVICE_FINALIZADO
import com.pds.chambitas.util.ForegroundLocationService

class SplashActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        launchSplash()
    }


    fun launchSplash(){
        Handler().postDelayed(Runnable{
            isSignIn()
        },5000)
    }

    private fun isSignIn() {
        auth = Firebase.auth
        val isAuth = auth.currentUser == null
        Log.d("UserAuth", "User is auth: $isAuth")
        if (isAuth) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
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
            intent.action = ACTION_START_LOCATION_SERVICE
            startService(intent)
        }
    }
}