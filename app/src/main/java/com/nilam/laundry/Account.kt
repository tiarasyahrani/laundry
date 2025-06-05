package com.nilam.laundry

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Account : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        // Ambil data dari SharedPreferences
        val sharedPref = getSharedPreferences("USER_DATA", MODE_PRIVATE)
        val nama = sharedPref.getString("nama", "-")
        val nohp = sharedPref.getString("no_hp", "-")
        val password = sharedPref.getString("password", "-")
        val bio = "Team Laundry Tiaya"

        // Tampilkan data ke UI
        findViewById<TextView>(R.id.tvNamaPengguna).text = nama
        findViewById<TextView>(R.id.tvNamaPenggunaProfil).text = nama
        findViewById<TextView>(R.id.tvNoHPPenggunaProfil).text = nohp
        findViewById<TextView>(R.id.tvPwPenggunaProfil).text = password
        findViewById<TextView>(R.id.tvBioPenggunaProfil).text = bio

        // Tombol Logout
        val btnLogout = findViewById<Button>(R.id.btnLogout)
        btnLogout.setOnClickListener {
            val dialog = androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle(getString(R.string.toast_KonfirmasiLogout))
                .setMessage(getString(R.string.ApakahAndayakininginkeluar))
                .setPositiveButton(getString(R.string.Ya)) { _, _ ->
                    sharedPref.edit().clear().apply()
                    Toast.makeText(this, getString(R.string.toast_BerhasilLogout), Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, login::class.java))
                    finish()
                }
                .setNegativeButton(getString(R.string.Batal), null)
                .create()

            dialog.show()
        }

    }
}
