package com.example.a7powerz

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.a7powerz.databinding.ActivityDashboardBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Dashboard : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Menggunakan view binding
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Menampilkan nama pengguna di dashboard
        val username = intent.getStringExtra("username") ?: "User"
        binding.welcomeText.text = "Welcome, $username"

        // Fungsi untuk menerjemahkan teks
        binding.btnTranslate.setOnClickListener {
            val textToTranslate = binding.inputText.text.toString()
            if (textToTranslate.isNotEmpty()) {
                translateText(textToTranslate)
            } else {
                Toast.makeText(this, "Please enter text to translate", Toast.LENGTH_SHORT).show()
            }
        }

        // Menuju ke halaman ProfileActivity
        binding.iconProfile.setOnClickListener {
            navigateToActivity(ProfileActivity::class.java, username)
        }

        // Menuju ke halaman EditProfileActivity
        binding.profileImage.setOnClickListener {
            navigateToActivity(EditProfileActivity::class.java, username)
        }
    }

    private fun translateText(text: String) {
        val langpair = "id|en" // Menetapkan pasangan bahasa (Bahasa Indonesia ke Bahasa Inggris)
        val call = ApiClient.apiService.getTranslation(text, langpair)

        // Mengirim permintaan API secara asynchronous
        call.enqueue(object : Callback<MyMemoryResponse> {
            override fun onResponse(
                call: Call<MyMemoryResponse>,
                response: Response<MyMemoryResponse>
            ) {
                if (response.isSuccessful) {
                    // Mendapatkan hasil terjemahan dari respons API
                    val translatedText = response.body()?.responseData?.translatedText
                    // Menampilkan hasil terjemahan di UI
                    binding.outputText.text = "Hasil Terjemahan: $translatedText"
                } else {
                    // Menampilkan pesan error jika respons gagal
                    Toast.makeText(this@Dashboard, "Translation failed", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<MyMemoryResponse>, t: Throwable) {
                // Menangani kesalahan jaringan atau kegagalan API
                Toast.makeText(this@Dashboard, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Fungsi umum untuk navigasi ke activity lain
    private fun navigateToActivity(targetActivity: Class<*>, username: String) {
        val intent = Intent(this, targetActivity).apply {
            putExtra("username", username)
        }
        startActivity(intent)
    }

    // Handle back button untuk logout
    override fun onBackPressed() {
        AlertDialog.Builder(this)
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Yes") { _, _ ->
                val intent = Intent(this, Login::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                startActivity(intent)
                finish()
            }
            .setNegativeButton("No", null)
            .show()
    }
}
