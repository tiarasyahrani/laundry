package com.nilam.laundry

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.SharedPreferences
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.nilam.laundry.adapter.adapter_pilih_layanan
import com.nilam.laundry.modeldata.modellayanan

class activity_pilih_layanan : AppCompatActivity() {
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("layanan")
    lateinit var rvPilihLayanan: RecyclerView
    lateinit var layananList: ArrayList<modellayanan>
    lateinit var tvkosong: TextView
    private lateinit var searchView: androidx.appcompat.widget.SearchView
    private lateinit var sharedPref: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pilih_layanan)
        sharedPref = getSharedPreferences("user_data", MODE_PRIVATE)
        init()
        val layoutManager = LinearLayoutManager(this)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        rvPilihLayanan.layoutManager=layoutManager
        rvPilihLayanan.setHasFixedSize(true)
        layananList = arrayListOf()
        getData()
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }


    private fun init() {
        rvPilihLayanan = findViewById(R.id.rv_pilih_layanan)
        tvkosong = findViewById(R.id.tv_kosong)
        searchView = findViewById(R.id.searchView_layanan)
    }


    fun getData(){
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    layananList.clear()
                    for (dataSnapshot in snapshot.children) {
                        val layanan = dataSnapshot.getValue(modellayanan::class.java)
                        layananList.add(layanan!!)
                    }
                    val adapter = adapter_pilih_layanan(layananList)
                    rvPilihLayanan.adapter = adapter
                }else{
                    tvkosong.visibility = View.VISIBLE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@activity_pilih_layanan,error.message, Toast.LENGTH_SHORT).show()
            }
        })


    }

}