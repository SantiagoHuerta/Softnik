package com.example.rrhhapp.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import com.example.rrhhapp.R
import com.example.rrhhapp.io.ApiService
import com.example.rrhhapp.io.response.LoginResponse
import com.example.rrhhapp.io.response.SignonResponse
import com.example.rrhhapp.model.Signon
import com.example.rrhhapp.util.MyCustomPrefs
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

class MenuActivity : AppCompatActivity() {

    private val apiService: ApiService by lazy {
        ApiService.create()
    }

    private lateinit var location: FusedLocationProviderClient
    private lateinit var database: FirebaseDatabase
    private lateinit var refLocation: DatabaseReference
    private var preferencesTenant = ""
    private var jwt = ""

    @SuppressLint("SetTextI18n", "ResourceAsColor", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val tenant = MyCustomPrefs.getTenant(this)
        preferencesTenant = tenant.toString()
        jwt = MyCustomPrefs.getJwt(this).toString()
        val etWelcomeUser = findViewById<TextView>(R.id.tv_user_name)
        val btnStartJourney = findViewById<TextView>(R.id.btn_start_journey)
        val btnEndJourney = findViewById<TextView>(R.id.btn_end_journey)
        val btnLogout = findViewById<TextView>(R.id.btn_logout)
      //database = FirebaseDatabase.getInstance()
      //refLocation = database.getReference("location")

        btnEndJourney.setBackgroundColor(Color.GRAY)

        val bundle = intent.extras
        etWelcomeUser.text = bundle?.getString("user")

        btnLogout.setOnClickListener {
            goToLogin()
        }

        btnStartJourney.setOnClickListener {

            val alert = android.app.AlertDialog.Builder(this@MenuActivity)
            alert.setMessage("Usted esta por enviar la informacion de inicio de su jornada")
                .setCancelable(true)
                .setPositiveButton(
                    "CONFIRMAR"
                ) { dialog, which ->
                    getLocation("I")
                }
                .setNegativeButton(
                    "VOLVER"
                ) { dialog, which -> dialog.cancel() }
            val title = alert.create()
            title.setTitle("Aviso")
            title.show()
        }

        btnEndJourney.setOnClickListener {

            val alert = android.app.AlertDialog.Builder(this@MenuActivity)
            alert.setMessage("Usted esta por enviar la informacion de salida de su jornada")
                .setCancelable(true)
                .setPositiveButton(
                    "CONFIRMAR"
                ) { dialog, which ->
                    getLocation("E")
                }
                .setNegativeButton(
                    "VOLVER"
                ) { dialog, which -> dialog.cancel() }
            val title = alert.create()
            title.setTitle("Aviso")
            title.show()
        }
    }

    fun updateButtonsEneable(concept: String){
        val btnStartJourney = findViewById<TextView>(R.id.btn_start_journey)
        val btnEndJourney = findViewById<TextView>(R.id.btn_end_journey)
        val tvStartDate = findViewById<TextView>(R.id.tv_start_date)
        val tvEndDate = findViewById<TextView>(R.id.tv_end_date)

        if(concept.equals("I")){
            tvEndDate.visibility = GONE
            tvStartDate.visibility = VISIBLE
            tvStartDate.text = LocalDateTime.now().toString()

            btnEndJourney.setBackgroundColor(resources.getColor(R.color.teal_700))
            btnStartJourney.setBackgroundColor(resources.getColor(R.color.gray))
            btnStartJourney.isEnabled = false
            btnEndJourney.isEnabled = true
        }
        else{
            tvStartDate.visibility = GONE
            tvEndDate.visibility = VISIBLE
            tvEndDate.text = LocalDateTime.now().toString()

            btnEndJourney.setBackgroundColor(resources.getColor(R.color.gray))
            btnStartJourney.setBackgroundColor(resources.getColor(R.color.teal_700))
            btnStartJourney.isEnabled = true
            btnEndJourney.isEnabled = false
        }

    }

    fun goToLogin(){
        MyCustomPrefs.clearPrefs(this)
        val intent = Intent(this,  MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun getLocation(concept : String){
        val date = Date()
        val format = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
        val dateFormat = format.format(date)

        if(checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED){

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

                val registry =  Signon(token = preferencesTenant,-37.99597508272, -57.54916992802159, date = dateFormat, concept)
                var call = apiService.postSignon(jwt,registry)

                call.enqueue(object : Callback<SignonResponse>{
                    override fun onResponse(call: Call<SignonResponse>, response: Response<SignonResponse>) {
                        if(response.isSuccessful){
                            Toast.makeText(this@MenuActivity, "Informacion enviada" , Toast.LENGTH_LONG).show()
                            updateButtonsEneable(concept)
                        }
                        else{
                            Toast.makeText(this@MenuActivity, "Error al enviar la informacion" , Toast.LENGTH_LONG).show()
                        }
                    }
                    override fun onFailure(call: Call<SignonResponse>, t: Throwable) {
                        Toast.makeText(this@MenuActivity, "Fallo de servidor" , Toast.LENGTH_LONG).show()
                    }
                })
              //  var locationToDatabase = LocationPojo(preferencesTenant, latitud, longuitud, DateTimeFormatter.ofPattern("dd/mm/yyyy HH:mm:ss").format(now), concept)
              //  refLocation.push().setValue(locationToDatabase)
            }
        }
    }
}
