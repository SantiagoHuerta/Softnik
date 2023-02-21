package com.example.rrhhapp.ui

import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import butterknife.*
import com.example.rrhhapp.R


open class PopUpConfirmSendLocation : AppCompatActivity() {

    private var unbinder: Unbinder? = null

    @BindView(R.id.btn_confirm)
    var btnAccept: Button? = null

    @BindView(R.id.btn_back)
    var btnCancel: Button? = null

    @BindView(R.id.btn_start_journey)
    var btnStartJourney: Button? = null

    @BindView(R.id.btn_end_journey)
    var btnEndJourney: Button? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pop_up_confirm_send_location)
        unbinder = ButterKnife.bind(this)

        val windowSize = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(windowSize)

        val width = windowSize.widthPixels
        val height = windowSize.heightPixels

        window.setLayout((width * 0.8).toInt(), (height * 0.4).toInt())

        btnAccept?.setOnClickListener {

        }
    }

    @OnClick(R.id.btn_confirm)
     fun goToGetLocation(view: View) {
        val intent = Intent(this,  MenuActivity::class.java)
        intent.putExtra("confirm", true)
        startActivity(intent)
        finish()
    }

    @OnClick(R.id.btn_back)
    fun closePopUp(view: View) {
        finish()
    }
}