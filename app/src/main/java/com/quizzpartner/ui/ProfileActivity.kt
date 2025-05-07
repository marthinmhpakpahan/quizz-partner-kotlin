package com.quizzpartner.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.quizzpartner.R
import com.quizzpartner.databinding.ActivityLoginBinding
import com.quizzpartner.databinding.ActivityProfileBinding
import com.quizzpartner.databinding.ActivityQuizResultBinding
import com.quizzpartner.util.SessionManager

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val _user_id = SessionManager.getSession(this, "user", "id")
        val _fullname = SessionManager.getSession(this, "user", "fullname")
        val _email = SessionManager.getSession(this, "user", "email")
        val _username = SessionManager.getSession(this, "user", "username")

        binding.tvFullname.text = _fullname
        binding.tvEmail.text = _email
        binding.tvUsername.text = _username

        binding.btnLogout.setOnClickListener {
            SessionManager.deleteSession(this, "user")
            startActivity(Intent(this@ProfileActivity, MainActivity::class.java))
            finish()
        }
    }
}