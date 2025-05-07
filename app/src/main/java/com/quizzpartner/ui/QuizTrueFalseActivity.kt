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
import com.quizzpartner.databinding.ActivityQuizBinding
import com.quizzpartner.databinding.ActivityQuizTrueFalseBinding
import com.quizzpartner.util.SessionManager
import kotlin.random.Random

class QuizTrueFalseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuizTrueFalseBinding

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityQuizTrueFalseBinding.inflate(layoutInflater)
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

        // SET TIMER
        object : CountDownTimer(timer.toLong() * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                var second = millisUntilFinished/1000
                quizResultData.timeNeeded = timer - second.toInt()
                binding.tvCountdownTimer.setText("Sisa Waktu : ${second}")
            }

            override fun onFinish() {
                finishQuiz()
            }
        }.start()
    }

    fun resetButtonState() {
        val choiceButtons = listOf(
            binding.llCorrectChoice, binding.llWrongtChoice
        )
        val choiceButtonTexts = listOf(
            binding.tvCorrectChoice, binding.tvWrongChoice
        )

        for ((index, choiceButton) in choiceButtons.withIndex()) {
            choiceButton.backgroundTintList = getColorStateList(R.color.white)
            choiceButton.setBackgroundResource(R.drawable.rounded_button)
            choiceButtonTexts.get(index).setTextColor(getColor(R.color.black))
        }
        binding.imgCorrectChoice.setImageDrawable(getDrawable(R.drawable.ic_true_black))
        binding.imgWrongChoice.setImageDrawable(getDrawable(R.drawable.ic_false_black))

        binding.tvNextQuestionCaption.visibility = View.INVISIBLE
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

                binding.llWrongtChoice.setOnClickListener {
                    validateAnswer(question.question, binding.tvWrongChoice.text.toString(), question.answer)
                }
                binding.llCorrectChoice.setOnClickListener {
                    validateAnswer(question.question, binding.tvCorrectChoice.text.toString(), question.answer)
                }
            } else {
                Toast.makeText(this@QuizTrueFalseActivity, "Null question!", Toast.LENGTH_SHORT).show()
            }
        }
        binding.loadingLayout.visibility = View.GONE
    }

    fun validateAnswer(question : String, selected : String, answer : String) {
        val choiceButtons = listOf(
            binding.llCorrectChoice, binding.llWrongtChoice
        )
        val choiceButtonTexts = listOf(
            binding.tvCorrectChoice, binding.tvWrongChoice
        )
        val choiceButtonImages = listOf(
            binding.imgCorrectChoice, binding.imgWrongChoice
        )
        val choiceButtonDrawables = listOf(
            R.drawable.ic_true_white, R.drawable.ic_false_white
        )

        for ((index,choiceButton) in choiceButtons.withIndex()) {
            val textButton = choiceButtonTexts.get(index).text.toString().lowercase()
            Log.e("QuizTrueFalseActivity.validateAnswer", textButton + " => " + answer)
            if (textButton.equals(answer.lowercase())) {
                choiceButton.backgroundTintList = getColorStateList(R.color.accentGreen)
                choiceButtonTexts.get(index).setTextColor(getColor(R.color.white))
                choiceButtonImages.get(index).setImageResource(choiceButtonDrawables.get(index))
            }
            if (!textButton.equals(answer.lowercase())){
                choiceButton.backgroundTintList = getColorStateList(R.color.accentRed)
                choiceButtonTexts.get(index).setTextColor(getColor(R.color.white))
                choiceButtonImages.get(index).setImageResource(choiceButtonDrawables.get(index))
            }
        }

        if (selected.lowercase().equals(answer.lowercase())) {
            correctAnswer += 1
            binding.tvTotalCorrectAnswer.text = correctAnswer.toString()
        } else {
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
        var intent = Intent(this@QuizTrueFalseActivity, QuizResultActivity::class.java)
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
        val userId = SessionManager.getSession(this@QuizTrueFalseActivity, "user", "id")
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