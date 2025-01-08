package com.example.a7powerz

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "UserDatabase.db"
        private const val DATABASE_VERSION = 2
        private const val TABLE_USERS = "users"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = """
            CREATE TABLE $TABLE_USERS (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT NOT NULL UNIQUE,
                password TEXT NOT NULL,
                email TEXT,
                phone TEXT
            )
        """
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        onCreate(db)
    }

    // Fungsi untuk menambahkan pengguna baru
    fun registerUser(username: String, password: String): Boolean {
        val db = this.writableDatabase
        val query = "SELECT * FROM $TABLE_USERS WHERE username = ?"
        val cursor = db.rawQuery(query, arrayOf(username))

        if (cursor.count > 0) {
            cursor.close()
            db.close()
            return false // Username sudah ada
        }

        cursor.close()

        val contentValues = ContentValues().apply {
            put("username", username)
            put("password", password)
        }

        val result = db.insert(TABLE_USERS, null, contentValues)
        db.close()

        return result != -1L
    }

    // Fungsi untuk memvalidasi login
    fun validateUser(username: String, password: String): Boolean {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_USERS WHERE username = ? AND password = ?"
        val cursor = db.rawQuery(query, arrayOf(username, password))
        val isValid = cursor.count > 0
        cursor.close()
        db.close()
        return isValid
    }

    // Fungsi untuk mendapatkan ID pengguna berdasarkan username
    fun getUserIdByUsername(username: String): Int {
        val db = this.readableDatabase
        val query = "SELECT id FROM $TABLE_USERS WHERE username = ?"
        val cursor = db.rawQuery(query, arrayOf(username))
        var userId = -1

        if (cursor.moveToFirst()) {
            userId = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
        }

        cursor.close()
        db.close()

        return userId
    }

    // Fungsi untuk menambahkan data default jika belum ada
    fun insertDefaultUserIfNotExist() {
        val db = this.writableDatabase
        val query = "SELECT * FROM $TABLE_USERS WHERE username = ?"
        val cursor = db.rawQuery(query, arrayOf("user1"))

        if (cursor.count == 0) { // Jika user1 belum ada
            val contentValues = ContentValues()
            contentValues.put("username", "user1")
            contentValues.put("password", "password1")
            db.insert(TABLE_USERS, null, contentValues)
        }

        cursor.close()
        db.close()
    }
    // Fungsi untuk memperbarui profil pengguna
    fun updateUserProfile(userId: Int, username: String, email: String, phone: String): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put("username", username)
            put("email", email)
            put("phone", phone)
        }

        val result = db.update(
            TABLE_USERS,
            contentValues,
            "id = ?",
            arrayOf(userId.toString())
        )

        db.close()

        // Mengembalikan true jika update berhasil (result > 0)
        return result > 0
    }

}
