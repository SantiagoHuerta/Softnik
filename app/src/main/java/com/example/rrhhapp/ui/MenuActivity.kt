package com.example.rrhhapp.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import android.widget.Toast
import com.example.rrhhapp.R
import com.example.rrhhapp.util.PreferenceHelper
import com.example.rrhhapp.util.PreferenceHelper.set

class MenuActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val etWelcome = findViewById<TextView>(R.id.tv_welcome)


        val bundle = intent.extras
        val user = bundle?.getString("user")
        etWelcome.text = "Bienvenido  $user"

        val btnLogout = findViewById<TextView>(R.id.btn_logout)
        btnLogout.setOnClickListener {
            goToLogin()
        }
    }

    private fun goToLogin(){
        createSessionPreference("")
        val intent = Intent(this,  MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun createSessionPreference(jwt: String){
        val preferences = PreferenceHelper.defaultPrefs(this)
        preferences["jwt"]= jwt
    }

    /*
    private fun getLocation(){
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this, "Permisos de ubicacion concedidos", Toast.LENGTH_SHORT).show()
        }else{
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ), 1
            )
        }
    }*/
}