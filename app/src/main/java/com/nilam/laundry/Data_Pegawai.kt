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
import com.nilam.laundry.adapter.adapter_data_pegawai
import com.nilam.laundry.modeldata.modelpegawai
import com.nilam.laundry.TambahPegawai

class Data_Pegawai : AppCompatActivity() {
    lateinit var bt_data_pegawai_tambah : FloatingActionButton
    lateinit var rv_data_pegawai: RecyclerView
    lateinit var pegawaiList: ArrayList<modelpegawai>
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("pegawai")

    override fun onResume() {
        super.onResume()
        getData()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_data_pegawai)

        init()

        val layoutManager = LinearLayoutManager(this)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        rv_data_pegawai.layoutManager = layoutManager
        rv_data_pegawai.setHasFixedSize(true)
        pegawaiList = arrayListOf<modelpegawai>()

        bt_data_pegawai_tambah.setOnClickListener {
            val intent = Intent(this, TambahPegawai::class.java)
            startActivity(intent)
        }
        getData()

        val fabTambahPegawai: FloatingActionButton = findViewById(R.id.bt_data_pegawai_tambah)
        fabTambahPegawai.setOnClickListener {
            val intent = Intent(this, TambahPegawai::class.java)
            intent.putExtra("judul", this.getString(R.string.TambahPegawai))
            intent.putExtra("idPegawai", "")
            intent.putExtra("namaPegawai", "")
            intent.putExtra("noHPPegawai", "")
            intent.putExtra("alamatPegawai", "")
            intent.putExtra("cabangPegawai", "")
            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun init() {
        rv_data_pegawai = findViewById(R.id.rv_data_pegawai)
        bt_data_pegawai_tambah = findViewById(R.id.bt_data_pegawai_tambah)

    }


    fun getData() {
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                pegawaiList.clear()

                if (snapshot.exists()) {
                    for (pegawaiSnap in snapshot.children) {
                        val pegawai = pegawaiSnap.getValue(modelpegawai::class.java)
                        if (pegawai != null) {
                            pegawaiList.add(pegawai)
                        }
                    }

                    val adapter = adapter_data_pegawai(pegawaiList)
                    rv_data_pegawai.adapter = adapter

                } else {
                    Toast.makeText(this@Data_Pegawai, "Data kosong", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@Data_Pegawai, "Gagal memuat data: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

}