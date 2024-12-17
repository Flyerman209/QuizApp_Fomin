package com.example.quizapp

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private var currentQuestionIndex = 0
    private var score = 0

    private val questions = listOf(
        Pair("Как называется столица России?", listOf("Москва", "Санкт-Петербург", "Казань", "Новосибирск")),
        Pair("Какая планета ближе всего к Солнцу?", listOf("Меркурий", "Земля", "Марс", "Венера")),
        Pair("Сколько дней в году?", listOf("365", "364", "366", "363")),
        Pair("Какое животное самое быстрое на Земле?", listOf("Гепард", "Орёл", "Лев", "Антилопа")),
        Pair("В каком году человек впервые высадился на Луну?", listOf("1969", "1971", "1965", "1959")),
        Pair("Кто написал роман 'Война и мир'?", listOf("Толстой", "Достоевский", "Пушкин", "Гоголь")),
        Pair("Сколько материков на Земле?", listOf("7", "6", "8", "5")),
        Pair("Как называется самая длинная река в мире?", listOf("Амазонка", "Нил", "Миссисипи", "Енисей")),
        Pair("В каком году началась Вторая мировая война?", listOf("1939", "1941", "1938", "1945")),
        Pair("Какое самое большое животное на Земле?", listOf("Синий кит", "Слон", "Акула", "Крокодил"))
    ).shuffled() // Перемешиваем вопросы
    private val correctAnswers = mapOf(
        "Как называется столица России?" to "Москва",
        "Какая планета ближе всего к Солнцу?" to "Меркурий",
        "Сколько дней в году?" to "365",
        "Какое животное самое быстрое на Земле?" to "Гепард",
        "В каком году человек впервые высадился на Луну?" to "1969",
        "Кто написал роман 'Война и мир'?" to "Толстой",
        "Сколько материков на Земле?" to "7",
        "Как называется самая длинная река в мире?" to "Амазонка",
        "В каком году началась Вторая мировая война?" to "1939",
        "Какое самое большое животное на Земле?" to "Синий кит"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val questionText = findViewById<TextView>(R.id.question_text)
        val questionCountText = findViewById<TextView>(R.id.question_count)
        val answersGroup = findViewById<RadioGroup>(R.id.answers_group)
        val submitButton = findViewById<Button>(R.id.submit_button)
        // Загружаем первый вопрос
        loadQuestion(questionText, questionCountText, answersGroup)
        submitButton.setOnClickListener {
            val selectedOptionId = answersGroup.checkedRadioButtonId
            if (selectedOptionId == -1) {
                Toast.makeText(this, "Пожалуйста, выберите ответ!", Toast.LENGTH_SHORT).
                show()
                return@setOnClickListener
            }
            val selectedAnswer = findViewById<RadioButton>(selectedOptionId).text.toString()
            val currentQuestion = questions[currentQuestionIndex].first

            if (selectedAnswer == correctAnswers[currentQuestion]) {
                score++
            }
            currentQuestionIndex++

            if (currentQuestionIndex < questions.size) {
                loadQuestion(questionText, questionCountText, answersGroup)
            } else {
                showResults()
            }
        }
    }

    private fun loadQuestion(
        questionText: TextView,
        questionCountText: TextView,
        answersGroup: RadioGroup
    ) {
        val question = questions[currentQuestionIndex]
        questionText.text = question.first
        questionCountText.text = "Вопрос ${currentQuestionIndex + 1} из ${questions.size}"

        answersGroup.removeAllViews()
        val shuffledAnswers = question.second.shuffled()
        shuffledAnswers.forEach { answer ->
            val radioButton = RadioButton(this)
            radioButton.text = answer
            radioButton.textSize = 18f
            radioButton.setPadding(16, 16, 16, 16)
            answersGroup.addView(radioButton)
        }
    }

    private fun showResults() {
        val resultMessage = when (score) {
            in 9..10 -> "Отлично! Вы набрали $score из 10!"
            in 7..8 -> "Хорошо! Вы набрали $score из 10!"
            in 5..6 -> "Средне! Вы набрали $score из 10."
            else -> "Плохо. Вы набрали $score из 10. Попробуйте ещё раз!"
        }

        AlertDialog.Builder(this)
            .setTitle("Результаты викторины")
            .setMessage(resultMessage)
            .setPositiveButton("Перезапустить") { _, _ ->
                restartQuiz()
            }
            .setNegativeButton("Выход") { _, _ -> finish() }
            .show()
    }

    private fun restartQuiz() {
        currentQuestionIndex = 0
        score = 0
        loadQuestion(
            findViewById(R.id.question_text),
            findViewById(R.id.question_count),
            findViewById(R.id.answers_group)
        )
    }
}
