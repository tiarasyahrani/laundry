package com.nilam.laundry

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.nilam.laundry.adapter.adapter_pilih_layanan
import com.nilam.laundry.modeldata.modellayanan

class activity_pilih_layanan : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var layananList: ArrayList<modellayanan>
    private lateinit var adapter: adapter_pilih_layanan
    private lateinit var databaseReference: DatabaseReference
    private lateinit var tvKosong: TextView
    private val TAG = "PilihLayanan"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pilih_layanan)

        recyclerView = findViewById(R.id.rv_pilih_layanan)
        tvKosong = findViewById(R.id.tv_kosong)
        val searchView = findViewById<androidx.appcompat.widget.SearchView>(R.id.searchView_layanan)

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)

        layananList = ArrayList()
        adapter = adapter_pilih_layanan(layananList)
        recyclerView.adapter = adapter

        databaseReference = FirebaseDatabase.getInstance().getReference("layanan")

        tvKosong.text = "Loading..."
        tvKosong.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE



        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filterList(newText.orEmpty())
                return true
            }
        })


        loadLayananData()
    }

    private fun loadLayananData() {
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d(TAG, "onDataChange: Data received from Firebase")
                layananList.clear()

                if (snapshot.exists()) {
                    for (layananSnapshot in snapshot.children) {
                        val layanan = layananSnapshot.getValue(modellayanan::class.java)
                        if (layanan != null) {
                            val updatedLayanan = modellayanan(
                                layananSnapshot.key ?: "",
                                layanan.tv_namalayanan,
                                layanan.tv_harga
                            )
                            layananList.add(updatedLayanan)
                        }
                    }

                    adapter.updateData(layananList)
                    Log.d(TAG, "Final list size: ${layananList.size}")

                    if (layananList.isNotEmpty()) {
                        recyclerView.visibility = View.VISIBLE
                        tvKosong.visibility = View.GONE
                    } else {
                        recyclerView.visibility = View.GONE
                        tvKosong.visibility = View.VISIBLE
                        tvKosong.text = "Data Tidak Ditemukan"
                    }
                } else {
                    recyclerView.visibility = View.GONE
                    tvKosong.visibility = View.VISIBLE
                    tvKosong.text = "Data Tidak Ditemukan"
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(TAG, "Database error: ${error.message}")
                recyclerView.visibility = View.GONE
                tvKosong.visibility = View.VISIBLE
                tvKosong.text = "Error: ${error.message}"
            }
        })
    }
}
