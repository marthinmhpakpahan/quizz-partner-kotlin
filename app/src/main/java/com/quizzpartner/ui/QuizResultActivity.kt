package com.quizzpartner.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.quizzpartner.R
import com.quizzpartner.data.QuizResultData
import com.quizzpartner.databinding.ActivityQuizBinding
import com.quizzpartner.databinding.ActivityQuizResultBinding

class QuizResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuizResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityQuizResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val quizResultData = intent.getParcelableExtra<QuizResultData>("QuizResultData")

        binding.tvQuizCategory.text = quizResultData?.quizCategory
        binding.tvCorrectAnswer.text = quizResultData?.totalCorrectAnswer.toString()
        binding.tvTotalQueztion.text = quizResultData?.totalQuestion.toString()
    }
}