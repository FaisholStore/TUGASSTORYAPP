package com.dicoding.tugasstoryapp.data.Api


import com.dicoding.tugasstoryapp.Utils.Const
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {

    // membuat variabel untuk token
    private var token = ""

    fun setToken(value: String){
        token = value
    }
    private fun getHeaderInterceptor(): Interceptor {
        return Interceptor { chain ->
            var request = chain.request()

            if(request.header("No-Authentication") == null) {
                if(token.isNotEmpty()) {
                    val finalToken = "Bearer $token"
                    request = request.newBuilder()
                        .addHeader(Const.AUTH_HEADER, finalToken)
                        .build()
                }
            }
            chain.proceed(request)
        }
    }


    fun getApiService(): ApiService {
        val loggingInterceptor = if (Const.DEBUG) {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        } else {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(getHeaderInterceptor())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(Const.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(ApiService::class.java)

    }




}