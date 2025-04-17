package com.nilam.laundry

import android.content.Intent
import android.os.Bundle
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
import com.nilam.laundry.adapter.adapter_data_pelanggan
import com.nilam.laundry.modeldata.modelpelanggan


class data_pelanggan : AppCompatActivity() {
    lateinit var bt_data_pelanggan_tambah: FloatingActionButton
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("pelanggan")
    lateinit var rv_data_pelanggan: RecyclerView
    lateinit var pelangganList: ArrayList<modelpelanggan>

    override fun onResume() {
        super.onResume()
        getdata()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_data_pelanggan)

        init()

        val layoutManager = LinearLayoutManager(this)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        rv_data_pelanggan.layoutManager = layoutManager
        rv_data_pelanggan.setHasFixedSize(true)
        pelangganList = arrayListOf<modelpelanggan>()

        getdata()

        val fabTambahPegawai: FloatingActionButton = findViewById(R.id.bt_data_pelanggan_tambah)
        fabTambahPegawai.setOnClickListener {
            val intent = Intent(this, TambahPegawai::class.java)
            intent.putExtra("judul", this.getString(R.string.TambahPelanggan))
            intent.putExtra("id_pelanggan", "")
            intent.putExtra("tv_nama_pelanggan", "")
            intent.putExtra("tv_nohp_pelanggan", "")
            intent.putExtra("tv_alamat_pelanggan", "")
            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        bt_data_pelanggan_tambah = findViewById(R.id.bt_data_pelanggan_tambah)

        bt_data_pelanggan_tambah.setOnClickListener{
            val intent = Intent(this, TambahPelanggan::class.java)
            startActivity(intent)
        }
    }




    fun init() {
        rv_data_pelanggan = findViewById(R.id.rv_data_pelanggan)
        bt_data_pelanggan_tambah = findViewById(R.id.bt_data_pelanggan_tambah)

    }

    fun getdata() {
        val query = myRef.orderByChild("idPelanggan").limitToLast(100)
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    pelangganList.clear()
                    for (dataSnapshot in snapshot.children) {
                        val pelanggan = dataSnapshot.getValue(modelpelanggan::class.java)
                        pelanggan?.let{pelangganList.add(it)}
                    }
                    val adapter = adapter_data_pelanggan(pelangganList)
                    rv_data_pelanggan.adapter = adapter
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) { }
        })
    }
}