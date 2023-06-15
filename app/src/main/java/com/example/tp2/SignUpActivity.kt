package com.example.tp2

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tp2.ApiService.SignUpRequest
import com.example.tp2.databinding.ActivitySignUpBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegister.setOnClickListener {
            val firstName = binding.etFirstName.text.toString()
            val lastName = binding.etLastName.text.toString()
            val password = binding.etPassword.text.toString()

            if (firstName.isBlank() || lastName.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Tous les champs sont requis", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.length < 5) {
                Toast.makeText(this, "mot de passe trop court, 5 caractères minimum", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val signUpRequest = SignUpRequest(firstName, lastName, password)
            RetrofitClient.apiService.signUp(signUpRequest).enqueue(object : Callback<ApiService.SignUpResponse> {
                override fun onResponse(call: Call<ApiService.SignUpResponse>, response: Response<ApiService.SignUpResponse>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@SignUpActivity, "Inscription réussie", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this@SignUpActivity, "Erreur lors de l'inscription", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ApiService.SignUpResponse>, t: Throwable) {
                    Toast.makeText(this@SignUpActivity, "Erreur réseau", Toast.LENGTH_SHORT).show()
                }
            })
        }

        binding.btnCancel.setOnClickListener {
            finish()
        }
    }
}