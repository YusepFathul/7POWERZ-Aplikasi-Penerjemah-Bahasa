package com.example.a7powerz

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MyMemoryApiService {
    @GET("get")
    fun getTranslation(
        @Query("q") text: String,
        @Query("langpair") langpair: String = "id|en" // Bahasa Indonesia ke Bahasa Inggris
    ): Call<MyMemoryResponse>
}
