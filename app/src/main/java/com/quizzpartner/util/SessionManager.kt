package com.quizzpartner.util

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE

object SessionManager {

    private lateinit var sharedPreferences: SharedPreferences

    fun getSession(context : Context, sharedName : String, key: String) : String {
        sharedPreferences = context.getSharedPreferences(sharedName, MODE_PRIVATE)
        return sharedPreferences.getString(key, "").toString()
    }
}