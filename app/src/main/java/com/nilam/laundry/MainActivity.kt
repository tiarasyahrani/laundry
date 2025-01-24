package com.nilam.laundry

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tvTanggal: TextView = findViewById(R.id.tvTanggal)
        val sdf = SimpleDateFormat("EEEE, d MMMM yyyy", Locale("id", "ID"))
        val currentDate = sdf.format(Calendar.getInstance().time)
        tvTanggal.text = currentDate

        val tvGreeting: TextView = findViewById(R.id.tvGreeting)
        val greetingMessage = getGreetingMessage()
        tvGreeting.text = greetingMessage

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun getGreetingMessage(): String {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)

        return when (hour) {
            in 5..11 -> "Selamat Pagi, Tiara N"   // 5:00 - 11:59
            in 12..15 -> "Selamat Siang, Tiara N" // 12:00 - 15:59
            in 16..18 -> "Selamat Sore, Tiara N"  // 16:00 - 18:59
            else -> "Selamat Malam, Tiara N"      // 19:00 - 4:59
        }
    }
}
