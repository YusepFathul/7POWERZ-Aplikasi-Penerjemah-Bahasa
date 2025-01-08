package com.example.a7powerz

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.a7powerz.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DatabaseHelper(this)


        binding.btnRegister.setOnClickListener {
            register()
        }


        binding.backToLoginLink.setOnClickListener {
            goToLogin()
        }
    }

    private fun register() {
        val username = binding.editUsername.text.toString()
        val password = binding.editPassword.text.toString()

        // Validasi input username dan password
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Username dan password harus diisi", Toast.LENGTH_SHORT).show()
        } else {
            // Registrasi pengguna baru
            val success = dbHelper.registerUser(username, password)

            if (success) {
                Toast.makeText(this, "Registrasi berhasil", Toast.LENGTH_SHORT).show()
                finish() // Kembali ke halaman login
            } else {
                Toast.makeText(this, "Username sudah terdaftar", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Fungsi untuk navigasi ke halaman login
    private fun goToLogin() {
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
        finish() // Hapus activity register dari stack
    }
}
