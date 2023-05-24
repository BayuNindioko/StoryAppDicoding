package com.example.storyapp.api

import com.example.storyapp.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @POST("v1/login")
    fun login(
        @Body request: LoginRequest
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("v1/register")
    fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<RegisterResponse>

    @GET("/v1/stories")
    suspend fun getAllStories(
        @Header("Authorization") token: String,
        @Query("page") page: Int?,
        @Query("size") size: Int?,
        @Query("location") location: Int = 0
    ): StoryResponse

    @GET("/v1/stories")
    fun getLocation(
        @Header("Authorization") token: String,
        @Query("page") page: Int?,
        @Query("size") size: Int?,
        @Query("location") location: Int = 0
    ): Call<StoryResponse>

    @GET("v1/stories/{id}")
    fun getDetail(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Call<DetailResponse>

    @Multipart
    @POST("/v1/stories")
    fun uploadImage(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody
    ): Call<FileUploadResponse>

}