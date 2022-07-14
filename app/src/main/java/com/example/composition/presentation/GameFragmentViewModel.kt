package com.example.composition.presentation

import android.app.Application
import android.os.CountDownTimer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.composition.R
import com.example.composition.data.GameRepositoryImpl
import com.example.composition.domain.entity.GameResult
import com.example.composition.domain.entity.GameSettings
import com.example.composition.domain.entity.Level
import com.example.composition.domain.entity.Question
import com.example.composition.domain.usecases.GenerateQuestionUseCase
import com.example.composition.domain.usecases.GetGameSettingsUseCase

class GameFragmentViewModel(application: Application): AndroidViewModel(application) {
    private lateinit var incLevel: Level
    private lateinit var gameSettings: GameSettings
    private val _formattedTime = MutableLiveData<String>()
    private val context = application
    val formattedTime: LiveData<String>
        get() = _formattedTime
    private val repository = GameRepositoryImpl
    private val generateQuestionUseCase = GenerateQuestionUseCase(repository)
    private val getGameSettingsUseCase = GetGameSettingsUseCase(repository)
    private lateinit var timer: CountDownTimer
    private val _question = MutableLiveData<Question>()
    private var rightAnswers = 0
    private var countOfQuestions = 0
    private val _percentOfRightAnswers = MutableLiveData<Int>()
    val percentOfRightAnswers: LiveData<Int>
        get() = _percentOfRightAnswers
    val question: LiveData<Question>
        get() = _question
    private val _progressAnswers = MutableLiveData<String>()
    val progressAnswers: LiveData<String>
        get() = _progressAnswers

    private val _enoughCountOfRightAnswers = MutableLiveData<Boolean>()
    val enoughCountOfRightAnswers: LiveData<Boolean>
        get() = _enoughCountOfRightAnswers

    private val _enoughPercentOfRightAnswers = MutableLiveData<Boolean>()
    val enoughPercentOfRightAnswers: LiveData<Boolean>
        get() = _enoughPercentOfRightAnswers

    private val _minPercent = MutableLiveData<Int>()
    val minPercent: LiveData<Int>
        get() = _minPercent

    private val _gameResult = MutableLiveData<GameResult>()
    val gameResult: LiveData<GameResult>
        get() = _gameResult

    fun startGame(level: Level){
        incLevel = level
        gameSettings = getGameSettingsUseCase(incLevel)
        _minPercent.value = gameSettings.minPercentOfRightAnswers
        startTimer()
        generateQuestion()
    }

    fun chooseAnswer(answer: Int){
        checkAnswer(answer)
        generateQuestion()
        updateProgress()
    }

    private fun checkAnswer(answer: Int){
        val rightAnswer = _question.value?.rightAnswer
        if (rightAnswer == answer){
            rightAnswers++
        }
        countOfQuestions++
    }

    private fun updateProgress(){
        val percent = calculatePercentOfRightAnswers()
        _percentOfRightAnswers.value = percent
        _progressAnswers.value = String.format(
            context.resources.getString(R.string.progress_answers),
            rightAnswers,
            gameSettings.minCountOfRightAnswers
        )
        _enoughPercentOfRightAnswers.value = percent >= gameSettings.minPercentOfRightAnswers
        _enoughCountOfRightAnswers.value = rightAnswers >= gameSettings.minCountOfRightAnswers
    }

    private fun calculatePercentOfRightAnswers(): Int{
        return ((rightAnswers/ countOfQuestions.toDouble())*100).toInt()
    }

    private fun formatTime(millSec: Long): String {
        val second = millSec / 1000
        val minutes = second / 60
        val leftSeconds = second - (minutes * 60)
        return String.format("%02d:%02d", minutes, leftSeconds)
    }

    private fun startTimer(){
         timer = object : CountDownTimer((gameSettings.gameTimeInSeconds * 1000).toLong(), 1000){
            override fun onTick(p0: Long) {
                _formattedTime.value = formatTime(p0)
            }

            override fun onFinish() {
                finishGame()
            }
        }
        timer.start()
    }
    private fun finishGame(){
        _gameResult.value = GameResult(
            enoughCountOfRightAnswers.value == true && enoughPercentOfRightAnswers.value == true,
             rightAnswers,
            countOfQuestions,
            gameSettings
        )
    }

    private fun generateQuestion(){
        _question.value = generateQuestionUseCase(gameSettings.maxSumValue)
    }

    override fun onCleared() {
        super.onCleared()
        timer.cancel()
    }
}