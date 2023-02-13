package com.example.rrhhapp.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.rrhhapp.R
import com.example.rrhhapp.pojo.LocationPojo
import com.example.rrhhapp.util.PreferenceHelper
import com.example.rrhhapp.util.PreferenceHelper.get
import com.example.rrhhapp.util.PreferenceHelper.set
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.time.Instant

class MenuActivity : AppCompatActivity() {

    private lateinit var location: FusedLocationProviderClient
    private lateinit var database: FirebaseDatabase
    private lateinit var refLocation: DatabaseReference
    private var preferencesTenant = ""
    private lateinit var sharedPreferences: SharedPreferences

    @SuppressLint("SetTextI18n", "ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        sharedPreferences = getSharedPreferences("infoTenant", Context.MODE_PRIVATE)
        val tenant = PreferenceHelper.customPrefs(this, "infoTenant").getString("tenant", "")
        preferencesTenant = tenant.toString()
        Toast.makeText(this, preferencesTenant, Toast.LENGTH_SHORT).show()

        database = FirebaseDatabase.getInstance()
        refLocation = database.getReference("location")

        val etWelcome = findViewById<TextView>(R.id.tv_welcome)
        val btnStartJourney = findViewById<TextView>(R.id.btn_start_journey)
        val btnEndJourney = findViewById<TextView>(R.id.btn_end_journey)
        val bundle = intent.extras
        val btnLogout = findViewById<TextView>(R.id.btn_logout)

        btnLogout.setOnClickListener {
            goToLogin()
        }

        btnStartJourney.setOnClickListener {
            getLocation()
           // btnStartJourney.isEnabled = false
            btnEndJourney.isEnabled = true
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


    private fun getLocation(){
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this, "Permisos de ubicacion concedidos", Toast.LENGTH_SHORT).show()
        }else{
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ), 1)}

        location = LocationServices.getFusedLocationProviderClient(this)

        location.lastLocation.addOnSuccessListener(this) { location ->
            if (location != null) {
                val latitud = location.latitude
                val longuitud = location.longitude
                var locationToDatabase = LocationPojo(preferencesTenant, latitud, longuitud, Instant.now().toString(),"Entry" )
                refLocation.push().setValue(locationToDatabase)

                Toast.makeText(
                    this@MenuActivity, "Informacion enviada" , Toast.LENGTH_LONG).show()
            }
        }
    }

}
