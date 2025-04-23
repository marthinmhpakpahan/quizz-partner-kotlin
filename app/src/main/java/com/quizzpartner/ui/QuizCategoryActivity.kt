package com.quizzpartner.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.quizzpartner.R
import com.quizzpartner.databinding.ActivityQuizCategoryBinding

class QuizCategoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuizCategoryBinding
    private var jumlahSoal = 10
    private var categorySoal = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityQuizCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        onClickListener()
    }

    private fun onClickListener() {
        binding.btnPerjanjianLama.setOnClickListener {
            clickCategory(binding.btnPerjanjianLama.tag.toString())
        }
        binding.btnPerjanjianBaru.setOnClickListener {
            clickCategory(binding.btnPerjanjianBaru.tag.toString())
        }
        binding.btnTokohAlkitab.setOnClickListener {
            clickCategory(binding.btnTokohAlkitab.tag.toString())
        }
        binding.btnAyatPenting.setOnClickListener {
            clickCategory(binding.btnAyatPenting.tag.toString())
        }
        binding.btnKuisAcak.setOnClickListener {
            clickCategory(binding.btnKuisAcak.tag.toString())
        }

        binding.btnJumlah10.setOnClickListener {
            clickTotalQuizz(binding.btnJumlah10.text.toString())
        }
        binding.btnJumlah20.setOnClickListener {
            clickTotalQuizz(binding.btnJumlah20.text.toString())
        }
        binding.btnJumlah30.setOnClickListener {
            clickTotalQuizz(binding.btnJumlah30.text.toString())
        }
        binding.btnJumlah40.setOnClickListener {
            clickTotalQuizz(binding.btnJumlah40.text.toString())
        }

        binding.btnMulai.setOnClickListener {
            val intent = Intent(this@QuizCategoryActivity, QuizActivity::class.java)
            intent.putExtra("totalQuestion", jumlahSoal)
            intent.putExtra("quizCategory", categorySoal)
            startActivity(intent)
        }
    }

    private fun clickTotalQuizz(total : String) {
        val arrButton = arrayOf(binding.btnJumlah10, binding.btnJumlah20, binding.btnJumlah30, binding.btnJumlah40)
        for (button in arrButton) {
            if (button.text.toString().equals(total)) {
                button.setBackgroundColor(getColor(R.color.accentGreen))
                button.setTextColor(getColor(R.color.white))
                jumlahSoal = button.text.toString().toInt()
                binding.tvJumlahSoal.text = "Jumlah Soal : ${button.text.toString()}"
            } else {
                button.setBackgroundColor(getColor(R.color.white))
                button.setTextColor(getColor(R.color.black))
            }
        }
    }

    private fun clickCategory(category : String) {
        val arrButton = arrayOf(binding.btnPerjanjianLama, binding.btnPerjanjianBaru, binding.btnTokohAlkitab, binding.btnAyatPenting, binding.btnKuisAcak)
        for (button in arrButton) {
            if (button.tag.equals(category)) {
                button.setBackgroundColor(getColor(R.color.accentGreen))
                button.setTextColor(getColor(R.color.white))
                categorySoal = button.tag.toString()
            } else {
                button.setBackgroundColor(getColor(R.color.white))
                button.setTextColor(getColor(R.color.black))
            }
        }
    }
}