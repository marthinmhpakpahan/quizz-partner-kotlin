package com.quizzpartner.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class QuizItem (
    var category: String = "",
    var topic: String = "",
    var time: Int = 200,
    var totalQuestion: Int = 10
): Parcelable