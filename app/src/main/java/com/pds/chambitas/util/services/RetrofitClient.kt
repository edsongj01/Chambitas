package com.pds.chambitas.util.services;

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.util.concurrent.TimeUnit

object RetrofitClient {

    var retrofit: Retrofit? = null

    fun getClient(url: String): Retrofit? {
        if (retrofit == null) {
            var gson: Gson = GsonBuilder().setLenient().serializeNulls().create()
            val fileLogger = HttpLoggingInterceptor.Logger { s ->
                try {
                    val outputStream = FileOutputStream(File("/sdcard/loghttp.txt"), true)
                    outputStream.write(s.toByteArray())
                    outputStream.close()
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                }
            }
            val interceptor = HttpLoggingInterceptor()
            val fileLoggingInterceptor = HttpLoggingInterceptor(fileLogger)
            val loggingInterceptor = HttpLoggingInterceptor()

            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

            val retryInterceptor = RetryInterceptor(5)

            val client = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addInterceptor(fileLoggingInterceptor)
                .addInterceptor(loggingInterceptor)
                .addInterceptor(retryInterceptor)
                .connectTimeout(10000, TimeUnit.SECONDS)
                .readTimeout(10000, TimeUnit.SECONDS)
                .writeTimeout(10000, TimeUnit.SECONDS)
                .build()

            retrofit = Retrofit.Builder()
                .client(client)
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        }
        return retrofit
    }

    fun getApiService(): APIService? {
        return ApiUtils.apiService
    }

}