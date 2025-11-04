package com.example.quizapp

import android.app.Application
import com.google.firebase.FirebaseApp

// Esta classe herda de 'Application', tornando-se o ponto de entrada
// principal do seu app, executado antes de qualquer Activity.
class QuizApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // 1. INICIALIZAÇÃO DO FIREBASE:
        // Embora o Firebase geralmente se inicialize sozinho,
        // é uma boa prática inicializá-lo explicitamente aqui
        // para garantir que ele esteja pronto antes de qualquer uso.
        FirebaseApp.initializeApp(this)


        // 2. EXEMPLO OPCIONAL (Habilitar persistência offline do Firestore):
        // Se você quisesse que seu app funcionasse offline (lendo dados
        // do cache quando não há internet), você descomentaria estas linhas:

        /*
        import com.google.firebase.firestore.ktx.firestore
        import com.google.firebase.firestore.ktx.firestoreSettings
        import com.google.firebase.ktx.Firebase

        val settings = firestoreSettings {
            isPersistenceEnabled = true
        }
        Firebase.firestore.firestoreSettings = settings
        */

    }
}