package com.example.quizapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.quizapp.ui.view.home.HomeScreen
import com.example.quizapp.ui.view.quiz.QuizScreen
import com.example.quizapp.ui.view.results.ResultsScreen

// Objeto para centralizar as rotas e evitar erros de digitação
object AppRoutes {
    const val HOME = "home"
    const val QUIZ = "quiz"
    const val RESULTS = "results"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = AppRoutes.HOME) {

        // Tela 1: Home
        composable(AppRoutes.HOME) {
            HomeScreen(
                onStartClick = {
                    navController.navigate(AppRoutes.QUIZ)
                }
            )
        }

        // Tela 2: Quiz
        composable(AppRoutes.QUIZ) {
            QuizScreen(
                onQuizFinished = { score, totalQuestions ->
                    // Navega para os resultados, passando os argumentos
                    navController.navigate("${AppRoutes.RESULTS}/$score/$totalQuestions") {
                        // Remove a tela de quiz da pilha de navegação
                        popUpTo(AppRoutes.QUIZ) { inclusive = true }
                    }
                }
            )
        }

        // Tela 3: Resultados
        composable(
            route = "${AppRoutes.RESULTS}/{score}/{total}", // Define os argumentos na rota
            arguments = listOf(
                navArgument("score") { type = NavType.IntType },
                navArgument("total") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            // Coleta os argumentos passados
            val score = backStackEntry.arguments?.getInt("score") ?: 0
            val total = backStackEntry.arguments?.getInt("total") ?: 0

            ResultsScreen(
                score = score,
                totalQuestions = total,
                onPlayAgainClick = {
                    // Volta para a Home, limpando a tela de resultados da pilha
                    navController.navigate(AppRoutes.HOME) {
                        popUpTo(AppRoutes.RESULTS) { inclusive = true }
                    }
                }
            )
        }
    }
}