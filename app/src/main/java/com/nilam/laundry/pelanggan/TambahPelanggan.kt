package com.nilam.laundry.pelanggan

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
import com.nilam.laundry.R
import com.google.firebase.database.FirebaseDatabase
import com.nilam.laundry.modeldata.modelpelanggan


class TambahPelanggan : AppCompatActivity() {
    lateinit var tvJudul_pelanggan: TextView
    lateinit var etNamaLengkap_pelanggan: EditText
    lateinit var etAlamat_pelanggan: EditText
    lateinit var etNoHp_pelanggan: EditText
    lateinit var etTerdaftar_pelanggan: EditText
    lateinit var buttonSimpan_pelanggan: Button
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("pelanggan")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tambah_pelanggan)

        init()
        buttonSimpan_pelanggan.setOnClickListener {
            cekValidasi()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets


        }

    }


    fun init() {
        tvJudul_pelanggan = findViewById(R.id.tvJudul_pelanggan)
        etNamaLengkap_pelanggan = findViewById(R.id.etNamaLengkap_pelanggan)
        etAlamat_pelanggan = findViewById(R.id.etAlamat_pelanggan)
        etNoHp_pelanggan = findViewById(R.id.etNoHp_pelanggan)
        etTerdaftar_pelanggan = findViewById(R.id.etTerdaftar_pelanggan)
        buttonSimpan_pelanggan = findViewById(R.id.buttonSimpan_pelanggan)
    }

    fun cekValidasi() {
        val namalengkap = etNamaLengkap_pelanggan.text.toString()
        val noHP = etNoHp_pelanggan.text.toString()
        val terdaftar = etTerdaftar_pelanggan.text.toString()
        val alamat = etAlamat_pelanggan.text.toString()

        if (namalengkap.isEmpty()) {
            etNamaLengkap_pelanggan.error = getString(R.string.validasi_nama_pelanggan)
            Toast.makeText(this,getString(R.string.validasi_nama_pelanggan), Toast.LENGTH_SHORT).show()
            etNamaLengkap_pelanggan.requestFocus()
            return
        }

        if (alamat.isEmpty()) {
            etAlamat_pelanggan.error = getString(R.string.validasi_alamat_pelanggan)
            Toast.makeText(this,getString(R.string.validasi_alamat_pelanggan), Toast.LENGTH_SHORT).show()
            etAlamat_pelanggan.requestFocus()
            return
        }
        if (noHP.isEmpty()) {
            etNoHp_pelanggan.error = getString(R.string.validasi_noHP_pelanggan)
            Toast.makeText(this, getString(R.string.validasi_noHP_pelanggan), Toast.LENGTH_SHORT).show()
            etNoHp_pelanggan.requestFocus()
            return
        }
        if (terdaftar.isEmpty()) {
            etTerdaftar_pelanggan.error = getString(R.string.validasi_terdaftar_pelanggan)
            Toast.makeText(this,getString(R.string.validasi_terdaftar_pelanggan), Toast.LENGTH_SHORT).show()
            etTerdaftar_pelanggan.requestFocus()
            return
        }
        simpan()
    }


    fun simpan() {
        val pelangganBaru = myRef.push()
        val pelangganId = pelangganBaru.key
        val data = modelpelanggan(
            pelangganId.toString(),
            etNamaLengkap_pelanggan.text.toString(),
            etNoHp_pelanggan.text.toString(),
            etTerdaftar_pelanggan.text.toString(),
            etAlamat_pelanggan.text.toString(),
//            "timestamp"
        )
        pelangganBaru.setValue(data)
            .addOnSuccessListener {
                Toast.makeText(this, getString(R.string.tambah_pelanggan_sukses), Toast.LENGTH_SHORT).show()

                val resultIntent = Intent()
                resultIntent.putExtra("pelanggan_id",pelangganId)
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this,getString(R.string.tambah_pelanggan_gagal), Toast.LENGTH_SHORT).show()
            }
    }



}