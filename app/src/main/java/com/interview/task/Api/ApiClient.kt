package com.interview.task.Api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object  ApiClient {

    val BASE_URL = "https://jsonplaceholder.typicode.com/"


    private var retrofit: Retrofit? = null


    fun getClient(): Retrofit? {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit
    }
}