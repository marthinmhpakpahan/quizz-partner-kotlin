package com.quizzpartner.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class QuizResultData (
    var id : String = "",
    var userId: String = "",
    var quizCategory: String = "",
    var quizTopic: String = "",
    var totalQuestion: Int = 0,
    var totalCorrectAnswer: Int = 0,
    var timeNeeded: Int = 200,
    var listOfQuizAttemp : List<QuizAttemptData> = listOf()
): Parcelable