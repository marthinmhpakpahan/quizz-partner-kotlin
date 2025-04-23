package com.quizzpartner.util

object Helper {

    fun toCamelCase(sentences : String): String {
        return sentences.split("_").mapIndexed { index, word ->
            if (index == 0) {
                word.lowercase()
            } else {
                word.replaceFirstChar { it.uppercase() }
            }
        }.joinToString("")
    }

}