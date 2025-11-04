package com.example.jogoquiz.data.repository

import com.example.jogoquiz.data.model.Question
import kotlinx.coroutines.flow.Flow

// Interface (contrato) para o repositório.
interface QuizRepository {
    fun getQuizQuestions(): Flow<List<Question>>
}