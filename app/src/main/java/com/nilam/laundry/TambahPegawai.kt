package com.nilam.laundry

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
import com.nilam.laundry.modeldata.modelpegawai
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.nilam.laundry.TambahPegawai

class TambahPegawai : AppCompatActivity() {
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("pegawai")
    lateinit var tvJudul_pegawai: TextView
    lateinit var etNamaLengkap_pegawai: EditText
    lateinit var etAlamat_pegawai: EditText
    lateinit var etNoHp_pegawai: EditText
    lateinit var etCabang_pegawai: EditText
    lateinit var buttonSimpan_pegawai: Button

    var isEdit = false

    var id_pegawai:String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tambah_pegawai)

        init()
        getData()
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
    fun getData(){
        id_pegawai = intent.getStringExtra("id") ?: ""
        Log.d("DEBUG", "id_pegawai: $id_pegawai")

        if(id_pegawai.isNotEmpty()){
            isEdit = true
            tvJudul_pegawai.text = "Edit Pegawai"
            buttonSimpan_pegawai.text = "Edit"
            hidup()
            database.getReference("pegawai").child(id_pegawai).get()
                .addOnSuccessListener { snapshot ->
                    val data = snapshot.getValue(modelpegawai::class.java)
                    if(data != null){
                        etNamaLengkap_pegawai.setText(data.namaPegawai)
                        etAlamat_pegawai.setText(data.alamatPegawai)
                        etNoHp_pegawai.setText(data.noHPPegawai)
                        etCabang_pegawai.setText(data.cabangPegawai)
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Gagal memuat data pegawai", Toast.LENGTH_SHORT).show()
                }
        }else {
            isEdit = false
            tvJudul_pegawai.text = "Tambah Pegawai"
            buttonSimpan_pegawai.text = "Simpan"
            hidup()
            etNamaLengkap_pegawai .requestFocus()
        }
    }

    fun hidup(){
        etNamaLengkap_pegawai.isEnabled=true
        etAlamat_pegawai.isEnabled=true
        etNoHp_pegawai.isEnabled=true
        etCabang_pegawai.isEnabled=true
    }
    fun update(){
        val pegawaiRef = database.getReference("pegawai").child(id_pegawai)
        val updateData = mutableMapOf<String, Any>()
        updateData["namaPegawai"]= etNamaLengkap_pegawai.text.toString()
        updateData["alamatPegawai"]= etAlamat_pegawai.text.toString()
        updateData["noHPPegawai"]= etNoHp_pegawai.text.toString()
        updateData["cabangPegawai"]= etCabang_pegawai.text.toString()
        pegawaiRef.updateChildren(updateData).addOnSuccessListener {
            Toast.makeText(this@TambahPegawai, "Data Pegawai Berhasil Diperbarui",Toast.LENGTH_SHORT).show()
            finish()
        }.addOnFailureListener{
            Toast.makeText(this@TambahPegawai, "Data Pegawai Gagal Diperbarui",Toast.LENGTH_SHORT).show()
        }
    }


    fun cek_validasi() {
        val nama = etNamaLengkap_pegawai.text.toString()
        val alamat = etAlamat_pegawai.text.toString()
        val nohp = etNoHp_pegawai.text.toString()
        val cabang = etCabang_pegawai.text.toString()
        val terdaftar = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())



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
        if (buttonSimpan_pegawai.text.equals("Simpan")) {
            simpan(terdaftar)
        }else if(buttonSimpan_pegawai.text.equals("Edit")){
            hidup()
            etNamaLengkap_pegawai.requestFocus()
            buttonSimpan_pegawai.text="Perbarui"
        }else if (buttonSimpan_pegawai.text.equals("Perbarui")) {
            update()
        }


    }


    fun simpan(terdaftar: String) {
        val pegawaiBaru = myRef.push()
        val pegawaiId = pegawaiBaru.key ?: return
        val data = modelpegawai(
            id_pegawai = pegawaiId,
            namaPegawai = etNamaLengkap_pegawai.text.toString(),
            alamatPegawai = etAlamat_pegawai.text.toString(),
            noHPPegawai = etNoHp_pegawai.text.toString(),
            terdaftarPegawai = terdaftar,
            cabangPegawai = etCabang_pegawai.text.toString()
        )

        pegawaiBaru.setValue(data)
            .addOnSuccessListener {
                Toast.makeText(this, "Pegawai berhasil disimpan", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener{
                Toast.makeText(this, "Gagal menyimpan pegawai", Toast.LENGTH_SHORT).show()
            }
    }


}