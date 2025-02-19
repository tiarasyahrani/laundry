package com.nilam.laundry

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.FirebaseDatabase
import com.nilam.laundry.modeldata.modellayanan

class Tambah_Layanan : AppCompatActivity() {
    lateinit var tvJudul_layanan: TextView
    lateinit var etNama_layanan: EditText
    lateinit var etHarga_layanan: EditText
    lateinit var et_Tambah_Cabang_layanan: EditText
    lateinit var buttonSimpan_layanan: Button
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("layanan")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tambah_layanan)

        init()
        buttonSimpan_layanan.setOnClickListener{
            cek_validasi()
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun init(){
        tvJudul_layanan = findViewById(R.id.tvJudul_layanan)
        etNama_layanan = findViewById(R.id.etNama_layanan)
        etHarga_layanan = findViewById(R.id.etHarga_layanan)
        et_Tambah_Cabang_layanan = findViewById(R.id.et_Tambah_Cabang_layanan)
        buttonSimpan_layanan = findViewById(R.id.buttonSimpan_layanan)

    }

    fun cek_validasi() {
        val namalayanan = etNama_layanan.text.toString()
        val harga = etHarga_layanan.text.toString()
        val cabanglayanan = et_Tambah_Cabang_layanan.text.toString()

        if (namalayanan.isEmpty()) {
            etNama_layanan.error = getString(R.string.validasi_nama_layanan)
            Toast.makeText(this, getString(R.string.validasi_nama_layanan), Toast.LENGTH_SHORT).show()
            etNama_layanan.requestFocus()
            return
        }
        if (harga.isEmpty()) {
            etHarga_layanan.error = getString(R.string.validasi_harga_layanan)
            Toast.makeText(this, getString(R.string.validasi_harga_layanan), Toast.LENGTH_SHORT).show()
            etHarga_layanan.requestFocus()
            return
        }

        if (cabanglayanan.isEmpty()) {
            et_Tambah_Cabang_layanan.error = getString(R.string.validasi_cabang_layanan)
            Toast.makeText(this, getString(R.string.validasi_cabang_layanan), Toast.LENGTH_SHORT).show()
            et_Tambah_Cabang_layanan.requestFocus()
            return
        }

        simpan()
    }


    fun simpan() {
        val layananBaru = myRef.push()
        val layananId = layananBaru.key
        val data = modellayanan(
            layananId.toString(),
            etNama_layanan.text.toString(),
            etHarga_layanan.text.toString(),
            et_Tambah_Cabang_layanan.text.toString(),

            "timestamp"
        )
        layananBaru.setValue(data)
            .addOnSuccessListener {
                Toast.makeText(this, getString(R.string.sukses_simpan_layanan), Toast.LENGTH_SHORT).show()

                val resultIntent = Intent()
                resultIntent.putExtra("layanan_id",layananId)
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this,getString(R.string.gagal_simpan_layanan), Toast.LENGTH_SHORT).show()
            }
    }
}