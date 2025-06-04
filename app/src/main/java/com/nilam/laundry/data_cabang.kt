package com.nilam.laundry

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.nilam.laundry.adapter.adapter_data_cabang
import com.nilam.laundry.modeldata.modelcabang

class data_cabang : AppCompatActivity() {
    lateinit var bt_data_cabang_tambah : FloatingActionButton
    lateinit var rv_data_cabang: RecyclerView
    lateinit var cabangList: ArrayList<modelcabang>
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("cabang")

    override fun onResume() {
        super.onResume()
        getData()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_data_cabang)

        init()

        val layoutManager = LinearLayoutManager(this)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        rv_data_cabang.layoutManager = layoutManager
        rv_data_cabang.setHasFixedSize(true)
        cabangList = arrayListOf<modelcabang>()

        bt_data_cabang_tambah.setOnClickListener {
            val intent = Intent(this, tambah_cabang::class.java)
            startActivity(intent)
        }
        getData()

        val fabtambah_cabang: FloatingActionButton = findViewById(R.id.bt_data_cabang_tambah)
        fabtambah_cabang.setOnClickListener {
            val intent = Intent(this, tambah_cabang::class.java)
            intent.putExtra("judul", this.getString(R.string.tambah_cabang))
            intent.putExtra("id_cabang", "")
            intent.putExtra("namacabang", "")
            intent.putExtra("alamatcabang", "")
            intent.putExtra("layanancabang", "")
            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun init() {
        rv_data_cabang = findViewById(R.id.rv_data_cabang)
        bt_data_cabang_tambah = findViewById(R.id.bt_data_cabang_tambah)

    }


    fun getData() {
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                cabangList.clear()

                if (snapshot.exists()) {
                    for (cabangSnap in snapshot.children) {
                        val cabang = cabangSnap.getValue(modelcabang::class.java)
                        if (cabang != null) {
                            cabangList.add(cabang)
                        }
                    }

                    val adapter = adapter_data_cabang(cabangList)
                    rv_data_cabang.adapter = adapter

                } else {
                    Toast.makeText(this@data_cabang, "Data kosong", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@data_cabang, "Gagal memuat data: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

}