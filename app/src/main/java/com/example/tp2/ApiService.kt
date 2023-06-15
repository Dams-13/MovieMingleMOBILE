package com.example.tp2

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {

    @POST("users/signup")
    fun signUp(@Body signUpRequest: SignUpRequest): Call<SignUpResponse>

    @POST("auth/login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @GET("users/account")
    fun getAccountInfo(@Header("Authorization") authToken: String): Call<AccountInfoResponse>

    @POST("auth/verify-token")
    fun verifyToken(@Header("Authorization") authToken: String): Call<VerifyTokenResponse>

    @GET("movies/movie")
    fun getPopularMovies(@Header("Authorization") authToken: String): Call<MovieResponse>

    data class SignUpRequest(
        val firstName: String,
        val lastName: String,
        val password: String,
        val isAdmin: Boolean = false
    )

    data class SignUpResponse(
        val message: String
    )

    data class LoginRequest(
        val firstName: String,
        val password: String
    )

    data class LoginResponse(
        val token: String
    )

    data class AccountInfoResponse(
        val id: String,
        val firstName: String,
        val lastName: String,
        val createdAt: String
    )

    data class VerifyTokenResponse(
        val valid: Boolean
    )

    data class Movie(
        val id: Int,
        val title: String,
        val poster_path: String
    )
}