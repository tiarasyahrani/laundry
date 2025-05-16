package com.nilam.laundry.pelanggan

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.FirebaseDatabase
import com.nilam.laundry.R
import com.nilam.laundry.modeldata.modelpelanggan
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TambahPelanggan : AppCompatActivity() {
    lateinit var tvJudul_pelanggan: TextView
    lateinit var etNamaLengkap_pelanggan: EditText
    lateinit var etAlamat_pelanggan: EditText
    lateinit var etNoHp_pelanggan: EditText
    lateinit var buttonSimpan_pelanggan: Button
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("pelanggan")

    var isEdit = false
    var id_pelanggan:String=""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tambah_pelanggan)

        init()
        getData()
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
        buttonSimpan_pelanggan = findViewById(R.id.buttonSimpan_pelanggan)
    }
    fun getData(){
        id_pelanggan = intent.getStringExtra("id") ?: ""
        Log.d("DEBUG", "idPelanggan: $id_pelanggan")

        if(id_pelanggan.isNotEmpty()){
            isEdit = true
            tvJudul_pelanggan.text = "Edit Pelanggan"
            buttonSimpan_pelanggan.text = "Edit"
            hidup()
            database.getReference("pelanggan").child(id_pelanggan).get()
                .addOnSuccessListener { snapshot ->
                    val data = snapshot.getValue(modelpelanggan::class.java)
                    if(data != null){
                        etNamaLengkap_pelanggan.setText(data.tv_nama_pelanggan)
                        etAlamat_pelanggan.setText(data.tv_alamat_pelanggan)
                        etNoHp_pelanggan.setText(data.tv_nohp_pelanggan)
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Gagal memuat data pelanggan", Toast.LENGTH_SHORT).show()
                }
        }else {
            isEdit = false
            tvJudul_pelanggan.text = "Tambah Pelanggan"
            buttonSimpan_pelanggan.text = "Simpan"
            hidup()
            etNamaLengkap_pelanggan.requestFocus()
        }
    }
    fun hidup(){
        etNamaLengkap_pelanggan.isEnabled=true
        etAlamat_pelanggan.isEnabled=true
        etNoHp_pelanggan.isEnabled=true
    }
    fun update(){
        val pegawaiRef = database.getReference("pelanggan").child(id_pelanggan)
        val updateData = mutableMapOf<String, Any>()
        updateData["tv_nama_pelanggan"]= etNamaLengkap_pelanggan.text.toString()
        updateData["tv_alamat_pelanggan"]= etAlamat_pelanggan.text.toString()
        updateData["tv_nohp_pelanggan"]= etNoHp_pelanggan.text.toString()
        pegawaiRef.updateChildren(updateData).addOnSuccessListener {
            Toast.makeText(this@TambahPelanggan, "Data Pelanggan Berhasil Diperbarui",Toast.LENGTH_SHORT).show()
            finish()
        }.addOnFailureListener{
            Toast.makeText(this@TambahPelanggan, "Data Pelanggan Gagal Diperbarui",Toast.LENGTH_SHORT).show()
        }
    }

    fun cekValidasi() {
        val namalengkap = etNamaLengkap_pelanggan.text.toString()
        val noHP = etNoHp_pelanggan.text.toString()
        val alamat = etAlamat_pelanggan.text.toString()
        val terdaftar = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())


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
        if (!noHP.matches(Regex("^[0-9]+$"))) {
            etNoHp_pelanggan.error = "Nomor HP harus berupa angka"
            Toast.makeText(this, "Nomor HP harus berupa angka", Toast.LENGTH_SHORT).show()
            etNoHp_pelanggan.requestFocus()
            return
        }
        if (buttonSimpan_pelanggan.text.equals("Simpan")) {
            simpan(terdaftar)
        }else if(buttonSimpan_pelanggan.text.equals("Edit")){
            hidup()
            etNamaLengkap_pelanggan.requestFocus()
            buttonSimpan_pelanggan.text="Perbarui"
        }else if (buttonSimpan_pelanggan.text.equals("Perbarui")) {
            update()
        }

    }

    fun simpan(terdaftar: String) {
        val pelangganBaru = myRef.push()
        val pelangganId = pelangganBaru.key ?: return
        val data = modelpelanggan(
            id_pelanggan = pelangganId,
            tv_nama_pelanggan = etNamaLengkap_pelanggan.text.toString(),
            tv_alamat_pelanggan = etAlamat_pelanggan.text.toString(),
            tv_nohp_pelanggan = etNoHp_pelanggan.text.toString(),
            tv_terdaftar_pelanggan = terdaftar,
        )

        pelangganBaru.setValue(data)
            .addOnSuccessListener {
                Toast.makeText(this, getString(R.string.tambah_pelanggan_sukses), Toast.LENGTH_SHORT).show()

                val resultIntent = Intent()
                resultIntent.putExtra("idPelanggan",pelangganId)
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this,getString(R.string.tambah_pelanggan_gagal), Toast.LENGTH_SHORT).show()
            }
    }



}