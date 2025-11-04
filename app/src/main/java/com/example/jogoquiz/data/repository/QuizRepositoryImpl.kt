package com.example.jogoquiz.data.repository

import com.example.jogoquiz.data.model.Question
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObjects
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

// Implementação concreta que usa o Firebase Firestore.
class QuizRepositoryImpl(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) : QuizRepository {

    override fun getQuizQuestions(): Flow<List<Question>> = callbackFlow {
        // Aponta para a coleção "questions" que você criou no Firestore
        val collection = firestore.collection("questions")

        val listener = collection.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error) // Fecha o flow em caso de erro
                return@addSnapshotListener
            }

            if (snapshot != null) {
                // Converte os documentos em objetos Question
                val questions = snapshot.toObjects<Question>()
                trySend(questions).isSuccess // Envia a lista para o flow
            }
        }

        // Remove o listener quando o flow é cancelado
        awaitClose { listener.remove() }
    }
}