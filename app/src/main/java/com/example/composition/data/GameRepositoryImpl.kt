package com.example.composition.data

import com.example.composition.domain.entity.GameSettings
import com.example.composition.domain.entity.Level
import com.example.composition.domain.entity.Question
import com.example.composition.domain.repository.GameRepository
import kotlin.random.Random
import kotlin.random.nextInt

object GameRepositoryImpl:GameRepository {
    override fun generateQuestion(maxSumValue: Int, countOfOptions: Int): Question {
        val variables = HashSet<Int>()
        var sumInQuestion = Random.nextInt(2 until maxSumValue)
        var visibleNum = Random.nextInt(1 until sumInQuestion)
        var trueAnswer = sumInQuestion - visibleNum
        variables.add(trueAnswer)
        while (variables.count() < countOfOptions){
            variables.add(Random.nextInt(1..sumInQuestion))
        }
        return Question(sumInQuestion, visibleNum, variables.toList())
    }

    override fun getGameSettings(level: Level): GameSettings {
        return when (level){
            Level.TEST-> GameSettings(10, 3, 50, 10)
            Level.EASY-> GameSettings(15, 6, 50, 30)
            Level.NORMAL-> GameSettings(40, 6, 60, 30)
            Level.HARD->GameSettings(100, 6, 70, 30)
        }
    }
}