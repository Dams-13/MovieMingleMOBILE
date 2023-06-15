package com.example.tp2

import com.google.gson.annotations.SerializedName

data class MovieResponse(
    @SerializedName("movies") val movies: MovieList,
    @SerializedName("totalMovies") val totalMovies: Int
)

data class MovieList(
    @SerializedName("movies") val movies: List<ApiService.Movie>,
    @SerializedName("count") val count: Int,
    @SerializedName("totalPages") val totalPages: Int,
    @SerializedName("currentPage") val currentPage: Int
)