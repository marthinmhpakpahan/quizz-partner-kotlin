package com.quizzpartner.ui

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.quizzpartner.R
import com.quizzpartner.data.QuizAttemptData
import com.quizzpartner.data.QuizQuestionData
import com.quizzpartner.data.QuizResultData
import com.quizzpartner.databinding.ActivityQuizFillInWordBinding
import com.quizzpartner.databinding.ActivityQuizTrueFalseBinding
import com.quizzpartner.util.SessionManager
import kotlin.random.Random

class QuizFillInWordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuizFillInWordBinding

    var indexQuestion = 0
    var maxQuestion = 0
    var correctAnswer = 0
    var incorrectAnswer = 0
    var quizCategory = ""
    var quizTopic = ""
    var listQuestions : List<QuizQuestionData>? = emptyList()
    var listQuizAttempt : MutableList<QuizAttemptData>? = mutableListOf()
    var quizResultData : QuizResultData = QuizResultData()

    var timer = 200

    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    var remainingTimeInMillis: Long = timer.toLong() * 1000
    var countDownTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityQuizFillInWordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("user_attempts")

        maxQuestion = intent.extras?.getInt("totalQuestion") ?: 10
        quizCategory = intent.extras?.getString("quizCategory") ?: "Benar atau Salah"
        quizTopic = intent.extras?.getString("quizTopic") ?: "Ayat Penting"

        Log.e("QuizTrueFalseActivity", quizTopic)
        Log.e("QuizTrueFalseActivity", quizCategory)

        quizResultData.totalQuestion = maxQuestion
        quizResultData.quizCategory = quizCategory
        quizResultData.quizTopic = quizTopic

        fetchRandomQuizQuestions(maxQuestion) { questions ->
            Log.d("QuizTrueFalseActivity", questions.toString())
            if (questions.isNotEmpty()) {
                // Pass to adapter, UI, or ViewModel
                Log.d("QuizTrueFalseActivity - Quiz", "Loaded ${questions.size} questions")
                listQuestions = questions
                Log.d("QuizTrueFalseActivity - QuizSize", "QuizSize : ${listQuestions?.size}")
                setupQuestion()
            } else {
                Toast.makeText(this, "Gagal memuat soal.", Toast.LENGTH_SHORT).show()
            }
        }

        startTimer(remainingTimeInMillis)
    }

    fun startTimer(duration: Long) {
        countDownTimer?.cancel()
        countDownTimer = object : CountDownTimer(duration, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                remainingTimeInMillis = millisUntilFinished
                val seconds = millisUntilFinished / 1000
                binding.tvCountdownTimer.text = "Sisa Waktu : $seconds"
                quizResultData.timeNeeded = timer - seconds.toInt()
            }

            override fun onFinish() {
                finishQuiz()
            }
        }.start()
    }

    fun resetButtonState() {
        binding.llClueAnswer.visibility = View.GONE
        binding.etAnswer.setText("")
        binding.llETAnswer.backgroundTintList = null
        binding.llETAnswer.setBackgroundResource(R.drawable.edittext_border)
        binding.etAnswer.backgroundTintList = getColorStateList(R.color.white)
        binding.etAnswer.setTextColor(getColor(R.color.black))
        binding.tvNextQuestionCaption.visibility = View.INVISIBLE
    }

    fun setupHint() {
        remainingTimeInMillis -= 5000
        startTimer(remainingTimeInMillis)
        val hintItems = listOf(
            binding.tvHint1, binding.tvHint2, binding.tvHint3
        )
        for((index, tvHint) in hintItems.withIndex()) {
            tvHint.setText(listQuestions?.get(indexQuestion)?.options?.get(index) ?: "")
        }
        binding.llClueAnswer.visibility = View.VISIBLE
    }

    fun setupQuestion() {
        resetButtonState()
        if (listQuestions != null && listQuestions?.size!! > 0) {
            var question = listQuestions?.get(indexQuestion)
            binding.tvQuestionNumber.text = "Pertanyaan ${indexQuestion + 1} / ${maxQuestion}"
            if (question != null) {
                Log.d("QuizTrueFalseActivity - Question", question.question.toString())
                Log.d("QuizTrueFalseActivity - Answer", question.answer.toString())
                binding.tvQuestion.text = question.question

                binding.btnSubmitAnswer.setOnClickListener {
                    validateAnswer(question.question, binding.etAnswer.text.toString(), question.answer)
                }
            } else {
                Toast.makeText(this@QuizFillInWordActivity, "Null question!", Toast.LENGTH_SHORT).show()
            }
        }
        binding.loadingLayout.visibility = View.GONE
        binding.imgShowClue.setOnClickListener {
            setupHint()
        }
    }

    fun validateAnswer(question : String, selected : String, answer : String) {
        if (selected.lowercase().equals(answer.lowercase())) {
            binding.etAnswer.backgroundTintList = getColorStateList(R.color.accentGreen)
            binding.llETAnswer.backgroundTintList = getColorStateList(R.color.accentGreen)
            binding.etAnswer.setTextColor(getColor(R.color.white))
            correctAnswer += 1
            binding.tvTotalCorrectAnswer.text = correctAnswer.toString()
        } else {
            binding.etAnswer.backgroundTintList = getColorStateList(R.color.accentRed)
            binding.llETAnswer.backgroundTintList = getColorStateList(R.color.accentRed)
            binding.etAnswer.setTextColor(getColor(R.color.white))
            incorrectAnswer += 1
            binding.tvTotalIncorrectAnswer.text = incorrectAnswer.toString()
        }

        var quizAttemptData = QuizAttemptData()
        quizAttemptData.question = question
        quizAttemptData.answered = selected
        quizAttemptData.correct_answer = answer
        listQuizAttempt?.add(quizAttemptData)

        indexQuestion += 1

        nextQuestion()
    }

    fun nextQuestion() {
        binding.tvNextQuestionCaption.visibility = View.VISIBLE
        object : CountDownTimer(2000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                var second = millisUntilFinished/1000
                binding.tvNextQuestionCaption.setText("Pertanyaan selanjutnya dalam ${second} detik...")
            }

            override fun onFinish() {
                if (indexQuestion == maxQuestion) {
                    finishQuiz()
                }
                setupQuestion()
            }
        }.start()
    }

    fun finishQuiz() {
        quizResultData.totalCorrectAnswer = correctAnswer
        saveResultToDatabase()
        var intent = Intent(this@QuizFillInWordActivity, QuizResultActivity::class.java)
        intent.putExtra("QuizResultData", quizResultData)
        startActivity(intent)
        finish()
        return
    }

    fun fetchRandomQuizQuestions(limit: Int, callback: (List<QuizQuestionData>) -> Unit) {
        val database = FirebaseDatabase.getInstance().reference
        val quizRef = database.child("questions/" + quizCategory + "/" + quizTopic)

        quizRef.get().addOnSuccessListener { dataSnapshot ->
            val allQuestions = mutableListOf<QuizQuestionData>()

            for (questionSnapshot in dataSnapshot.children) {
                val question = questionSnapshot.getValue(QuizQuestionData::class.java)
                if (question != null) {
                    allQuestions.add(question)
                }
            }

            Log.d("QuizTrueFalseActivity.fetchRandomQuizQuestions", allQuestions.toString())

            // Shuffle and pick 20
            Log.d("QuizTrueFalseActivity", "Jumlah soal : ${limit}")
            val randomQuestions = allQuestions.shuffled(Random(System.currentTimeMillis())).take(limit)
            callback(randomQuestions)
        }.addOnFailureListener {
            it.printStackTrace()
            callback(emptyList())
        }
    }

    fun saveResultToDatabase() {
        Log.d("QuizTrueFalseActivity.saveResultToDatabase", "registerUser invoked!")
        val id = databaseReference.push().key
        val userId = SessionManager.getSession(this@QuizFillInWordActivity, "user", "id")
        quizResultData.id = id?: ""
        quizResultData.userId = userId
        databaseReference.child(id!!).setValue(quizResultData)

        Log.d("QuizTrueFalseActivity.saveResultToDatabase", "listQuizAttempt size : ${listQuizAttempt!!.size.toString()}")
        for (item in listQuizAttempt!!) {
            databaseReference = firebaseDatabase.reference.child("user_attempts").child(id).child("records")
            val id = databaseReference.push().key
            Log.d("QuizTrueFalseActivity.saveResultToDatabase", item.toString())
            databaseReference.child(id!!).setValue(item)
        }
    }
}