package com.nilam.laundry

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.cardview.widget.CardView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import transaksi.transaksi
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var cardLayanan : CardView
    lateinit var cv_tambahan : CardView
    lateinit var cardPegawai : CardView
    lateinit var cv_pelanggan : CardView
    lateinit var cv_transaksi : CardView
    lateinit var cv_cabang : CardView
    lateinit var cv_laporan : CardView
    lateinit var cv_akun : CardView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        cardPegawai= findViewById(R.id.cardPegawai)
        cv_pelanggan = findViewById(R.id.cv_pelanggan)
        cardLayanan = findViewById(R.id.cardLayanan)
        cv_transaksi = findViewById(R.id.cv_transaksi)
        cv_tambahan = findViewById(R.id.cardTambahan)
        cv_cabang = findViewById(R.id.cardCabang)
        cv_laporan = findViewById(R.id.cv_laporan)
        cv_akun = findViewById(R.id.cardAkun)




        cardPegawai.setOnClickListener{
            val intent = Intent(this, Data_Pegawai::class.java)
            startActivity(intent)
        }

        cv_pelanggan.setOnClickListener{
            val intent = Intent(this, data_pelanggan::class.java)
            startActivity(intent)
        }
        cardLayanan.setOnClickListener{
            val intent = Intent(this, Data_Layanan::class.java)
            startActivity(intent)
        }
        cv_transaksi.setOnClickListener{
            val intent = Intent(this, transaksi::class.java)
            startActivity(intent)
        }
        cv_tambahan.setOnClickListener {
            val intent = Intent(this, data_tambahkan::class.java)
            startActivity(intent)
        }
        cv_cabang.setOnClickListener {
            val intent = Intent(this, data_cabang::class.java)
            startActivity(intent)
        }
        cv_laporan.setOnClickListener {
            val intent = Intent(this, activity_data_laporan::class.java)
            startActivity(intent)
        }
        cv_akun.setOnClickListener {
            val intent = Intent(this, Account::class.java)
            startActivity(intent)
        }

        val greetingTextView: TextView = findViewById(R.id.tvGreeting)
        greetingTextView.text = getGreetingMessage()

        val dateTextView: TextView = findViewById(R.id.tvTanggal)
        dateTextView.text = getCurrentDate()

    }

    private fun getGreetingMessage(): String {
        val sharedPref = getSharedPreferences("USER_DATA", MODE_PRIVATE)
        val nama = sharedPref.getString("nama", "Pengguna") ?: "Pengguna"
        val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        return when (currentHour) {
            in 5..11 -> getString(R.string.selamat_pagi, nama)
            in 12..14 -> getString(R.string.selamat_siang, nama)
            in 15..17 -> getString(R.string.selamat_sore, nama)
            else -> getString(R.string.selamat_malam, nama)
        }
    }



    private fun getCurrentDate(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("id", "ID"))
        return dateFormat.format(calendar.time)
    }

}