package com.example.tp2

import android.content.Context
import android.content.SharedPreferences

class JWTToken private constructor(context: Context) {
    private val pref: SharedPreferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
    var token: String = pref.getString("token", "") ?: ""
    var firstName: String? = null
    var lastName: String? = null
    var createdAt: String? = null

    fun setTokenValue(newToken: String) {
        token = if (newToken.isNotEmpty()) {
            "Bearer $newToken"
        } else {
            ""
        }
        with(pref.edit()) {
            putString("token", token)
            apply()
        }
    }

    fun isValid(): Boolean {
        return token.isNotEmpty() && token != "Bearer "
    }

    fun clearToken() {
        token = ""
        firstName = ""
        lastName = ""
        createdAt = ""
    }

    companion object {
        @Volatile
        private var instance: JWTToken? = null

        fun getInstance(context: Context): JWTToken {
            return instance ?: synchronized(this) {
                instance ?: JWTToken(context.applicationContext).also { instance = it }
            }
        }
    }
}