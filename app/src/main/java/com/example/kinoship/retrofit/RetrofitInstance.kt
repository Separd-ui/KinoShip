package com.example.kinoship.retrofit

import com.example.kinoship.utils.Constans
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {

    companion object{

        private val retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(Constans.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        val jsonHelper by lazy {
            retrofit.create(JsonHelper::class.java)
        }
    }
}