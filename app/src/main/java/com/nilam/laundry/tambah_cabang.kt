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
import com.nilam.laundry.modeldata.modelcabang

class tambah_cabang : AppCompatActivity() {
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("cabang")
    lateinit var tvJudul_cabang: TextView
    lateinit var etNama_cabang: EditText
    lateinit var etAlamat_cabang: EditText
    lateinit var etLayanan_cabang: EditText
    lateinit var buttonSimpan_cabang: Button

    var isEdit = false

    var id_cabang:String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tambah_cabang)

        init()
        getData()
        buttonSimpan_cabang.setOnClickListener{
            cek_validasi()
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun init(){
        tvJudul_cabang = findViewById(R.id.tvJudul_cabang)
        etNama_cabang = findViewById(R.id.etNama_cabang)
        etAlamat_cabang = findViewById(R.id.etAlamat_cabang)
        etLayanan_cabang = findViewById(R.id.et_Tambah_layanan_cabang)
        buttonSimpan_cabang = findViewById(R.id.buttonSimpan_cabang)

    }
    fun getData(){
        id_cabang = intent.getStringExtra("id") ?: ""
        Log.d("DEBUG", "id_cabang: $id_cabang")

        if(id_cabang.isNotEmpty()){
            isEdit = true
            tvJudul_cabang.text = getString(R.string.toast_EditCabang)
            buttonSimpan_cabang.text = getString(R.string.toast_Editcabang)
            hidup()
            database.getReference("cabang").child(id_cabang).get()
                .addOnSuccessListener { snapshot ->
                    val data = snapshot.getValue(modelcabang::class.java)
                    if(data != null){
                        etNama_cabang.setText(data.namacabang)
                        etAlamat_cabang.setText(data.alamatcabang)
                        etLayanan_cabang.setText(data.layanancabang)
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, getString(R.string.toast_GagalmemuatdataCabang), Toast.LENGTH_SHORT).show()
                }
        }else {
            isEdit = false
            tvJudul_cabang.text = getString(R.string.toast_TambahCabang)
            buttonSimpan_cabang.text = getString(R.string.toast_Simpancabang)
            hidup()
            etNama_cabang .requestFocus()
        }
    }

    fun hidup(){
        etNama_cabang.isEnabled=true
        etAlamat_cabang.isEnabled=true
        etLayanan_cabang.isEnabled=true
    }
    fun update(){
        val cabangRef = database.getReference("cabang").child(id_cabang)
        val updateData = mutableMapOf<String, Any>()
        updateData["namacabang"]= etNama_cabang.text.toString()
        updateData["alamatcabang"]= etAlamat_cabang.text.toString()
        updateData["layanancabang"]= etLayanan_cabang.text.toString()
        cabangRef.updateChildren(updateData).addOnSuccessListener {
            Toast.makeText(this@tambah_cabang, getString(R.string.toast_DataCabangBerhasilDiperbarui),Toast.LENGTH_SHORT).show()
            finish()
        }.addOnFailureListener{
            Toast.makeText(this@tambah_cabang, getString(R.string.toast_DataCabangGagalDiperbarui),Toast.LENGTH_SHORT).show()
        }
    }


    fun cek_validasi() {
        val nama = etNama_cabang.text.toString()
        val alamat = etAlamat_cabang.text.toString()
        val layanan = etLayanan_cabang.text.toString()


        if (nama.isEmpty()) {
            etNama_cabang.error = getString(R.string.validasi_nama_cabang)
            Toast.makeText(this, getString(R.string.validasi_nama_cabang), Toast.LENGTH_SHORT).show()
            etNama_cabang.requestFocus()
            return
        }
        if (alamat.isEmpty()) {
            etAlamat_cabang.error = getString(R.string.validasi_alamat_cabang)
            Toast.makeText(this, getString(R.string.validasi_alamat_cabang), Toast.LENGTH_SHORT).show()
            etAlamat_cabang.requestFocus()
            return
        }

        if (layanan.isEmpty()) {
            etLayanan_cabang.error = getString(R.string.validasi_layanan_cabang)
            Toast.makeText(this, getString(R.string.validasi_layanan_cabang), Toast.LENGTH_SHORT).show()
            etLayanan_cabang.requestFocus()
            return
        }
        if (buttonSimpan_cabang.text.equals(getString(R.string.toast_save_cabang))) {
            simpan ()
        }else if(buttonSimpan_cabang.text.equals(getString(R.string.toast_edit_cabang))){
            hidup()
            etNama_cabang.requestFocus()
            buttonSimpan_cabang.text=getString(R.string.toast_perbarui_cabang)
        }else if (buttonSimpan_cabang.text.equals(getString(R.string.toast_perbarui_cabang))) {
            update()
        }


    }


    fun simpan() {
        val cabangBaru = myRef.push()
        val cabangId = cabangBaru.key ?: return
        val data = modelcabang(
            id_cabang = cabangId,
            namacabang = etNama_cabang.text.toString(),
            alamatcabang = etAlamat_cabang.text.toString(),
            layanancabang = etLayanan_cabang.text.toString(),
        )

        cabangBaru.setValue(data)
            .addOnSuccessListener {
                Toast.makeText(this, (getString(R.string.toast_Cabangberhasildisimpan)), Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener{
                Toast.makeText(this, (getString(R.string.toast_GagalmenyimpanCabang)), Toast.LENGTH_SHORT).show()
            }
    }


}