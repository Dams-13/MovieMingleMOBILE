package com.example.tp2

import android.content.Context
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.example.tp2.databinding.ActivityHomeBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var jwtToken: JWTToken

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        jwtToken = JWTToken.getInstance(this)

        binding.btnSignUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.fabUserAccount.setOnClickListener {
            if (jwtToken.isValid()) {
                getAccountInfoAndUpdateJwtToken()
            } else {
                showLoginSignUpDialog()
            }
        }

        binding.buttonLogout.setOnClickListener {
            if (jwtToken.isValid()) {
                // Supprimer le token JWT
                val sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE)
                sharedPreferences.edit().remove("token").apply()

                // Mettre à jour le JWTToken singleton
                jwtToken.clearToken()

                // Afficher un Toast pour indiquer la réussite de la déconnexion
                Toast.makeText(this, "Vous êtes bien déconnecté", Toast.LENGTH_SHORT).show()

                // Mettre à jour les boutons "Déconnexion" et "Films populaires"
                updateUI()
            } else {
                // Afficher un Toast pour indiquer que l'utilisateur est déjà déconnecté
                Toast.makeText(this, "Vous êtes déjà déconnecté", Toast.LENGTH_SHORT).show()
            }
        }
        updateUI()

        // Ajout du OnClickListener pour le bouton Films populaires
        binding.btnPopularMovies.setOnClickListener {
            val intent = Intent(this, PopularMoviesActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        updateUI()
    }

    private fun updateUI() {
        if (jwtToken.isValid()) {
            binding.btnPopularMovies.visibility = View.VISIBLE
            binding.buttonLogout.visibility = View.VISIBLE
        } else {
            binding.btnPopularMovies.visibility = View.GONE
            binding.buttonLogout.visibility = View.GONE
        }
    }

    private fun showLoginSignUpDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Veuillez vous connecter ou vous inscrire")
        builder.setPositiveButton("Connexion") { _, _ ->
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        builder.setNegativeButton("Inscription") { _, _ ->
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
        builder.setNeutralButton("Annuler", null)
        builder.create().show()
    }

    private fun getAccountInfoAndUpdateJwtToken() {
        val apiService = RetrofitClient.apiService
        val call = apiService.getAccountInfo(jwtToken.token)
        call.enqueue(object : Callback<ApiService.AccountInfoResponse> {
            override fun onResponse(
                call: Call<ApiService.AccountInfoResponse>,
                response: Response<ApiService.AccountInfoResponse>
            ) {
                if (response.isSuccessful) {
                    val accountInfo = response.body()
                    if (accountInfo != null) {
                        jwtToken.firstName = accountInfo.firstName
                        jwtToken.lastName = accountInfo.lastName
                        jwtToken.createdAt = accountInfo.createdAt
                        val intent = Intent(this@HomeActivity, AccountDetailsActivity::class.java)
                        startActivity(intent)
                    }
                } else {
                    val errorMessage = when (response.code()) {
                        401 -> "Accès non autorisé. Veuillez vous reconnecter."
                        404 -> "Informations du compte introuvables."
                        else -> "Erreur lors de la récupération des informations du compte."
                    }
                    Toast.makeText(this@HomeActivity, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ApiService.AccountInfoResponse>, t: Throwable) {
                val networkErrorMessage = "Erreur de connexion. Veuillez vérifier votre connexion Internet et réessayer."
                Toast.makeText(this@HomeActivity, networkErrorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }
}