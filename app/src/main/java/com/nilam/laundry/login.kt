package com.nilam.laundry

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.MotionEvent
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import com.nilam.laundry.modeldata.model_user

class login : AppCompatActivity() {

    private lateinit var etNoHp: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var tvRegister: TextView
    private lateinit var dbRef: DatabaseReference
    private var isPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etNoHp = findViewById(R.id.etNoHp_Login)
        etPassword = findViewById(R.id.etPass_Login)
        btnLogin = findViewById(R.id.btn_SignIn)
        tvRegister = findViewById(R.id.tvSignUp)

        dbRef = FirebaseDatabase.getInstance().getReference("users")



        tvRegister.setOnClickListener {
            startActivity(Intent(this, Sign_Up::class.java))
        }

        btnLogin.setOnClickListener {
            val inputNoHp = etNoHp.text.toString().trim()
            val inputPassword = etPassword.text.toString().trim()

            if (inputNoHp.isEmpty() || inputPassword.isEmpty()) {
                Toast.makeText(this, "Isi semua data", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            dbRef.orderByChild("nohp").equalTo(inputNoHp)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (data in snapshot.children) {
                            val user = data.getValue(model_user::class.java)
                            if (user != null && user.password == inputPassword) {
                                val sharedPref = getSharedPreferences("USER_DATA", MODE_PRIVATE)
                                sharedPref.edit()
                                    .putString("uid", user.uid)
                                    .putString("nama", user.nama)
                                    .putString("no_hp", user.nohp)
                                    .putString("password", user.password)
                                    .apply()

                                Toast.makeText(this@login, "Berhasil Login", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this@login, MainActivity::class.java))
                                finish()
                                return
                            }
                        }
                        Toast.makeText(this@login, "Login gagal", Toast.LENGTH_SHORT).show()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@login, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }
}
