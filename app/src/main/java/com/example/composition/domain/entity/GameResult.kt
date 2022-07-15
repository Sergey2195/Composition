package com.example.composition.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class GameResult(
    val winner: Boolean,
    val countOfRightAnswers: Int,
    val countOfQuestions: Int,
    val gameSettings: GameSettings
): Parcelable{
    val countOfRightAnswersString
        get() = countOfRightAnswers.toString()
    val countOfQuestionsString
        get() = countOfQuestions.toString()
}