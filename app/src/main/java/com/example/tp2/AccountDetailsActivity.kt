package com.example.tp2
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tp2.databinding.ActivityAccountDetailsBinding
import java.text.SimpleDateFormat
import java.util.*

class AccountDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAccountDetailsBinding
    private val jwtToken = JWTToken.getInstance(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (jwtToken.isValid()) {
            // Utilisez l'objet JWTToken pour récupérer les informations du compte utilisateur et les afficher
            binding.tvFirstName.text = "Prénom: ${jwtToken.firstName}"
            binding.tvLastName.text = "Nom: ${jwtToken.lastName}"

            // Convertir la date de création en format JOUR/MOIS/ANNEE
            val originalFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
            originalFormat.timeZone = TimeZone.getTimeZone("UTC")
            val targetFormat = SimpleDateFormat("dd/MM/yyyy", Locale.US)
            targetFormat.timeZone = TimeZone.getTimeZone("Europe/Paris")
            val date = originalFormat.parse(jwtToken.createdAt)
            val formattedDate = targetFormat.format(date)

            binding.tvCreatedAt.text = "Date de création: $formattedDate"
        } else {
            finish()
        }
    }
}