package com.nilam.laundry

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.nilam.laundry.adapter.adapter_pilih_pelanggan
import com.nilam.laundry.modeldata.modelpelanggan

class Pilih_Pelanggan : AppCompatActivity() {
    private val database = FirebaseDatabase.getInstance()
    private val myRef = database.getReference("pelanggan")

    private lateinit var rvPilihPelanggan: RecyclerView
    private lateinit var pelangganList: ArrayList<modelpelanggan>
    private lateinit var tvkosong: TextView
    private lateinit var searchView: SearchView
    private lateinit var sharedPref: SharedPreferences
    private lateinit var adapter: adapter_pilih_pelanggan

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pilih_pelanggan2)

        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        sharedPref = getSharedPreferences("user_data", MODE_PRIVATE)
        initViews()

        val layoutManager = LinearLayoutManager(this)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        rvPilihPelanggan.layoutManager = layoutManager
        rvPilihPelanggan.setHasFixedSize(true)

        pelangganList = arrayListOf()
        adapter = adapter_pilih_pelanggan(pelangganList)
        rvPilihPelanggan.adapter = adapter

        tvkosong.text = "Loading..."
        tvkosong.visibility = View.VISIBLE
        rvPilihPelanggan.visibility = View.GONE

        setupSearch()
        getData()
    }

    private fun initViews() {
        rvPilihPelanggan = findViewById(R.id.rv_pilih_pelanggan)
        tvkosong = findViewById(R.id.tv_kosong)
        searchView = findViewById(R.id.searchView_pelanggan)
    }

    private fun setupSearch() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filterList(newText ?: "")
                return true
            }
        })
    }

    fun getData() {
        tvkosong.text = "Loading..."
        tvkosong.visibility = View.VISIBLE
        rvPilihPelanggan.visibility = View.GONE

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    pelangganList.clear()
                    for (dataSnapshot in snapshot.children) {
                        val pegawai = dataSnapshot.getValue(modelpelanggan::class.java)
                        pelangganList.add(pegawai!!)
                    }

                    val adapter = adapter_pilih_pelanggan(ArrayList(pelangganList))
                    rvPilihPelanggan.adapter = adapter

                    searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(query: String?): Boolean {
                            return false
                        }

                        override fun onQueryTextChange(newText: String?): Boolean {
                            adapter.filterList(newText ?: "")
                            return true
                        }
                    })

                    tvkosong.visibility = View.GONE
                    rvPilihPelanggan.visibility = View.VISIBLE

                } else {
                    tvkosong.text = "Data Tidak Ditemukan"
                    tvkosong.visibility = View.VISIBLE
                    rvPilihPelanggan.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@Pilih_Pelanggan, error.message, Toast.LENGTH_SHORT).show()
                tvkosong.text = "Error: ${error.message}"
                tvkosong.visibility = View.VISIBLE
                rvPilihPelanggan.visibility = View.GONE
            }
        })
    }

}
