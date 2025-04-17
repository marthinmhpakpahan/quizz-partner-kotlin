package com.quizzpartner.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.quizzpartner.databinding.ActivityDashboardBinding

class DashboardActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("user", MODE_PRIVATE)

        binding.tvName.setText(sharedPreferences.getString("username", ""));

        binding.btnMulaiKuis.setOnClickListener {
            startActivity(Intent(this@DashboardActivity, QuizCategoryActivity::class.java))
            finish()
        }
    }
}