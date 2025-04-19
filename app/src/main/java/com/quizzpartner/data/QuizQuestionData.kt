package com.quizzpartner.data

data class QuizQuestionData (
    val question: String = "",
    val options: List<String> = listOf(),
    val answer: String = "",
    val category: String = ""
)