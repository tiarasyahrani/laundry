package com.nilam.laundry

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.activity.enableEdgeToEdge
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import transaksi.transaksi
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var cardLayanan: CardView
    lateinit var cv_tambahan: CardView
    lateinit var cardPegawai: CardView
    lateinit var cv_pelanggan: CardView
    lateinit var cv_transaksi: CardView
    lateinit var cv_cabang: CardView
    lateinit var cv_laporan: CardView
    lateinit var cv_akun: CardView

    private lateinit var estimationText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        cardPegawai = findViewById(R.id.cardPegawai)
        cv_pelanggan = findViewById(R.id.cv_pelanggan)
        cardLayanan = findViewById(R.id.cardLayanan)
        cv_transaksi = findViewById(R.id.cv_transaksi)
        cv_tambahan = findViewById(R.id.cardTambahan)
        cv_cabang = findViewById(R.id.cardCabang)
        cv_laporan = findViewById(R.id.cv_laporan)
        cv_akun = findViewById(R.id.cardAkun)

        estimationText = findViewById(R.id.estimation_text)

        cardPegawai.setOnClickListener {
            val intent = Intent(this, Data_Pegawai::class.java)
            startActivity(intent)
        }

        cv_pelanggan.setOnClickListener {
            val intent = Intent(this, data_pelanggan::class.java)
            startActivity(intent)
        }

        cardLayanan.setOnClickListener {
            val intent = Intent(this, Data_Layanan::class.java)
            startActivity(intent)
        }

        cv_transaksi.setOnClickListener {
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

        fetchAndCalculateTotalLaporan()
    }

    override fun onResume() {
        super.onResume()
        val greetingTextView: TextView = findViewById(R.id.tvGreeting)
        greetingTextView.text = getGreetingMessage()
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

    private fun fetchAndCalculateTotalLaporan() {
        val database = FirebaseDatabase.getInstance().getReference("laporan")
        val estimationText: TextView = findViewById(R.id.estimation_text)

        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var totalEstimasi = 0L
                for (laporanSnapshot in snapshot.children) {
                    val totalBayarStr = laporanSnapshot.child("totalBayar").getValue(String::class.java) ?: "0"
                    val totalBayarAngka = parseRupiahToInt(totalBayarStr)
                    totalEstimasi += totalBayarAngka
                }
                estimationText.text = formatRupiah(totalEstimasi)
            }

            override fun onCancelled(error: DatabaseError) {
                estimationText.text = "Rp 0"
            }
        })
    }

    private fun parseRupiahToInt(rupiahStr: String): Long {
        val cleaned = rupiahStr
            .replace("Rp", "")
            .replace(".", "")
            .replace(",00", "")
            .trim()

        return cleaned.toLongOrNull() ?: 0L
    }

    private fun formatRupiah(number: Long): String {
        val symbols = DecimalFormatSymbols(Locale("id", "ID")).apply {
            currencySymbol = "Rp "
            monetaryDecimalSeparator = ','
            groupingSeparator = '.'
        }
        val formatter = DecimalFormat("¤ #,###.00", symbols)
        return formatter.format(number)
    }
}
