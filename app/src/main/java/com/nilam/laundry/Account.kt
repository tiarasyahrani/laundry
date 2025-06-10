package com.nilam.laundry

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Account : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        tampilkanData()

        // Tombol Logout
        val sharedPref = getSharedPreferences("USER_DATA", MODE_PRIVATE)
        val btnLogout = findViewById<TextView>(R.id.tvLogout)
        btnLogout.setOnClickListener {
            val dialog = androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle(getString(R.string.toast_KonfirmasiLogout))
                .setMessage(getString(R.string.ApakahAndayakininginkeluar))
                .setPositiveButton(getString(R.string.Ya)) { _, _ ->
                    sharedPref.edit().clear().apply()
                    startActivity(Intent(this, login::class.java))
                    finish()
                }
                .setNegativeButton(getString(R.string.Batal), null)
                .create()

            dialog.show()
        }

        // Pindah ke halaman edit profil
        val tvSuntingProfil = findViewById<TextView>(R.id.tvSuntingProfil)
        tvSuntingProfil.setOnClickListener {
            val intent = Intent(this, edit_profile::class.java)
            startActivity(intent)
        }

        val tvchange_password = findViewById<TextView>(R.id.tvchange_password)
        tvchange_password.setOnClickListener {
            val intent = Intent(this, change_password::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        tampilkanData()
    }

    private fun tampilkanData() {
        val sharedPref = getSharedPreferences("USER_DATA", MODE_PRIVATE)
        val nama = sharedPref.getString("nama", "-")
        val nohp = sharedPref.getString("no_hp", "-")
        val password = sharedPref.getString("password", "-")
        val bio = sharedPref.getString("bio", "Team Laundry Tiaya")

        findViewById<TextView>(R.id.tvNamaPengguna).text = nama
        findViewById<TextView>(R.id.tvNamaPenggunaProfil).text = nama
        findViewById<TextView>(R.id.tvNoHPPenggunaProfil).text = nohp
        findViewById<TextView>(R.id.tvPwPenggunaProfil).text = password
        findViewById<TextView>(R.id.tvBioPenggunaProfil).text = bio
    }
}
