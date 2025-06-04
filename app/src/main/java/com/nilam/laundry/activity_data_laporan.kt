package com.nilam.laundry

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.nilam.laundry.databinding.ActivityDataLaporanBinding
import com.nilam.laundry.modeldata.model_laporan
import com.nilam.laundry.adapter.adapter_laporan
import com.google.firebase.database.*

class activity_data_laporan : AppCompatActivity() {

    private lateinit var binding: ActivityDataLaporanBinding
    private lateinit var database: DatabaseReference
    private lateinit var laporanList: MutableList<model_laporan>
    private lateinit var laporanAdapter: adapter_laporan

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDataLaporanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance().getReference("laporan")
        laporanList = mutableListOf()
        laporanAdapter = adapter_laporan(this, laporanList)

        binding.rvDataLaporan.apply {
            layoutManager = LinearLayoutManager(this@activity_data_laporan)
            adapter = laporanAdapter
        }

        ambilDataLaporan()
    }

    private fun ambilDataLaporan() {
        binding.progressBar.visibility = View.VISIBLE
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                laporanList.clear()
                for (data in snapshot.children) {
                    val laporan = data.getValue(model_laporan::class.java)
                    if (laporan != null) {
                        laporanList.add(laporan)
                    }
                }
                laporanAdapter.notifyDataSetChanged()
                binding.ac.visibility = View.GONE
                binding.progressBar.visibility = View.GONE
            }

            override fun onCancelled(error: DatabaseError) {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this@activity_data_laporan, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateSemuaStatusJikaKosong() {
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (data in snapshot.children) {
                    val laporan = data.getValue(model_laporan::class.java)
                    if (laporan != null) {
                        val id = data.key ?: continue
                        if (laporan.status.isNullOrEmpty()) {
                            val statusBaru = if (laporan.metodePembayaran.equals("Bayar Nanti", true)) {
                                "Belum Dibayar"
                            } else {
                                "Lunas"
                            }
                            database.child(id).child("status").setValue(statusBaru)
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
     }
}