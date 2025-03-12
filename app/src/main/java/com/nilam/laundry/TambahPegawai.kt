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
import com.nilam.laundry.modeldata.modelpegawai
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TambahPegawai : AppCompatActivity() {
    lateinit var tvJudul_pegawai: TextView
    lateinit var etNamaLengkap_pegawai: EditText
    lateinit var etAlamat_pegawai: EditText
    lateinit var etNoHp_pegawai: EditText
    lateinit var etCabang_pegawai: EditText
    lateinit var buttonSimpan_pegawai: Button
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("pegawai")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tambah_pegawai)

        init()
        buttonSimpan_pegawai.setOnClickListener{
            cek_validasi()
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun init(){
        tvJudul_pegawai = findViewById(R.id.tvJudul_pegawai)
        etNamaLengkap_pegawai = findViewById(R.id.etNamaLengkap_pegawai)
        etAlamat_pegawai = findViewById(R.id.etAlamat_pegawai)
        etNoHp_pegawai = findViewById(R.id.etNoHp_pegawai)
        etCabang_pegawai = findViewById(R.id.etCabang_pegawai)
        buttonSimpan_pegawai = findViewById(R.id.buttonSimpan_pegawai)

    }

    fun cek_validasi() {
        val nama = etNamaLengkap_pegawai.text.toString()
        val alamat = etAlamat_pegawai.text.toString()
        val nohp = etNoHp_pegawai.text.toString()
        val cabang = etCabang_pegawai.text.toString()
        val register = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())


        if (nama.isEmpty()) {
            etNamaLengkap_pegawai.error = getString(R.string.validasi_nama_pegawai)
            Toast.makeText(this, getString(R.string.validasi_nama_pegawai), Toast.LENGTH_SHORT).show()
            etNamaLengkap_pegawai.requestFocus()
            return
        }
        if (alamat.isEmpty()) {
            etAlamat_pegawai.error = getString(R.string.validasi_alamat_pegawai)
            Toast.makeText(this, getString(R.string.validasi_alamat_pegawai), Toast.LENGTH_SHORT).show()
            etAlamat_pegawai.requestFocus()
            return
        }

        if (!nohp.matches(Regex("^[0-9]+$"))) {
            etNoHp_pegawai.error = "Nomor HP harus berupa angka"
            Toast.makeText(this, "Nomor HP harus berupa angka", Toast.LENGTH_SHORT).show()
            etNoHp_pegawai.requestFocus()
            return
        }


        if (cabang.isEmpty()) {
            etCabang_pegawai.error = getString(R.string.validasi_namacabang_pegawai)
            Toast.makeText(this, getString(R.string.validasi_namacabang_pegawai), Toast.LENGTH_SHORT).show()
            etCabang_pegawai.requestFocus()
            return
        }
        simpan(register)
    }


    fun simpan(register: String) {
        val pegawaiBaru = myRef.push()
        val pegawaiId = pegawaiBaru.key
        val currenTime = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())


        val data = modelpegawai(
            pegawaiId.toString(),
            etNamaLengkap_pegawai.text.toString(),
            etAlamat_pegawai.text.toString(),
            etNoHp_pegawai.text.toString(),
            currenTime,
            etCabang_pegawai.text.toString()
        )

        pegawaiBaru.setValue(data)
            .addOnSuccessListener {
                Toast.makeText(this, getString(R.string.sukses_simpan_pegawai), Toast.LENGTH_SHORT).show()

                val resultIntent = Intent()
                resultIntent.putExtra("pegawai_id",pegawaiId)
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this,getString(R.string.gagal_simpan_pegawai), Toast.LENGTH_SHORT).show()
            }
    }
}