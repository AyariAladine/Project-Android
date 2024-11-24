package com.example.bookproject.DataClass

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @POST("auth/signup")
    fun signUp(@Body request: SignUpRequest): Call<SignUpResponse>

    @POST("auth/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @POST("auth/forgot-password")
    fun forgotPassword(@Body request: ForgotPasswordRequest): Call<ForgotPasswordResponse>

    @POST("auth/verify-reset-code")
    fun verifyResetCode(@Body request: VerifyCodeRequest): Call<VerifyCodeResponse>

    @POST("auth/reset-password")
    fun resetPassword(@Body request: ResetPasswordRequest): Call<Void>


    @GET("stories/recommended")
    suspend fun getRecommendedStories(): List<Story>
    @GET("stories/stories")
    suspend fun getStories(): List<Story>

    @GET("story/details/{id}")
    suspend fun getStoryDetails(@Path("id") id: String): Story


}


