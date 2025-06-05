package com.nilam.laundry

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.nilam.laundry.modeldata.model_user
import com.google.firebase.database.FirebaseDatabase

class Sign_Up : AppCompatActivity() {

    private lateinit var etNama: EditText
    private lateinit var etNoHp: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnDaftar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        etNama = findViewById(R.id.etNama_SignUp)
        etNoHp = findViewById(R.id.etNoHp_SignUp)
        etPassword = findViewById(R.id.etPass_SignUp)
        btnDaftar = findViewById(R.id.btn_SignUp)

        btnDaftar.setOnClickListener {
            val nama = etNama.text.toString().trim()
            val nohp = etNoHp.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (nama.isEmpty() || nohp.isEmpty() || password.isEmpty()) {
                Toast.makeText(this,  getString(R.string.toast_Harapisisemuadata), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val uid = FirebaseDatabase.getInstance().getReference("users").push().key ?: return@setOnClickListener
            val user = model_user(uid, nama, nohp, password)

            FirebaseDatabase.getInstance().getReference("users")
                .child(uid)
                .setValue(user)
                .addOnSuccessListener {
                    Toast.makeText(this, getString(R.string.toast_Registrasiberhasil), Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, login::class.java))
                    finish()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Gagal simpan data: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
}