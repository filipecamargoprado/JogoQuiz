package com.example.quizapp.ui.view.results

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

// Tela 3: A tela de resultados
@Composable
fun ResultsScreen(
    score: Int,
    totalQuestions: Int,
    onPlayAgainClick: () -> Unit
) {
    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Fim de Jogo!",
                style = MaterialTheme.typography.displaySmall
            )
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                "Seu resultado:",
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(8.dp))
            // Mostra o placar
            Text(
                "$score / $totalQuestions",
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(48.dp))

            Button(
                onClick = onPlayAgainClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Jogar Novamente", fontSize = MaterialTheme.typography.titleMedium.fontSize)
            }
        }
    }
}