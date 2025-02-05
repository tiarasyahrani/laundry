package com.nilam.laundry

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.nilam.laundry.pelanggan.TambahPelanggan

class Data_Pegawai : AppCompatActivity() {
    lateinit var bt_data_pegawai_tambah : FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_data_pegawai)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        bt_data_pegawai_tambah = findViewById(R.id.bt_data_pegawai_tambah)

        bt_data_pegawai_tambah.setOnClickListener{
            val intent = Intent(this, TambahPegawai::class.java)
            startActivity(intent)
        }
        }
    }
