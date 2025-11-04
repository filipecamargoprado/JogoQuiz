package com.example.jogoquiz.ui.view.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jogoquiz.data.model.Question
import com.example.jogoquiz.data.repository.QuizRepository
import com.example.jogoquiz.data.repository.QuizRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Data class que representa o estado da tela do Quiz
data class QuizUiState(
    val questions: List<Question> = emptyList(),
    val currentQuestionIndex: Int = 0,
    val selectedOptionIndex: Int? = null, // Nenhuma opção selecionada ainda
    val isAnswerChecked: Boolean = false, // A resposta já foi verificada?
    val score: Int = 0,
    val isLoading: Boolean = true,
    val isQuizFinished: Boolean = false // O quiz terminou?
)

class QuizViewModel(private val repository: QuizRepository = QuizRepositoryImpl()) : ViewModel() {

    private val _uiState = MutableStateFlow(QuizUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadQuestions()
    }

    private fun loadQuestions() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            // Coleta as perguntas do repositório
            repository.getQuizQuestions().collect { questions ->
                _uiState.update {
                    it.copy(
                        // Embaralha as perguntas para o jogo ser diferente
                        questions = questions.shuffled(),
                        isLoading = false
                    )
                }
            }
        }
    }

    // Chamado quando o usuário clica em uma opção
    fun onOptionSelected(optionIndex: Int) {
        if (_uiState.value.isAnswerChecked) return // Não permite mudar após verificar

        _uiState.update { it.copy(selectedOptionIndex = optionIndex) }
    }

    // Chamado quando o usuário clica em "Verificar Resposta"
    fun checkAnswer() {
        if (_uiState.value.isAnswerChecked) return
        if (_uiState.value.selectedOptionIndex == null) return

        _uiState.update { currentState ->
            val currentQuestion = currentState.questions[currentState.currentQuestionIndex]
            val isCorrect = currentState.selectedOptionIndex == currentQuestion.correctOptionIndex
            val newScore = if (isCorrect) currentState.score + 1 else currentState.score

            currentState.copy(
                isAnswerChecked = true,
                score = newScore
            )
        }
    }

    // Chamado quando o usuário clica em "Próxima"
    fun nextQuestion() {
        _uiState.update { currentState ->
            val nextIndex = currentState.currentQuestionIndex + 1
            if (nextIndex >= currentState.questions.size) {
                // Fim do quiz
                currentState.copy(isQuizFinished = true)
            } else {
                // Prepara para a próxima pergunta
                currentState.copy(
                    currentQuestionIndex = nextIndex,
                    selectedOptionIndex = null,
                    isAnswerChecked = false
                )
            }
        }
    }
}