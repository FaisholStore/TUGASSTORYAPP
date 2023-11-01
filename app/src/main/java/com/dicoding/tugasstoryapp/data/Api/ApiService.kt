package com.dicoding.tugasstoryapp.data.Api

import com.dicoding.tugasstoryapp.Response.DetailStoryResponse
import com.dicoding.tugasstoryapp.Response.GetStoriesResponse
import com.dicoding.tugasstoryapp.Response.LoginResponse
import com.dicoding.tugasstoryapp.Response.PostStoryResponse
import com.dicoding.tugasstoryapp.Response.RegisterResponse
import com.dicoding.tugasstoryapp.data.Load.LoginPayload
import com.dicoding.tugasstoryapp.data.Load.RegisterLoad
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("/v1/stories")
    fun getStories(
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
        @Query("location") location: Int = 0
    ): Call<GetStoriesResponse>

    @GET("/v1/stories/{userId}")
    fun getDetailStory(
        @Path("userId") userId: String
    ): Call<DetailStoryResponse>

    @Multipart
    @POST("/v1/stories")
    fun postStory(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody
    ): Call<PostStoryResponse>


    @Headers("No-Authentication: true")
    @POST("/v1/login")
    fun login(
        @Body payload: LoginPayload
    ): Call<LoginResponse>

    @Headers("No-Authentication: true")
    @POST("/v1/register")
    fun register(
        @Body payload: RegisterLoad
    ): Call<RegisterResponse>
}