package com.example.quizapp.data.model

import com.google.firebase.firestore.DocumentId

// Modelo de dados para a pergunta.
// O construtor vazio é OBRIGATÓRIO para o Firestore.
data class Question(
    @DocumentId val id: String = "",
    val text: String = "",
    val options: List<String> = emptyList(),
    val correctOptionIndex: Int = 0
)