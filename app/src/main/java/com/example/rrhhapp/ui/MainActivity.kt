package com.example.rrhhapp.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.rrhhapp.R
import com.example.rrhhapp.io.ApiService
import com.example.rrhhapp.io.response.LoginResponse
import com.example.rrhhapp.model.User
import com.example.rrhhapp.util.MyCustomPrefs
import com.example.rrhhapp.util.PreferenceHelper
import com.example.rrhhapp.util.PreferenceHelper.set
import retrofit2.Call
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private val apiService: ApiService by lazy {
        ApiService.create()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnGoMenu = findViewById<Button>(R.id.btn_login)

        if (MyCustomPrefs.getIsUserLogged(this)){
            goToMenu()
        }

        btnGoMenu.setOnClickListener {
            performLogin()
        }

    }


    private fun goToForgetPassword(){
        val intent = Intent(this, ForgetUserOrPassword::class.java)
        startActivity(intent)
    }

    private fun goToMenu(){
        val etEmail = findViewById<EditText>(R.id.et_email).text.toString()
        val etPassword = findViewById<EditText>(R.id.et_password).text.toString()

        val intent = Intent(this,  MenuActivity::class.java)
        intent.putExtra("user", etEmail)
        intent.putExtra("password", etPassword)
        startActivity(intent)
        finish()
    }


    private fun createSessionPreference(jwt: String){
        val preferences = PreferenceHelper.defaultPrefs(this)
        preferences["jwt"]= jwt
    }

    private fun performLogin(){
        val etEmail = findViewById<EditText>(R.id.et_email).text.toString()
        val etPassword = findViewById<EditText>(R.id.et_password).text.toString()

        val user = User (username = etEmail, password = etPassword)

        val call = apiService.postLogin(user)

        call.enqueue(object : retrofit2.Callback<LoginResponse>{
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if(response.isSuccessful){
                    val loginResponse = response.body()

                    if (loginResponse == null){
                        Toast.makeText(applicationContext, "Se produjo un error en el servidor", Toast.LENGTH_SHORT).show()
                        return
                    }
                    if(loginResponse.access_token != null){
                        MyCustomPrefs.setJwt(loginResponse.access_token, this@MainActivity)
                        MyCustomPrefs.setTenant(loginResponse.tenant, this@MainActivity)
                        MyCustomPrefs.setIsUserLogged(true, this@MainActivity)
                        goToMenu()
                    }else{
                        Toast.makeText(applicationContext, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(applicationContext, "Se produjo un error en el servidor", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(applicationContext, "Se produjo un error en el servidor", Toast.LENGTH_SHORT).show()
            }
        })
    }

}