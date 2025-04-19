package com.quizzpartner.data

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class QuizAttemptData (
    var question : String = "",
    var answered : String = "",
    var correct_answer : String = ""
): Parcelable