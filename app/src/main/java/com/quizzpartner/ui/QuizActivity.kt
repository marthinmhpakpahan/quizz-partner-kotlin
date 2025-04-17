package com.quizzpartner.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.quizzpartner.R
import com.quizzpartner.databinding.ActivityQuizBinding
import com.quizzpartner.databinding.ActivityQuizCategoryBinding

class QuizActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuizBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}