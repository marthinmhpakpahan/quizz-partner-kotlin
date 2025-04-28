package com.quizzpartner.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.quizzpartner.R
import com.quizzpartner.databinding.ActivityQuizCategoryBinding

class QuizCategoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuizCategoryBinding
    private var jumlahSoal = 10
    private var quizCategory = ""
    private var quizTopic = ""

    private var btnCategoryClicked = false
    private var btnTopicClicked = false
    private var btnTotalClicked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityQuizCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        onClickListener()
    }

    private fun onClickListener() {
        binding.llMultipleChoice.setOnClickListener {
            clickCategory(binding.llMultipleChoice.tag.toString())
        }
        binding.llTrueFalse.setOnClickListener {
            clickCategory(binding.llTrueFalse.tag.toString())
        }
        binding.llFillInWord.setOnClickListener {
            clickCategory(binding.llFillInWord.tag.toString())
        }
        binding.llRandom.setOnClickListener {
            clickCategory(binding.llRandom.tag.toString())
        }

        binding.btnAyatPenting.setOnClickListener {
            clickTopicQuizz(binding.btnAyatPenting.text.toString())
        }

        binding.btnTokohAlkitab.setOnClickListener {
            clickTopicQuizz(binding.btnTokohAlkitab.text.toString())
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
            if (validateStartQuizButton()) {
                val intent = Intent(this@QuizCategoryActivity, QuizMultipleChoiceActivity::class.java)
                intent.putExtra("totalQuestion", jumlahSoal)
                intent.putExtra("quizCategory", quizCategory)
                intent.putExtra("quizTopic", quizTopic)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Pilih Kategori, topik dan Total soal terlebih dahulu!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateStartQuizButton(): Boolean {
        if(btnCategoryClicked && btnTopicClicked && btnTotalClicked) {
            binding.btnMulai.setBackgroundColor(getColor(R.color.accentGreen))
            return true
        }
        binding.btnMulai.setBackgroundColor(getColor(R.color.gray700))
        return false
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
        btnTotalClicked = true
        validateStartQuizButton()
    }

    private fun clickTopicQuizz(topic : String) {
        val arrButton = arrayOf(binding.btnAyatPenting, binding.btnTokohAlkitab)
        for (button in arrButton) {
            if (button.text.toString().equals(topic)) {
                button.setBackgroundColor(getColor(R.color.accentGreen))
                button.setTextColor(getColor(R.color.white))
            } else {
                button.setBackgroundColor(getColor(R.color.white))
                button.setTextColor(getColor(R.color.black))
            }
        }
        quizTopic = topic
        btnTopicClicked = true
        validateStartQuizButton()
    }

    private fun clickCategory(category : String) {
        val arrCategoryElements = arrayOf(binding.llMultipleChoice, binding.llTrueFalse, binding.llFillInWord, binding.llRandom)
        val arrTextViewElements = arrayOf(binding.tvllMultipleChoice, binding.tvllTrueFalse, binding.tvllFillInWord, binding.tvllRandom)
        for ((index, element) in arrCategoryElements.withIndex()) {
            if(element.tag.equals(category)) {
                element.setBackgroundResource(R.drawable.rounded_button_selected)
                arrTextViewElements.get(index).setTextColor(getColor(R.color.white))
            } else {
                element.setBackgroundResource(R.drawable.rounded_button)
                arrTextViewElements.get(index).setTextColor(getColor(R.color.black))
            }
        }
        btnCategoryClicked = true
        quizCategory = category
        validateStartQuizButton()
    }
}