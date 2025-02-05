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
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var cv_layanan : CardView
    lateinit var cv_tambahan : CardView
    lateinit var cardPegawai : CardView
    lateinit var cv_pelanggan : CardView


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


        cardPegawai.setOnClickListener{
            val intent = Intent(this, Data_Pegawai::class.java)
            startActivity(intent)
        }

        cv_pelanggan.setOnClickListener{
            val intent = Intent(this, data_pelanggan::class.java)
            startActivity(intent)
        }

        val greetingTextView: TextView = findViewById(R.id.tvGreeting)
        greetingTextView.text = getGreetingMessage()

        val dateTextView: TextView = findViewById(R.id.tvTanggal)
        dateTextView.text = getCurrentDate()

    }

    private fun getGreetingMessage(): String {
        val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        return when (currentHour) {
            in 5..11 -> "Selamat Pagi, Tiara N"
            in 12..14 -> "Selamat Siang, Tiara N"
            in 15..17 -> "Selamat Sore, Tiara N"
            else -> "Selamat Malam, Tiara N"
        }
    }

    private fun getCurrentDate(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("id", "ID"))
        return dateFormat.format(calendar.time)
    }

}