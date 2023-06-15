package com.example.tp2

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tp2.RetrofitClient.apiService
import com.example.tp2.databinding.ActivityPopularMoviesBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PopularMoviesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPopularMoviesBinding
    private lateinit var adapter: MovieAdapter
    private val jwtToken = JWTToken.getInstance(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPopularMoviesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = MovieAdapter()
        binding.recyclerViewMovies.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewMovies.adapter = adapter

        if (jwtToken.isValid()) {
            loadPopularMovies()
        } else {
            Toast.makeText(applicationContext, "Erreur : Token invalide", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadPopularMovies() {
        apiService.getPopularMovies(jwtToken.token).enqueue(object : Callback<MovieResponse> {
            override fun onResponse(
                call: Call<MovieResponse>,
                response: Response<MovieResponse>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { movieResponse ->
                        adapter.setMovies(movieResponse.movies.movies)
                        adapter.notifyDataSetChanged()
                    }
                } else {
                    Toast.makeText(this@PopularMoviesActivity, "Erreur lors de la récupération des films populaires", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                Toast.makeText(this@PopularMoviesActivity, "Erreur lors de la connexion au serveur", Toast.LENGTH_SHORT).show()
            }
        })
    }
}