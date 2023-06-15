package com.example.tp2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.tp2.RetrofitClient.apiService
import com.example.tp2.databinding.ActivityLoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            val firstName = binding.etFirstName.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            apiService.login(ApiService.LoginRequest(firstName, password)).enqueue(object : Callback<ApiService.LoginResponse> {
                override fun onResponse(call: Call<ApiService.LoginResponse>, response: Response<ApiService.LoginResponse>) {
                    if (response.isSuccessful) {
                        val token = response.body()?.token
                        if (token != null) {
                            JWTToken.getInstance(applicationContext).setTokenValue(token)
                            startActivity(Intent(applicationContext, PopularMoviesActivity::class.java))
                            finish()
                            Toast.makeText(applicationContext, "Connexion réussie", Toast.LENGTH_SHORT).show() // Ajouté ici
                        }
                    } else {
                        Toast.makeText(applicationContext, "Échec de la connexion", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ApiService.LoginResponse>, t: Throwable) {
                    Toast.makeText(applicationContext, "Erreur : ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }

        binding.btnCancel.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
    }
}