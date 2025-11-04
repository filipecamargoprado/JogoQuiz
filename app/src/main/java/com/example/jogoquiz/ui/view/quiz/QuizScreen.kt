package com.example.quizapp.ui.view.quiz

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

// Tela 2: A tela do Quiz
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
    onQuizFinished: (score: Int, totalQuestions: Int) -> Unit,
    viewModel: QuizViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // Efeito que observa a flag 'isQuizFinished'.
    // Quando for true, dispara a navegação para a tela de resultados.
    LaunchedEffect(uiState.isQuizFinished) {
        if (uiState.isQuizFinished) {
            onQuizFinished(uiState.score, uiState.questions.size)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (!uiState.isLoading) {
                        Text(
                            "Questão ${uiState.currentQuestionIndex + 1} de ${uiState.questions.size}"
                        )
                    } else {
                        Text("Carregando...")
                    }
                }
            )
        },
        bottomBar = {
            // Barra inferior com os botões de "Verificar" ou "Próxima"
            BottomButtonRow(
                isAnswerChecked = uiState.isAnswerChecked,
                isOptionSelected = uiState.selectedOptionIndex != null,
                onCheckClick = viewModel::checkAnswer,
                onNextClick = viewModel::nextQuestion
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (uiState.questions.isNotEmpty()) {
                val currentQuestion = uiState.questions[uiState.currentQuestionIndex]

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Texto da Pergunta
                    Text(
                        text = currentQuestion.text,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Lista de Opções (LazyColumn não é necessária aqui, pois são poucas opções)
                    currentQuestion.options.forEachIndexed { index, option ->
                        OptionButton(
                            text = option,
                            isSelected = uiState.selectedOptionIndex == index,
                            isAnswerChecked = uiState.isAnswerChecked,
                            isCorrect = index == currentQuestion.correctOptionIndex,
                            onOptionClick = { viewModel.onOptionSelected(index) }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }
    }
}

// Composable customizado para o botão de opção
@Composable
fun OptionButton(
    text: String,
    isSelected: Boolean,
    isAnswerChecked: Boolean,
    isCorrect: Boolean,
    onOptionClick: () -> Unit
) {
    // Lógica para definir a cor do botão baseado no estado
    val buttonColors = when {
        isAnswerChecked -> {
            if (isCorrect) { // Opção correta (fica verde)
                ButtonDefaults.buttonColors(containerColor = Color.Green.copy(alpha = 0.7f))
            } else if (isSelected) { // Opção errada selecionada (fica vermelha)
                ButtonDefaults.buttonColors(containerColor = Color.Red.copy(alpha = 0.7f))
            } else { // Opção não selecionada (fica cinza)
                ButtonDefaults.buttonColors(containerColor = Color.Gray.copy(alpha = 0.3f))
            }
        }
        isSelected -> { // Opção selecionada, mas não verificada
            ButtonDefaults.buttonColors() // Cor primária padrão
        }
        else -> { // Opção padrão
            ButtonDefaults.outlinedButtonColors()
        }
    }

    val border = if (isSelected && !isAnswerChecked) BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null

    OutlinedButton(
        onClick = onOptionClick,
        modifier = Modifier.fillMaxWidth().height(55.dp),
        colors = buttonColors,
        border = border,
        enabled = !isAnswerChecked // Desabilita o botão após a verificação
    ) {
        Text(text, fontSize = MaterialTheme.typography.bodyLarge.fontSize)
    }
}

// Composable para a barra de botões inferior
@Composable
fun BottomButtonRow(
    isAnswerChecked: Boolean,
    isOptionSelected: Boolean,
    onCheckClick: () -> Unit,
    onNextClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        if (isAnswerChecked) {
            // Mostra "Próxima" se a resposta foi verificada
            Button(
                onClick = onNextClick,
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Text("Próxima")
            }
        } else {
            // Mostra "Verificar Resposta" se ainda não foi
            Button(
                onClick = onCheckClick,
                enabled = isOptionSelected, // Habilita só se uma opção foi selecionada
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Text("Verificar Resposta")
            }
        }
    }
}