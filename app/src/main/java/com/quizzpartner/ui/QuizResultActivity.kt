package com.quizzpartner.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.quizzpartner.R
import com.quizzpartner.data.QuizResultData
import com.quizzpartner.databinding.ActivityQuizBinding
import com.quizzpartner.databinding.ActivityQuizResultBinding
import com.quizzpartner.util.Helper

class QuizResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuizResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityQuizResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val quizResultData = intent.getParcelableExtra<QuizResultData>("QuizResultData")

        var totalQuestion = quizResultData?.totalQuestion.toString().toInt()
        var totalCorrectAnswer = quizResultData?.totalCorrectAnswer.toString().toInt()
        var totalScore = (totalCorrectAnswer.toFloat()/totalQuestion.toFloat()) * 100
        binding.tvQuizCategory.text = quizResultData?.quizCategory.toString()
        binding.tvQuizTopic.text = quizResultData?.quizTopic.toString()
        binding.tvCorrectAnswer.text = totalCorrectAnswer.toString() + " Benar"
        binding.tvWrongAnswer.text = (totalQuestion - totalCorrectAnswer).toString() + " Salah"
        binding.tvTotalQueztion.text = totalQuestion.toString() + " Pertanyaan"
        binding.tvScore.text = totalScore.toInt().toString()
        binding.tvTimeNeeded.text = quizResultData?.timeNeeded.toString() + " Detik"

        binding.btnDashboard.setOnClickListener {
            startActivity(Intent(this@QuizResultActivity, DashboardActivity::class.java))
            finish()
        }
    }
}