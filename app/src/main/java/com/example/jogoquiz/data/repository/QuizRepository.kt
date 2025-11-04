package com.example.quizapp.data.repository

import com.example.quizapp.data.model.Question
import kotlinx.coroutines.flow.Flow

// Interface (contrato) para o repositório.
interface QuizRepository {
    fun getQuizQuestions(): Flow<List<Question>>
}