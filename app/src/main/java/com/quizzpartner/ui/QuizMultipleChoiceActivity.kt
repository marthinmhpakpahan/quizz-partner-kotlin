package com.quizzpartner.ui

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.quizzpartner.R
import com.quizzpartner.data.QuizAttemptData
import com.quizzpartner.data.QuizQuestionData
import com.quizzpartner.data.QuizResultData
import com.quizzpartner.databinding.ActivityQuizMultipleChoiceBinding
import com.quizzpartner.util.SessionManager
import kotlin.random.Random

class QuizMultipleChoiceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuizMultipleChoiceBinding

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
        binding = ActivityQuizMultipleChoiceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.linearLoading.visibility = View.VISIBLE

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("user_attempts")

        maxQuestion = intent.extras?.getInt("totalQuestion") ?: 10
        quizCategory = intent.extras?.getString("quizCategory") ?: "Pilihan Ganda"
        quizTopic = intent.extras?.getString("quizTopic") ?: "Ayat Penting"

        quizResultData.totalQuestion = maxQuestion
        quizResultData.quizCategory = quizCategory
        quizResultData.quizTopic = quizTopic

        fetchRandomQuizQuestions(maxQuestion) { questions ->
            if (questions.isNotEmpty()) {
                // Pass to adapter, UI, or ViewModel
                Log.d("Quiz", "Loaded ${questions.size} questions")
                listQuestions = questions
                Log.d("QuizSize", "QuizSize : ${listQuestions?.size}")
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
                binding.tvCountdownTimer.setText("${second}")
                if(second.toInt() <= 80) {
                    setupCountdownUI("warning")
                }
                if(second.toInt() <= 30) {
                    setupCountdownUI("danger")
                }
            }

            override fun onFinish() {
                finishQuiz()
            }
        }.start()
    }

    fun setupCountdownUI(type : String) {
        if(type.equals("warning")) {
            binding.tvCountdownTimer.setBackgroundResource(R.drawable.progress_circle_warning)
            binding.lottieCircleDefault.visibility = View.GONE
            binding.lottieCircleWarning.visibility = View.VISIBLE
            binding.lottieCircleDanger.visibility = View.GONE
        } else if(type.equals("danger")) {
            binding.tvCountdownTimer.setBackgroundResource(R.drawable.progress_circle_danger)
            binding.lottieCircleDefault.visibility = View.GONE
            binding.lottieCircleWarning.visibility = View.GONE
            binding.lottieCircleDanger.visibility = View.VISIBLE
        } else {
            binding.tvCountdownTimer.setBackgroundResource(R.drawable.progress_circle)
            binding.lottieCircleDefault.visibility = View.VISIBLE
            binding.lottieCircleWarning.visibility = View.GONE
            binding.lottieCircleDanger.visibility = View.GONE
        }
    }

    fun resetButtonState() {
        val choiceButtons = arrayOf(
            binding.btnChoice1, binding.btnChoice2, binding.btnChoice3, binding.btnChoice4
        )

        for (choiceButton in choiceButtons) {
            choiceButton.setTextColor(getColor(R.color.black))
            choiceButton.setBackgroundResource(R.drawable.btn_quiz_option)
        }
    }

    fun setupQuestion() {
        resetButtonState()
        if (listQuestions != null && listQuestions?.size!! > 0) {
            var question = listQuestions?.get(indexQuestion)
            binding.tvQuestionNumber.text = "Pertanyaan ${indexQuestion + 1} / ${maxQuestion}"
            if (question != null) {
                binding.tvQuestion.text = question.question
                binding.btnChoice1.text = question.options.get(0)
                binding.btnChoice2.text = question.options.get(1)
                binding.btnChoice3.text = question.options.get(2)
                binding.btnChoice4.text = question.options.get(3)

                binding.btnChoice1.setOnClickListener {
                    validateAnswer(question.question, binding.btnChoice1.text.toString(), question.answer)
                }
                binding.btnChoice2.setOnClickListener {
                    validateAnswer(question.question, binding.btnChoice2.text.toString(), question.answer)
                }
                binding.btnChoice3.setOnClickListener {
                    validateAnswer(question.question, binding.btnChoice3.text.toString(), question.answer)
                }
                binding.btnChoice4.setOnClickListener {
                    validateAnswer(question.question, binding.btnChoice4.text.toString(), question.answer)
                }
            }
        }
        binding.linearLoading.visibility = View.GONE
    }

    fun validateAnswer(question : String, selected : String, answer : String) {
        val choiceButtons = arrayOf(
            binding.btnChoice1, binding.btnChoice2, binding.btnChoice3, binding.btnChoice4
        )

        for (choiceButton in choiceButtons) {
            val textButton = choiceButton.text.toString().lowercase()
            if (textButton.equals(selected.lowercase())) {
                choiceButton.setBackgroundResource(R.drawable.btn_quiz_option_selected)
                choiceButton.setTextColor(getColor(R.color.white))
            }
            if (textButton.equals(answer.lowercase())) {
                choiceButton.setBackgroundResource(R.drawable.btn_quiz_option_correct)
                choiceButton.setTextColor(getColor(R.color.white))
            }
            if (!textButton.equals(selected.lowercase()) && !textButton.equals(answer.lowercase())){
                choiceButton.setBackgroundResource(R.drawable.btn_quiz_option_wrong)
                choiceButton.setTextColor(getColor(R.color.white))
            }
        }

        if (selected.lowercase().equals(answer.lowercase())) {
            correctAnswer += 1
        } else {
            incorrectAnswer += 1
        }
        binding.tvScore.text = ((correctAnswer.toFloat() / maxQuestion.toFloat()) * 100).toInt().toString()

        var quizAttemptData = QuizAttemptData()
        quizAttemptData.question = question
        quizAttemptData.answered = selected
        quizAttemptData.correct_answer = answer
        listQuizAttempt?.add(quizAttemptData)

        indexQuestion += 1

        nextQuestion()
    }

    fun nextQuestion() {
        object : CountDownTimer(2000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                var second = millisUntilFinished/1000
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
        var intent = Intent(this@QuizMultipleChoiceActivity, QuizResultActivity::class.java)
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

            // Shuffle and pick 20
            Log.d("QuizActivity", "Jumlah soal : ${limit}")
            val randomQuestions = allQuestions.shuffled(Random(System.currentTimeMillis())).take(limit)
            callback(randomQuestions)
        }.addOnFailureListener {
            it.printStackTrace()
            callback(emptyList())
        }
    }

    fun saveResultToDatabase() {
        Log.d("QuizActivity.saveResultToDatabase", "registerUser invoked!")
        val id = databaseReference.push().key
        val userId = SessionManager.getSession(this@QuizMultipleChoiceActivity, "user", "id")
        quizResultData.id = id?: ""
        quizResultData.userId = userId
        databaseReference.child(id!!).setValue(quizResultData)

        Log.d("saveResultToDatabase", "listQuizAttempt size : ${listQuizAttempt!!.size.toString()}")
        for (item in listQuizAttempt!!) {
            databaseReference = firebaseDatabase.reference.child("user_attempts").child(id).child("records")
            val id = databaseReference.push().key
            Log.d("saveResultToDatabase", item.toString())
            databaseReference.child(id!!).setValue(item)
        }
    }
}