package com.nilam.laundry

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Account : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        val sharedPref = getSharedPreferences("USER_DATA", MODE_PRIVATE)
        val nama = sharedPref.getString("nama", "-")
        val nohp = sharedPref.getString("no_hp", "-")
        val password = sharedPref.getString("password", "-")
        val bio = "Team Laundry Tiaya"

        findViewById<TextView>(R.id.tvNamaPengguna).text = nama
        findViewById<TextView>(R.id.tvNamaPenggunaProfil).text = nama
        findViewById<TextView>(R.id.tvNoHPPenggunaProfil).text = nohp
        findViewById<TextView>(R.id.tvPwPenggunaProfil).text = password
        findViewById<TextView>(R.id.tvBioPenggunaProfil).text = bio
    }

}