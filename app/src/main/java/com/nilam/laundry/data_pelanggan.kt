package com.nilam.laundry

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.nilam.laundry.pelanggan.TambahPelanggan

class data_pelanggan : AppCompatActivity() {
    lateinit var bt_data_pelanggan_tambah : FloatingActionButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_data_pelanggan)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        bt_data_pelanggan_tambah = findViewById(R.id.bt_data_pelanggan_tambah)

        bt_data_pelanggan_tambah.setOnClickListener{
            val intent = Intent(this, TambahPelanggan::class.java)
            startActivity(intent)
        }
    }
}