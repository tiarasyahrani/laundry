package com.nilam.laundry

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.nilam.laundry.adapter.adapter_pilih_pelanggan
import com.nilam.laundry.modeldata.modelpelanggan

class Pilih_Pelanggan : AppCompatActivity() {
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("pelanggan")
    lateinit var rvPilihPelanggan: RecyclerView
    lateinit var pelangganList: ArrayList<modelpelanggan>
    lateinit var tvkosong: TextView
    private lateinit var searchView: androidx.appcompat.widget.SearchView
    private lateinit var sharedPref: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pilih_pelanggan2)
        sharedPref = getSharedPreferences("user_data", MODE_PRIVATE)
        init()
        val layoutManager = LinearLayoutManager(this)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        rvPilihPelanggan.layoutManager=layoutManager
        rvPilihPelanggan.setHasFixedSize(true)
        pelangganList = arrayListOf()
        getData()
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }


    private fun init() {
        rvPilihPelanggan = findViewById(R.id.rv_pilih_pelanggan)
        tvkosong = findViewById(R.id.tv_kosong)
        searchView = findViewById(R.id.searchView_pelanggan)
    }


    fun getData(){
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    pelangganList.clear()
                    for (dataSnapshot in snapshot.children) {
                        val pegawai = dataSnapshot.getValue(modelpelanggan::class.java)
                        pelangganList.add(pegawai!!)
                    }
                    val adapter = adapter_pilih_pelanggan(pelangganList)
                    rvPilihPelanggan.adapter = adapter
                }else{
                    tvkosong.visibility = View.VISIBLE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@Pilih_Pelanggan,error.message, Toast.LENGTH_SHORT).show()
            }
        })


    }

}