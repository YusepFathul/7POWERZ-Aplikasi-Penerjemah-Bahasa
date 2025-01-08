package com.example.a7powerz

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class EditProfileActivity : AppCompatActivity() {

    private lateinit var avatarImage: ImageView
    private lateinit var etUsername: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPhone: EditText
    private lateinit var btnUpdateProfile: Button
    private lateinit var dbHelper: DatabaseHelper
    private var userId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)


        dbHelper = DatabaseHelper(this)

        // Ambil userId dari SharedPreferences
        val sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE)
        userId = sharedPreferences.getInt("USER_ID", -1)

        // Inisialisasi Views
        avatarImage = findViewById(R.id.avatarImage)
        etUsername = findViewById(R.id.etUsername)
        etEmail = findViewById(R.id.etEmail)
        etPhone = findViewById(R.id.etPhone)
        btnUpdateProfile = findViewById(R.id.btnUpdateProfile)

        // Memuat data pengguna
        loadUserProfile()

        // Tombol Update Profile
        btnUpdateProfile.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val phone = etPhone.text.toString().trim()

            if (validateInput(username, email, phone)) {
                updateProfile(username, email, phone)
            }
        }

        // Klik pada avatar untuk mengganti gambar (simulasi)
        avatarImage.setOnClickListener {
            Toast.makeText(this, "Avatar clicked!", Toast.LENGTH_SHORT).show()
            // Tambahkan logika untuk mengganti avatar
        }
    }

    // Memuat data pengguna dari SQLite
    private fun loadUserProfile() {
        val db = dbHelper.readableDatabase
        val query = "SELECT * FROM users WHERE id = ?"
        val cursor = db.rawQuery(query, arrayOf(userId.toString()))

        if (cursor.moveToFirst()) {
            val username = cursor.getString(cursor.getColumnIndexOrThrow("username"))
            val email = cursor.getString(cursor.getColumnIndexOrThrow("email"))
            val phone = cursor.getString(cursor.getColumnIndexOrThrow("phone"))

            etUsername.setText(username)
            etEmail.setText(email)
            etPhone.setText(phone)
        } else {
            Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show()
        }

        cursor.close()
        db.close()
    }

    // Validasi input
    private fun validateInput(username: String, email: String, phone: String): Boolean {
        if (username.isEmpty()) {
            etUsername.error = "Username cannot be empty"
            return false
        }
        if (email.isEmpty()) {
            etEmail.error = "Email cannot be empty"
            return false
        }
        if (phone.isEmpty()) {
            etPhone.error = "Phone cannot be empty"
            return false
        }
        return true
    }

    // Memperbarui profil pengguna
    private fun updateProfile(username: String, email: String, phone: String) {
        val success = dbHelper.updateUserProfile(userId, username, email, phone)

        if (success) {
            Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "Failed to update profile", Toast.LENGTH_SHORT).show()
        }
    }
}
