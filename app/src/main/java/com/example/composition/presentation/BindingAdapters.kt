package com.example.composition.presentation

import android.content.Context
import android.content.res.ColorStateList
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.example.composition.R
import com.example.composition.domain.entity.GameResult
import com.example.composition.domain.entity.Question

@BindingAdapter("require_answers")
fun requireAnswers(textView: TextView, count: Int){
    textView.text = String.format(textView.context.getString(R.string.require_answers), count)
}

@BindingAdapter("win_or_loose")
fun winOrLoose(textView: TextView, res: Boolean){
    textView.text = if (res){
        "Победа"
    }else {
        "Поражение :("
    }
}

@BindingAdapter("user_score")
fun userScore(textView: TextView, score: Int){
    textView.text = String.format(textView.context.getString(R.string.user_score), score)
}

@BindingAdapter("need_percent")
fun needPercent(textView: TextView, percent: Int){
    textView.text = String.format(textView.context.getString(R.string.need_percent_of_right_answers), percent)
}

@BindingAdapter("user_percent")
fun userPercent(textView: TextView, gameResult: GameResult){
    val totalQuestion = gameResult.countOfQuestions
    val rightAnswers = gameResult.countOfRightAnswers
    val percent: Int = if (totalQuestion == 0){
        0
    }else {
        (rightAnswers * 100 / totalQuestion.toDouble()).toInt()
    }
    textView.text = String.format(textView.context.getString(R.string.user_percent_result), percent)
}

@BindingAdapter("enoughCount")
fun bindEnoughCount(textView: TextView, enough: Boolean){
    textView.setTextColor(getColorByState(enough, textView.context))
}

@BindingAdapter("enoughPercent")
fun bindEnoughPercent (progressBar: ProgressBar, enough: Boolean){
    val color = getColorByState(enough, progressBar.context)
    progressBar.progressTintList = ColorStateList.valueOf(color)
}

@BindingAdapter("numToString")
fun numToString(textView: TextView, num:Int){
    textView.text = num.toString()
}

private fun getColorByState(state: Boolean, context: Context): Int{
    return  ContextCompat.getColor(context,
        if (state){
            android.R.color.holo_green_light
        }else {
            android.R.color.holo_red_light
        }
    )
}